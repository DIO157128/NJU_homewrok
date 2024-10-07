#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include<iostream>
#include<string>
#include<sstream>
#include<vector>
#include<regex>
#include <algorithm>
using namespace std;
typedef unsigned char BYTE1;	//1字节
typedef unsigned short BYTE2;	//2字节
typedef unsigned int BYTE4;	//4字节
int  BytsPerSec;	//每扇区字节数
int  SecPerClus;	//每簇扇区数
int  RsvdSecCnt;	//Boot记录占用的扇区数
int  NumFATs;	//FAT表个数
int  RootEntCnt;	//根目录最大文件数
int  FATSz;	//FAT扇区数
bool is_l;
extern "C" {
void asm_print(const char *, const int);
}

#pragma pack (1) //指定按1字节对齐
struct BPB {
    BYTE2  BPB_BytsPerSec;	//每扇区字节数
    BYTE1   BPB_SecPerClus;	//每簇扇区数
    BYTE2  BPB_RsvdSecCnt;	//Boot记录占用的扇区数
    BYTE1   BPB_NumFATs;	//FAT表个数
    BYTE2  BPB_RootEntCnt;	//根目录最大文件数
    BYTE2  BPB_TotSec16;		//扇区总数
    BYTE1   BPB_Media;		//介质描述符
    BYTE2  BPB_FATSz16;	//FAT扇区数
    BYTE2  BPB_SecPerTrk;  //	每磁道扇区数（Sector/track）
    BYTE2  BPB_NumHeads;	//磁头数（面数）
    BYTE4  BPB_HiddSec;	//隐藏扇区数
    BYTE4  BPB_TotSec32;	//如果BPB_FATSz16为0，该值为FAT扇区数
};
struct RootEntry {
    char DIR_Name[11];  //文件名8字节，扩展名3字节
    BYTE1 DIR_Attr;		 //文件属性
    char reserved[10];
    BYTE2 DIR_WrtTime;
    BYTE2 DIR_WrtDate;
    BYTE2 DIR_FstClus;	//开始簇号
    BYTE4 DIR_FileSize;
};
#pragma pack ()
class Node {//链表的node类
public:
    string name;		//名字
    vector<Node *> next;	//下一级目录的Node数组
    string path;			//记录path，便于打印操作
    BYTE4 FileSize;			//文件大小
    bool isfile = false;		//是文件还是目录
    bool isval = true;			//用于标记是否是.和..
    int dir_count = 0;			//记录下一级有多少目录
    int file_count = 0;			//记录下一级有多少文件
    char *content = new char[10000];		//存放文件内容
};
void asmprint(const char* p){
    asm_print(p, strlen(p));
}
void addinitNode(Node *p) {
    Node *q = new Node();
    q->name = ".";
    q->isval = false;
    p->next.push_back(q);
    q = new Node();
    q->name = "..";
    q->isval = false;
    p->next.push_back(q);
}
int  getFATValue(FILE * fat12, int num) {
    //FAT1的偏移字节
    int fatBase = RsvdSecCnt * BytsPerSec;
    //FAT项的偏移字节
    int fatPos = fatBase + num * 3 / 2;
    //奇偶FAT项处理方式不同，分类进行处理，从0号FAT项开始
    int type = num % 2;
    //先读出FAT项所在的两个字节
    BYTE2 bytes;
    BYTE2* bytes_ptr = &bytes;
    fseek(fat12, fatPos, SEEK_SET);
    fread(bytes_ptr, 1, 2, fat12);
    //u16为short，结合存储的小尾顺序和FAT项结构可以得到
    //type为0的话，取byte2的低4位和byte1构成的值，type为1的话，取byte2和byte1的高4位构成的值
    if (type == 0) {
        bytes = bytes << 4;   //这里原文错误，原理建议看网上关于FAT表的文章
        return bytes >> 4;
    }
    else {
        return bytes >> 4;
    }
}
void readContent(FILE * fat12, int startClus, Node *son) {//获取文件内容
    int dataBase = BytsPerSec * (RsvdSecCnt + FATSz * NumFATs + (RootEntCnt * 32 + BytsPerSec - 1) / BytsPerSec);
    int currentClus = startClus;
    int curfatvalue = 0;		//这里用value来进行不同簇的读取（超过512字节）
    char *p = son->content;
    while(curfatvalue<0xFF8){
        int curbase = dataBase+(currentClus-2)*SecPerClus*BytsPerSec;
        curfatvalue = getFATValue(fat12,currentClus);
        if (curfatvalue == 0xFF7) {
            cout<<"Bad Cluster!"<<endl;
            break;
        }
        char* str = (char*)malloc(SecPerClus*BytsPerSec);	//暂存从簇中读出的数据
        char *content = str;
        fseek(fat12, curbase, SEEK_SET);
        fread(content, 1, SecPerClus*BytsPerSec, fat12);//提取数据
        int count = SecPerClus * BytsPerSec;
        for (int i = 0; i < count; i++) {//读取赋值
            *p = content[i];
            p++;
        }
        free(str);
        currentClus = curfatvalue;

    }
}
void readChildren(FILE * fat12, int startClus, Node *father) {
    //数据区的第一个簇（即2号簇）的偏移字节
    int dataBase = BytsPerSec * (RsvdSecCnt + FATSz * NumFATs + (RootEntCnt * 32 + BytsPerSec - 1) / BytsPerSec);
    char tempname[12];
    int currentClus = startClus;
    int curfatvalue = 0;//value用来查看是否存在多个簇（查FAT表）
    while (curfatvalue < 0xFF8) {
        int curbase = dataBase+(currentClus-2)*SecPerClus*BytsPerSec;
        curfatvalue = getFATValue(fat12,currentClus);
        if (curfatvalue == 0xFF7) {
            cout<<"Bad Cluster!"<<endl;
            break;
        }
        int count = SecPerClus * BytsPerSec;	//每簇的字节数
        int loop = 0;
        while (loop < count) {
            int i;
            RootEntry sonEntry;//读取目录项
            RootEntry *sonEntry_ptr = &sonEntry;
            fseek(fat12, curbase + loop, SEEK_SET);
            fread(sonEntry_ptr, 1, 32, fat12);

            loop += 32;
            if (sonEntry_ptr->DIR_Name[0] == '\0') {
                continue;
            }	//空条目不输出
            //过滤非目标文件
            int j;
            int boolean = 0;
            for (j = 0; j < 11; j++) {
                if (!(((sonEntry_ptr->DIR_Name[j] >= 48) && (sonEntry_ptr->DIR_Name[j] <= 57)) ||
                      ((sonEntry_ptr->DIR_Name[j] >= 65) && (sonEntry_ptr->DIR_Name[j] <= 90)) ||
                      ((sonEntry_ptr->DIR_Name[j] >= 97) && (sonEntry_ptr->DIR_Name[j] <= 122)) ||
                      (sonEntry_ptr->DIR_Name[j] == ' '))) {
                    boolean = 1;	//非英文及数字、空格
                    break;
                }
            }
            if (boolean == 1) {
                continue;
            }
            if(sonEntry_ptr->DIR_Attr ==0x10){
                //0x10为目录
                for(int j =0 ;j<11;j++){
                    if (sonEntry_ptr->DIR_Name[j] != ' ') {
                        tempname[j] = sonEntry_ptr->DIR_Name[j];
                    }
                    else {
                        tempname[j] = '\0';
                        break;
                    }
                }
                Node *son = new Node();
                father->next.push_back(son);
                son->name = tempname;
                cout<<son->name<<endl;
                son->path = father->path + tempname + "/";
                father->dir_count++;
                addinitNode(son);
                //输出目录及子文件
                readChildren(fat12,sonEntry_ptr->DIR_FstClus,son);
            } else{
                int lastidx = 0;
                for(int j =0 ;j<8;j++){
                    if (sonEntry_ptr->DIR_Name[j] != ' ') {
                        tempname[j] = sonEntry_ptr->DIR_Name[j];
                    }
                    else {
                        tempname[j] = '.';
                        lastidx = j+1;
                        break;
                    }
                }
                for(int j =8 ;j<11;j++){
                    tempname[lastidx++] = sonEntry_ptr->DIR_Name[j];
                }
                tempname[lastidx] = '\0';
                Node *son = new Node();   //新建该文件的节点
                father->next.push_back(son);  //存到father的next数组中
                son->name = tempname;
                cout<<son->name<<endl;
                son->FileSize = sonEntry_ptr->DIR_FileSize;
                son->isfile = true;
                son->path = father->path + tempname ;
                father->file_count++;
                readContent(fat12, sonEntry_ptr->DIR_FstClus, son);
            }


        }


        currentClus = curfatvalue;//下一个簇
    };
}
void loadBPBparams(FILE * fat12, BPB* bpb_ptr){
    //偏移11个字节到BPB开始处
    fseek(fat12, 11, SEEK_SET);
    //这里因为让struct按字节对齐，所以BPB参数连续，可以直接读到struct里
    fread(bpb_ptr, 1, 25, fat12);
};
void readRootentry(FILE * fat12, Node *father){
    int base = (RsvdSecCnt + NumFATs * FATSz) * BytsPerSec;	//根目录首字节的偏移数
    char tempname[12];	//暂存文件名
    struct RootEntry rootEntry;
    struct RootEntry* rootEntry_ptr = &rootEntry;
    //依次处理根目录中的各个条目
    for(int i =0;i<RootEntCnt;i++){
        fseek(fat12,base,SEEK_SET);
        fread(rootEntry_ptr,1,32,fat12);
        base+=32;
        if (rootEntry_ptr->DIR_Name[0] == '\0') continue;
        int j;
        int boolean = 0;
        for (j = 0; j < 11; j++) {
            if (!(((rootEntry_ptr->DIR_Name[j] >= 48) && (rootEntry_ptr->DIR_Name[j] <= 57)) ||
                  ((rootEntry_ptr->DIR_Name[j] >= 65) && (rootEntry_ptr->DIR_Name[j] <= 90)) ||
                  ((rootEntry_ptr->DIR_Name[j] >= 97) && (rootEntry_ptr->DIR_Name[j] <= 122)) ||
                  (rootEntry_ptr->DIR_Name[j] == ' '))) {
                boolean = 1;	//非英文及数字、空格
                break;
            }
        }
        if (boolean == 1) continue;	//非目标文件不输出

        if(rootEntry_ptr->DIR_Attr ==0x10){
            //0x10为目录
            for(int j =0 ;j<11;j++){
                if (rootEntry_ptr->DIR_Name[j] != ' ') {
                    tempname[j] = rootEntry_ptr->DIR_Name[j];
                }
                else {
                    tempname[j] = '\0';
                    break;
                }
            }
            Node *son = new Node();
            father->next.push_back(son);
            son->name = tempname;
            cout<<son->name<<endl;
            son->path = father->path + tempname + "/";
            father->dir_count++;
            addinitNode(son);
            //输出目录及子文件
            readChildren(fat12,rootEntry_ptr->DIR_FstClus,son);
        } else{
            int lastidx = 0;
            for(int j =0 ;j<8;j++){
                if (rootEntry_ptr->DIR_Name[j] != ' ') {
                    tempname[j] = rootEntry_ptr->DIR_Name[j];
                }
                else {
                    tempname[j] = '.';
                    lastidx = j+1;
                    break;
                }
            }
            for(int j =8 ;j<11;j++){
                tempname[lastidx++] = rootEntry_ptr->DIR_Name[j];
            }
            tempname[lastidx] = '\0';
            Node *son = new Node();   //新建该文件的节点
            father->next.push_back(son);  //存到father的next数组中
            son->name = tempname;
            cout<<son->name<<endl;
            son->FileSize = rootEntry_ptr->DIR_FileSize;
            son->isfile = true;
            son->path = father->path + tempname ;
            father->file_count++;
            readContent(fat12, rootEntry_ptr->DIR_FstClus, son);
        }
    }
}
vector<string> split(const string& str, const string& delim) {
    vector<string> res;
    if("" == str) return res;
    //先将要切割的字符串从string类型转换为char*类型
    char * strs = new char[str.length() + 1] ; //不要忘了
    strcpy(strs, str.c_str());

    char * d = new char[delim.length() + 1];
    strcpy(d, delim.c_str());

    char *p = strtok(strs, d);
    while(p) {
        string s = p; //分割得到的字符串转换为string类型
        res.push_back(s); //存入结果数组
        p = strtok(NULL, d);
    }

    return res;
}
bool isValidCmd(string cmd){
    string validcmds[3]={"ls","cat","exit"};
    for(string i:validcmds){
        if(cmd==i){
            return 1;
        }
    }
    return 0;
}
Node* findPathinNode(Node* node,string path){
    if(node->path==path){
        return node;
    } else if (node->next.size()==0){
        return nullptr;
    } else{
        for(Node* n:node->next){
            Node* son = findPathinNode(n,path);
            if(son!= nullptr){
                return son;
            }
        }
        return nullptr;
    }
}
bool validJudge(vector<string>command,vector<string>param,vector<string>path){
    is_l= false;
    regex validparam("-l+");
    if(path.size()>1){
        asmprint("Multiple paths!\n");
//        cout<<"Multiple paths!"<<endl;
        return 0;
    }

    for(int j =0;j<param.size();j++){
        smatch result;
        bool ret = regex_match(param[j], result, validparam);
        if(!ret){
            asmprint("Invalid parameter!\n");
//            cout<<"Invalid parameter!"<<endl;
            return 0;
        } else{
            is_l= true;
        }
    }
    for(int j =0;j<command.size();j++){
        if(!isValidCmd(command[j])) {
            asmprint("Invalid command!\n");
//            cout << "Invalid command!" << endl;
            return 0;
        }
    }
    return 1;
}
void printls(Node *root){
    if(!root->isval){
        return;
    }
    if(root->isfile){
        return;
    }
//    cout<<root->path<<":"<<endl;
    string tem_toprint;
    tem_toprint = root->path+":\n";
    asmprint(tem_toprint.c_str());
    for(Node* n :root->next){
        if(n->isfile){
            tem_toprint = n->name+"  ";
            asmprint(tem_toprint.c_str());
//            cout<<n->name<<"  ";
        } else{
            tem_toprint="\033[31m"+n->name+"  \033[31m";
            asmprint(tem_toprint.c_str());
            asmprint("\033[37m\033[37m");
//            cout<<n->name<<"  ";
        }
    }
    asmprint("\n");
    for(Node* n :root->next){
        printls(n);
    }
}
void printlsl(Node *root){
    if(!root->isval){
        return;
    }
    if(root->isfile){
        return;
    }
    string tem_toprint;
//    cout<<root->path<<" "<<root->dir_count<<" "<<root->file_count<<":"<<endl;
    tem_toprint= root->path + " " + to_string(root->dir_count)+" "+ to_string(root->file_count)+":\n";
    asmprint(tem_toprint.c_str());
    for(Node* n :root->next){
        if(n->isfile){
            tem_toprint=n->name+" "+to_string(n->FileSize)+"\n";
            asmprint(tem_toprint.c_str());
//            cout<<n->name<<" "<<n->FileSize<<endl;

        } else{
            if(n->isval){
                tem_toprint="\033[31m"+n->name+"  "+ to_string(n->dir_count)+" "+ to_string(n->file_count)+"  \033[31m\n";
                asmprint(tem_toprint.c_str());
                asmprint("\033[37m\033[37m");
//                cout<<n->name<<"  "<<n->dir_count<<" "<<n->file_count<<endl;
            } else{
                tem_toprint = "\033[31m"+n->name+"  \033[31m\n";
                asmprint(tem_toprint.c_str());
                asmprint("\033[37m\033[37m");
            }
        }
    }
    asmprint("\n");
    for(Node* n :root->next){
        printlsl(n);
    }
}
string filterpath(string path){
    vector<string>split_res = split(path,"/");
    vector<string>res;
    int cur_ptr=0;
    for(string s:split_res){
        if(s=="."){
            continue;
        }
        if(s==".."){
            if(cur_ptr==0){
                continue;
            } else{
                res.pop_back();
                cur_ptr--;
            }
        } else{
            res.push_back(s);
            cur_ptr++;
        }
    }
    if(cur_ptr==0){
        return "/";
    }
    string final_res;
    for(string s:res){
        final_res+="/"+s;
    }
    return final_res;
}
int main(){
    FILE* fat12;
    fat12 = fopen("/mnt/hgfs/OSshare/OS/lab2-asm/a2.img", "r");	//打开FAT12的映像文件
    struct BPB bpb;
    struct BPB* bpb_ptr = &bpb;
    Node *root = new Node();
    root->name = "";
    root->path = "/";
    loadBPBparams(fat12,bpb_ptr);
    BytsPerSec = bpb_ptr->BPB_BytsPerSec;
    SecPerClus = bpb_ptr->BPB_SecPerClus;
    RsvdSecCnt = bpb_ptr->BPB_RsvdSecCnt;
    NumFATs = bpb_ptr->BPB_NumFATs;
    RootEntCnt = bpb_ptr->BPB_RootEntCnt;
    //如果BPB_FATSz16=0,则由BPB_FATSz32给出FAT所占扇区数
    if (bpb_ptr->BPB_FATSz16 != 0) {
        FATSz = bpb_ptr->BPB_FATSz16;
    }
    else {
        FATSz = bpb_ptr->BPB_TotSec32;
    }
    readRootentry(fat12,root);
    while(true){
        string cmd;
        getline(cin ,cmd);
        vector<string>words = split(cmd," ");
        vector<string>command;
        vector<string>param;
        vector<string>path;
        command.push_back(words[0]);
        for(int i =1;i<words.size();i++){
            string prefix_param = "-";
            if(words[i].substr(0,1)==prefix_param){
                param.push_back(words[i]);
            }
            else {
                path.push_back(words[i]);
            }

        }
        if(!validJudge(command,param,path)){
            continue;
        } else{

            if(command[0]=="exit"){
                return 0;
            }
            if(command[0]=="ls"){
                if (path.size()!=0){
                    path[0]= filterpath(path[0]);
                }
                if (path.size()!=0&&path[0].substr(path[0].length()-1,path[0].length())!="/"){
                    path[0]=path[0]+"/";
                }
                if(path.size()!=0&&path[0].find('.')!= string::npos){
                    asmprint("Must be a path!\n");
//                    cout<<"Must be a path!"<<endl;
                    continue;
                }
                Node *cur_node =path.size()==0?root: findPathinNode(root,path[0]);
                if (cur_node== nullptr){
                    asmprint("Path not exist!\n");
//                    cout<<"Path not exist!"<<endl;
                    continue;
                }
                if(is_l){
                    printlsl(cur_node);
                } else{
                    printls(cur_node);
                }
            }
            if(command[0]=="cat"){
                if(path.size()==0){
                    asmprint("Inadequate path!\n");
//                    cout<<"Inadequate path!"<<endl;
                    continue;
                }
                if(param.size()!=0){
                    asmprint("Too many parameters!\n");
//                    cout<<"Too many parameters!"<<endl;
                    continue;
                }
                path[0]= filterpath(path[0]);
                string cur_path = path[0];
                if(cur_path.find('.')== string::npos){
                    asmprint("Must be a file!\n");
//                    cout<<"Must be a file!"<<endl;
                    continue;
                }
                Node * findres = findPathinNode(root,cur_path);
                if (findres== nullptr){
                    asmprint("Path not exist!\n");
//                    cout<<"Path not exist!"<<endl;
                    continue;
                }

                char * content = findres->content;
//                cout<<content<<endl;
                asmprint(content);
                asmprint("\n");
            }
        }

    }
}


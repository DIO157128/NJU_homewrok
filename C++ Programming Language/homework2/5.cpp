#include <iostream>
#include <string>
using namespace std;
int findrptr(string cmd,int cmdptr){
    int lcount=1;
    for(int i=cmdptr+1;i< cmd.length();i++){
        if(cmd[i]=='['){
            lcount++;
        }
        if(cmd[i]==']'){
            lcount--;
        }
        if(lcount==0){
            return i;
        }
    }
}
int findlptr(string cmd,int cmdptr){
    int rcount=1;
    for(int i=cmdptr-1;i>=0;i--){
        if(cmd[i]==']'){
            rcount++;
        }
        if(cmd[i]=='['){
            rcount--;
        }
        if(rcount==0){
            return i;
        }
    }
}
int main(){
    string cmd;
    std::getline(std::cin, cmd);
    int space[65536];
    for(int i=0;i<65536;i++){
        space[i]=0;
    }
    int nowptr=0;
    int cmdptr=0;
    while(cmdptr< cmd.length()){
        if(cmd[cmdptr]=='+'){
            space[nowptr]++;
            cmdptr++;
        }
        else if(cmd[cmdptr]=='-'){
            space[nowptr]--;
            cmdptr++;
        }
        else if(cmd[cmdptr]=='>'){
            nowptr++;
            cmdptr++;
        }
        else if(cmd[cmdptr]=='<'){
            nowptr--;
            cmdptr++;
        }
        else if(cmd[cmdptr]==','){
            char c;
            if(!(cin.get(c))){
                space[nowptr]=0;
            } else{
                space[nowptr]=c;
            }
            cmdptr++;
        }
        else if(cmd[cmdptr]=='.'){
            char b=space[nowptr];
            cout<<b;
            cmdptr++;
        }
        else if(cmd[cmdptr]=='['){
            int rptr= findrptr(cmd,cmdptr);
            if(space[nowptr]==0){
                cmdptr=rptr+1;
            }
            else cmdptr++;
        }
        else if(cmd[cmdptr]==']'){
            int lptr= findlptr(cmd,cmdptr);
            if(space[nowptr]!=0){
                cmdptr=lptr+1;
            }
            else cmdptr++;
        }
    }

    return 0;
}

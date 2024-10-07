#include <iostream>
#include <stdio.h>
using namespace std;
int len(char *a){
    int len=0;
    while(a[len]!='\0'){
        len++;
    }
    return len;
}
int judtyp(char*a){
    char *add="add";
    char *remove="remove";
    char *get="get";
    char *getSize="getSize";
    char *getCapacity="getCapacity";
    int typ=0;
    if(len(a)==3){
        typ=1;
        for(int i=0;i<3;i++){
            if(a[i]!=add[i]){
                typ=0;
                break;
            }
        }
        if(typ!=0){
            return typ;
        }
        else{
            typ=3;
            for(int i=0;i<3;i++){
                if(a[i]!=get[i]){
                    typ=0;
                    break;
                }
            }
            return typ;
        }
    }
    else if(len(a)==6){
        typ=2;
        for(int i=0;i<6;i++){
            if(a[i]!=remove[i]){
                typ=0;
                break;
            }
        }
        return typ;
    }
    else if(len(a)==7){
        typ=4;
        for(int i=0;i<7;i++){
            if(a[i]!=getSize[i]){
                typ=0;
                break;
            }
        }
        return typ;
    }
    else if(len(a)==11){
        typ=5;
        for(int i=0;i<11;i++){
            if(a[i]!=getCapacity[i]){
                typ=0;
                break;
            }
        }
        return typ;
    }
    else return typ;
}
int main(){
    char cmd[1000];
    int n;
    cin>>n;
    int para;
    bool first=true;
    int nowcap=0;
    int nowptr=0;
    int arraylist[65536];
    for(int i=0;i<n;i++){
        scanf("%s",cmd);
        if(judtyp(cmd)<=3&&judtyp(cmd)>=1){
            cin>>para;
        }
        switch(judtyp(cmd)){
            case 1:{
                if(first){
                    nowcap=10;
                    arraylist[nowptr++]=para;
                    first=false;
                }
                else{
                    if(nowptr==nowcap-1){
                        nowcap=nowcap+(nowcap/2);
                    }
                    arraylist[nowptr++]=para;
                }
                break;
            }
            case 2:{
                int temptr=-1;
                for(int i=0;i<nowcap;i++){
                    if(arraylist[i]==para){
                        temptr=i;
                        break;
                    }
                }
                if(temptr>=0){
                    for(int i=temptr;i<nowcap-1;i++){
                        arraylist[i]=arraylist[i+1];
                    }
                    nowptr-=1;
                }
                break;
            }
            case 3:{
                if(para>=0&&para<nowptr){
                    cout<<arraylist[para]<<endl;
                }
                else cout<<-1<<endl;
                break;
            }
            case 4:{
                cout<<nowptr<<endl;
                break;
            }
            case 5:{
                cout<<nowcap<<endl;
                break;
            }
            default:break;
        }
    }
    return 0;
}
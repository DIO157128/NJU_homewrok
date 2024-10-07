#include <iostream>
using namespace std;
int main(){
    string input;
    char temp;
    int countsapce=0;
    int countline=0;
    while(cin.get(temp)){
        input = input +temp;
        if(temp=='\n'){
            countline++;
        }

    }
    bool charflag=false;
    for(int i=0;i<input.length();i++){
        char t=input[i];
        if(t!=' '&&t!='\n'){
            charflag=true;
        }
        if(t==' '||t=='\n'){
            if(charflag){
                countsapce++;
                charflag=false;
            }
        }
    }
    cout<<input.length();
    cout<<' ';
    cout<<countsapce;
    cout<<' ';
    cout<<countline;
    return 0;
}
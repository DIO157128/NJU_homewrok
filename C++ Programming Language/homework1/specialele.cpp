//
// Created by dell on 2022/3/2.
//
#include <iostream>
using namespace std;
int main(){
    int num;
    int res=0;
    while(cin>>num){
        res=res^num;
        char ch=getchar();
        if(ch=='\n'){
            break;
        }
    }
    cout << res;
    return 0;
}

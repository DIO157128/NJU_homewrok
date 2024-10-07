#include <iostream>
using namespace std;
int main(){
    string input;
    int n;
    cin>>input>>n;
    int len=input.length();
    int step=2*n-2;
    if(n==1){
        cout<<input;
        return 0;
    }
    for(int j=0;j<len;j+=step){
        cout<<input[j];
    }
    for(int i=1;i<n-1;i++){
        int index1=i;
        int index2=step-i;
        while(true){
            if(index1<len&&index2<len){
                cout<<input[index1]<<input[index2];
                index1+=step;
                index2+=step;
            }
            if(index1<len&&index2>=len){
                cout<<input[index1];
                index1+=step;
                break;
            }
            if(index1>=len&&index2>=len){
                break;
            }
        }
    }
    for(int j=n-1;j<len;j+=step){
        cout<<input[j];
    }
    return 0;
} 
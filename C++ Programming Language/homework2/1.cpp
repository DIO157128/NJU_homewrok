#include <iostream>
#include<math.h>
using namespace std;
int main() {
    int m,n;
    cin>>m>>n;
    int matrix[m][n];
    for(int i=0;i<m;i++){
        for(int j=0;j<n;j++){
            cin>>matrix[i][j];
        }
    }
    int epoch=0;
    int nowrow=0;
    int nowcol=0;
    int maxepoch= ceil(m/2.0);
    bool exit= false;
    while(epoch<maxepoch){
        nowrow=epoch;
        nowcol=epoch;
        int tem;
        int i;
        for(i=nowcol;i<n-epoch;i++){
            exit= false;
            cout<<matrix[nowrow][i]<<' ';
        }
        if(exit)break;
        exit=true;
        nowcol=i-1;
        for(i=nowrow+1;i<m-epoch;i++){
            exit= false;
            cout<<matrix[i][nowcol]<<' ';
        }
        if(exit)break;
        exit=true;
        nowrow=i-1;
        for(i=nowcol-1;i>=epoch;i--){
            exit= false;
            cout<<matrix[nowrow][i]<<' ';
        }
        if(exit)break;
        exit=true;
        nowcol=i+1;
        for(i=nowrow-1;i>=epoch+1;i--){
            exit= false;
            cout<<matrix[i][nowcol]<<' ';
        }
        if(exit)break;
        exit=true;
        epoch++;
    }
    return 0;
}
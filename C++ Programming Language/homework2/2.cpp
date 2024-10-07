#include <iostream>
using namespace std;
int main(){
    int n;
    int degree;
    cin>>n>>degree;
    int matrix[n][n];
    for(int i=0;i<n;i++){
        for(int j=0;j<n;j++){
            cin>>matrix[i][j];
        }
    }
    int jud=(degree%360)/90;
    switch (jud){
        case 0:
            for(int i=0;i<n;i++){
                for(int j=0;j<n;j++){
                    cout<<matrix[i][j]<<' ';
                }
                cout<<endl;
            }
            break;
        case 1:
            for(int i=0;i<n;i++){
                for(int j=n-1;j>=0;j--){
                    cout<<matrix[j][i]<<' ';
                }
                cout<<endl;
            }
            break;
        case 2:
            for(int i=n-1;i>=0;i--){
                for(int j=n-1;j>=0;j--){
                    cout<<matrix[i][j]<<' ';
                }
                cout<<endl;
            }
            break;
        case 3:
            for(int i=n-1;i>=0;i--){
                for(int j=0;j<n;j++){
                    cout<<matrix[j][i]<<' ';
                }
                cout<<endl;
            }
            break;
    }
    return 0;
}
#include <iostream>
#include <cstddef>
using namespace std;
int **mul(int **pre,int**cur,size_t pre_r,size_t pre_c,size_t cur_r,size_t cur_c){
    int **res;
    res = new int*[pre_r];
    for(int i=0;i<pre_r;i++){
        res[i]=new int[cur_c];
    }
    if(pre== nullptr){
        res=cur;
        return res;
    } else{
        for(int i=0;i<pre_r;i++){
            for(int j=0;j<cur_c;j++){
                int sum=0;
                for(int k=0;k<pre_c;k++){
                    sum+=pre[i][k]*cur[k][j];
                }
                res[i][j]=sum;
            }
        }
        return res;
    }
}
int main(){
    size_t N;
    cin>>N;
    int **pre=nullptr;
    int **cur=nullptr;
    size_t cur_r,cur_c,pre_r,pre_c;
    pre_c=0;
    pre_r=0;
    for(int n=0;n<N;n++){
        cin>>cur_r>>cur_c;
        cur =new int*[cur_r];
        for(int i=0;i<cur_r;i++){
            cur[i]=new int[cur_c];
        }
        for (int i = 0; i < cur_r; i++){
            for (int j = 0; j < cur_c; j++){
                cin >> cur[i][j];
            }
        }
        pre= mul(pre,cur,pre_r,pre_c,cur_r,cur_c);
        if(n==0){
            pre_r=cur_r;
            pre_c=cur_c;
        } else{
            pre_r=pre_r;
            pre_c=cur_c;
        }

    }
    for(int i=0;i<pre_r;i++){
        for(int j=0;j<pre_c;j++){
            cout<<pre[i][j]<<' ';
        }
        cout<<endl;
    }
    return 0;
}
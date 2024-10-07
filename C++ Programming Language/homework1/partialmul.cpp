//
// Created by dell on 2022/3/2.
//
#include <iostream>
#include <limits>
using namespace std;
int main(){
    long long max=numeric_limits<int>::max();
    long long min=numeric_limits<int>::min();
    int N;
    cin>>N;
    int nums[65536];
    for(int i=0;i<N;i++){
        cin>>nums[i];
    }
    long long res =nums[0];
    cout<<res;
    for(int i=1;i<N;i++){
        res*=nums[i];
        if(res>max||res<min){
            cout<<' ';
            cout<<-1;
            return 0;
        }
        cout<<' ';
        cout<<res;
    }
    return 0;
}


#include <iostream>

#include <limits>
#include <stdlib.h>
#include <string>
#include <algorithm>

using namespace std;
string format(string s){
    return s.substr(s.find_first_of('0')+1);
}
int main() {
    long long max=numeric_limits<int>::max();
    long long min=numeric_limits<int>::min();
    int num;
    cin>>num;
    bool numflag=num<0?true: false;
    num=abs(num);
    string s=to_string(num);//数字转成字符串进行反转
    reverse(s.begin(), s.end());
    s=format(s);
    if(numflag){
        s='-'+s;
    }
    long long res = atoll(s.c_str());
    if (res>max||res<min){
        cout<<-1;
        return 0;
    }
    cout<<res;
    return 0;
}

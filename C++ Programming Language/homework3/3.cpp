#include <iostream>
#include <algorithm>
#include <string>
#include <cctype>
using namespace std;
bool defaultCompare(string a,string b){
    return a.compare(b)<0;
}
bool rCompare(string a,string b){
    return a.compare(b)>0;
}
bool nCompare(string a,string b){
    int ai=stoi(a);
    int bi=stoi(b);
    return ai<bi;
}
bool iCompare(string a,string b){
    transform(a.begin(),a.end(),a.begin(),::tolower);
    transform(b.begin(),b.end(),b.begin(),::tolower);
    return a.compare(b)<0;
}
bool dCompare(string a,string b){
    string _a="";
    string _b="";
    for(int i=0;i<a.length();i++){
        if(isdigit(a[i])|| isalpha(a[i])||a[i]==' '){
            _a+=a[i];
        }
    }
    for(int i=0;i<b.length();i++){
        if(isdigit(b[i])|| isalpha(b[i])||b[i]==' '){
            _b+=b[i];
        }
    }
    return _a.compare(_b)<0;
}
int main(){
    int N;
    cin>>N;
    string *s=new string [N];
    cin>>ws;
    for(int i=0;i<N;i++){
        string line;
        getline(cin,line);
        s[i]=line;
    }
    int C;
    cin>>C;
    char *c=new char[C];
    for(int i=0;i<C;i++){
        cin>>c[i];
    }
    for(int i=0;i<C;i++){
        switch (c[i]){
            case '-':
                sort(s,s+N, defaultCompare);
                for(int i=0;i<N;i++){
                    cout<<s[i]<<endl;
                }
                break;
            case 'r':
                sort(s,s+N, rCompare);
                for(int i=0;i<N;i++){
                    cout<<s[i]<<endl;
                }
                break;
            case 'n':
                sort(s,s+N, nCompare);
                for(int i=0;i<N;i++){
                    cout<<s[i]<<endl;
                }
                break;
            case 'i':
                sort(s,s+N, iCompare);
                for(int i=0;i<N;i++){
                    cout<<s[i]<<endl;
                }
                break;
            case 'd':
                sort(s,s+N, dCompare);
                for(int i=0;i<N;i++){
                    cout<<s[i]<<endl;
                }
                break;
        }
    }
    return 0;
}


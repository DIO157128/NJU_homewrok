#include <iostream>
using namespace std;
struct Smith{
    int uid;
    int type;
    int cur_t=0;
    int cur_o=0;
    int *done_o=new int[65536];
    int nowdone=0;
};
struct Order{
    int oid;
    int priority;
    int time;
    int type;
    int status=0;
};
struct shop{
    int s_num;
    int l_num;
    int c_num=0;
    int nowptr=0;
    Smith* smiths;
    Order* orders;
};
int getPriorityPtr(int type,shop* ss){
    int mostptr=-1;
    int mostprio=0;
    for(int i =0;i<ss->nowptr;i++){
        if(ss->orders[i].status==0&&ss->orders[i].type==type){
            if(ss->orders[i].priority>mostprio){
                mostptr=i;
                mostprio=ss->orders[i].priority;
            }
        }
    }
    return mostptr;
}
void add(int p,int t,int ty,int o,shop* ss){
    if(ss->c_num<ss->l_num){
        ss->orders[ss->nowptr].priority=p;
        ss->orders[ss->nowptr].time=t;
        ss->orders[ss->nowptr].type=ty;
        ss->orders[ss->nowptr].oid=o;
        ss->nowptr+=1;
        ss->c_num+=1;
    }
    else{
        ss->orders[ss->nowptr].priority=p;
        ss->orders[ss->nowptr].time=t;
        ss->orders[ss->nowptr].type=ty;
        ss->orders[ss->nowptr].oid=o;
        ss->nowptr+=1;
        int leastptr=0;
        int leastprio=p;
        for(int i =0;i<ss->nowptr;i++){
            if(ss->orders[i].status==0){
                if(ss->orders[i].priority<leastprio){
                    leastptr=i;
                    leastprio=ss->orders[i].priority;
                }
            }
        }
        ss->orders[leastptr].status=-1;
    }

}
void queryUser(int uid,shop* ss){
    Smith* tem=ss->smiths;
    for(int i=0;i<ss->s_num;i++){
        if(ss->smiths[i].uid=uid){
            if(ss->smiths[i].cur_t==0){
                cout<<"worker "<<uid<<" resting"<<endl;
                return;
            }
            else{
                cout<<"worker "<<uid<<" doing order "<<tem[i].cur_o<<endl;
                return;
            }
        }
    }
}
void queryOrder(int oid,shop* ss){
    auto* tem=ss->orders;
    for(int i=0;i<ss->l_num;i++){
        if(ss->orders[i].oid=oid){
            if(ss->orders[i].status==0){
                cout<<"order "<<oid<<" pending"<<endl;
                return;
            }
            else if(ss->orders[i].status==-1){
                cout<<"order "<<oid<<" discarded"<<endl;
                return;
            }
            else if(ss->orders[i].status==1){
                cout<<"order "<<oid<<" done"<<endl;
                return;
            }
            else if(ss->orders[i].status==2){
                cout<<"order "<<oid<<" doing"<<endl;
                return;
            }
        }
    }
}
void queryOrders(int uid,shop* ss){
    Smith* tem=ss->smiths;
    for(int i=0;i<ss->s_num;i++){
        if(ss->smiths[i].uid=uid){
            for(int j=0;j<ss->smiths[i].nowdone;j++){
                cout<<ss->smiths[i].done_o[j]<<" ";
            }
            cout<<endl;
            return;
        }
    }
}
int getOidPtr(int oid,shop* ss){
    for(int i=0;i<ss->nowptr;i++){
        if(ss->orders[i].oid==oid){
            return i;
        }
    }
    return 0;
}
void dispatch(shop* ss){
    for(int i=0;i<ss->s_num;i++){
        if(ss->smiths[i].cur_t!=0){
            ss->smiths[i].cur_t-=1;
        }
    }
    for(int i=0;i<ss->s_num;i++){
        if(ss->smiths[i].cur_t==0){
            if(ss->smiths[i].cur_o!=0){
                ss->smiths[i].done_o[ss->smiths[i].nowdone]=ss->smiths[i].cur_o;
                ss->smiths[i].nowdone+=1;
                ss->orders[getOidPtr(ss->smiths[i].cur_o,ss)].status=1;
                ss->smiths[i].cur_o=0;
            }
        }
    }
    for(int i=0;i<ss->s_num;i++){
        if(ss->smiths[i].cur_o==0){
            int ptr=getPriorityPtr(ss->smiths[i].type,ss);
            if(ptr!=-1){
                ss->smiths[i].cur_o=ss->orders[ptr].oid;
                ss->smiths[i].cur_t=ss->orders[ptr].time;
                ss->orders[ptr].status=2;
            }

        }
    }
}
int main(){
    shop *ss=new shop;
    int s_num,l_num;
    cin>>s_num>>l_num;
    ss->s_num=s_num;
    ss->l_num=l_num;
    ss->smiths=new Smith[s_num];
    ss->orders=new Order[65536];
    for(int i=0;i<s_num;i++){
        int uid,type;
        cin>>uid>>type;
        ss->smiths[i].uid=uid;
        ss->smiths[i].type=type;
        ss->smiths[i].cur_o=0;
        ss->smiths[i].cur_t=0;
    }
    int N;
    cin>>N;
    for(int i=0;i<N;i++){
        dispatch(ss);
        string cmd;
        cin>>cmd;
        if(cmd=="add"){
            int oid,priority,time,type;
            cin>>oid>>priority>>time>>type;
            add(priority,time,type,oid,ss);
        }
        else if(cmd=="queryUser"){
            int uid;
            cin>>uid;
            queryUser(uid,ss);
        }
        else if(cmd=="queryOrder"){
            int oid;
            cin>>oid;
            queryOrder(oid,ss);
        }
        else if(cmd=="queryOrders"){
            int uid;
            cin>>uid;
            queryOrders(uid,ss);
        }
    }
    return 0;
}
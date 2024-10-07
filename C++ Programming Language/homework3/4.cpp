#include <iostream>
#include <string>
using namespace std;
struct Node{
    Node* next;//前一个节点
    Node* prev;//后一个节点
    int val;//该节点存放的数字
    Node(int x): val(x),next(nullptr),prev(nullptr){};
};
struct Deque{
    int size;//有效节点数
    Node* front;//虚拟头节点，不作为有效节点
    Node* rear;//虚拟尾节点，不作为有效节点
    Deque():size(0),front(nullptr),rear(nullptr){};
};
void push_front (Deque* self, int value){
    self->size+=1;
    Node* ins=new Node(value);
    ins->prev=self->front;
    ins->next=self->front->next;
    self->front->next->prev=ins;
    self->front->next=ins;
};
void push_back (Deque* self, int value){
    self->size+=1;
    Node* ins=new Node(value);
    ins->prev=self->rear->prev;
    ins->next=self->rear;
    self->rear->prev->next=ins;
    self->rear->prev=ins;
};
void pop_front (Deque* self){
    if(self->size==0){
        cout<<-1;
    } else{
        self->size-=1;
        cout<<self->front->next->val<<endl;
        Node* tem=new Node(0);
        tem=self->front->next->next;
        self->front->next=tem;
        tem->prev=self->front;
    }
};
void pop_back (Deque* self){
    if(self->size==0){
        cout<<-1;
    } else{
        self->size-=1;
        cout<<self->rear->prev->val<<endl;
        Node* tem=new Node(0);
        tem=self->rear->prev->prev;
        self->rear->prev=tem;
        tem->next=self->rear;
    }
};
int main(){
    int N;
    cin>>N;
    auto *dq=new Deque();
    dq->front=new Node(0);
    dq->rear=new Node(0);
    dq->front->next=dq->rear;
    dq->rear->prev=dq->front;
    for (int i=0;i<N;i++){
        string cmd;
        cin>>cmd;
        if(cmd=="pushFront"){
            int val;
            cin>>val;
            push_front(dq,val);
        }
        else if(cmd=="pushBack"){
            int val;
            cin>>val;
            push_back(dq,val);
        }
        else if(cmd=="popFront"){
            pop_front(dq);
        }
        else if(cmd=="popBack"){
            pop_back(dq);
        }
        else if(cmd=="getSize"){
            cout<<dq->size<<endl;
        }
    }

    return 0;
}
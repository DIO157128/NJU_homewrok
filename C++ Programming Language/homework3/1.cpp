#include <iostream>
using namespace std;
struct Node{
    int value;
    Node *next;
    Node(int x): value(x),next(nullptr){};
};
int main() {
    Node *dummy=new Node(NULL);
    Node *pre =dummy;
    int tem;
    while(cin>>tem){
        Node *p =new Node(tem);
        pre->next=p;
        pre=p;
    }
    pre=new Node(NULL);
    Node *cur=dummy->next;
    while(cur!=NULL){
        Node *nexttem=cur->next;
        cur->next=pre;
        pre=cur;
        cur=nexttem;
    }
    while(pre->next!=nullptr){
        cout<<pre->value;
        pre=pre->next;
    }
    return 0;
}

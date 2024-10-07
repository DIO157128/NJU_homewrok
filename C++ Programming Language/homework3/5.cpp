#include <iostream>
#include <cmath>
using namespace std;
struct Node{
    long long int hashcode;
    long long int key;
    string value;
    Node *next= nullptr;
};
struct  LinkList{
    int size=0;
    Node* head= new Node;
};
struct  HashDict{
    int val_size=0;
    int L;
    LinkList* entry;
};
long long int hashcode(long long int key){
    long long int res=3*key*key*key+5*key*key+7*key+11;
    return res>=0?res:-res;
}
void insert(Node* ins,HashDict *hd){
    auto* togo=&hd->entry[ins->hashcode%hd->L];
    togo->size++;
    hd->val_size++;
    Node* toins1=togo->head;
    Node* toins2=togo->head->next;
    while(toins2!= nullptr&&toins2->key<=ins->key){
        toins1=toins1->next;
        toins2=toins2->next;
    }
    toins1->next=ins;
    ins->next=toins2;
}
bool validcheck(HashDict* hd){
    if(hd->val_size>hd->L){
        return false;
    } else{
        for(int i=0;i<hd->L;i++){
            auto*tem=&hd->entry[i];
            if(tem->size>4){
                return false;
            }
        }
    }
    return true;
}
void rehash(HashDict* hd){
    LinkList* origin=hd->entry;
    int oL=hd->L;
    hd->L=2*hd->L+1;
    hd->val_size=0;
    hd->entry=new LinkList[hd->L];
    for(int i=0;i<oL;i++){
        auto*tem=&origin[i];
        Node*temnode=tem->head->next;
        while(temnode!= nullptr){
            Node* ins=new Node;
            ins->key=temnode->key;
            ins->value=temnode->value;
            ins->hashcode= temnode->hashcode;
            insert(ins,hd);
            temnode=temnode->next;
        }
    }
}
int main(){
    int L;
    cin>>L;
    HashDict *hd=new HashDict;
    hd->L=L;
    hd->entry=new LinkList[L];
    int N;
    cin>>N;
    for(int i=0;i<N;i++){
        string cmd;
        cin>>cmd;
        if(cmd=="add"){
            long long int key;
            string value;
            cin>>key>>value;
            Node *ins=new Node;
            ins->key=key;
            ins->value=value;
            ins->hashcode= hashcode(key);
            auto* togo=&hd->entry[ins->hashcode%L];
            insert(ins,hd);
            while(!validcheck(hd)){
                rehash(hd);
            }
        } else if(cmd=="delete"){
            long long int key;
            cin>>key;
            long long int hc= hashcode(key);
            auto *todel=&hd->entry[hc%L];
            todel->size--;
            hd->val_size--;
            Node*todel1=todel->head;
            Node*todel2=todel->head->next;
            while(todel2!= nullptr&&todel2->key!=key){
                todel1=todel1->next;
                todel2=todel2->next;
            }
            todel1->next=todel2->next;
        }else if(cmd=="search"){
            int pos;
            cin>>pos;
            auto*tosea=&hd->entry[pos];
            Node*now=tosea->head->next;
            if(now== nullptr){
                cout<<"null"<<endl;
            } else{
                cout<<now->key<<':'<<now->value;
                while(now->next!= nullptr){
                    cout<<"->";
                    now=now->next;
                    cout<<now->key<<':'<<now->value;
                }
                cout<<endl;
            }
        }

    }
    return 0;
}
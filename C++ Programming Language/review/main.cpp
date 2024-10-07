#include <iostream>
using  namespace std;
//class Person
//{
//public:
//    Person(){
//        cout << "Constructor" << endl;
//    }
//    Person(const Person& p)
//    {
//        cout << "Copy Constructor" << endl;
//    }
//    Person(Person && p){
//        cout << "Move Constructor" << endl;
//    }
//    Person& operator=(const Person& p)
//    {
//        cout << "Copy Assign" << endl;
//        return *this;
//    }
//    Person& operator=(Person&& p)
//    {
//        cout << "Move Assign" << endl;
//        return *this;
//    }
//private:
//    int age;
//    string name;
//};
//
//void f(Person p)
//{
//    return;
//}
//
//Person f1()
//{
//    Person p;
//    return p;
//}
//Person f2(Person p){
//    return p;
//}
//int main()
//{
//    Person p=f1();
//    cout<<"-------"<<endl;
//    Person p1;
//    p1=f1();
//    cout<<"-------"<<endl;
//    Person p2(f1());
//    return 0;
//}
//class A
//{ int i, j;
//public:
//    A() { i=j=0; }
//};
//class B
//{ A *p;
//public:
//    B() {
//        p = new A;
//        cout<<"Constuctor B:"<<this<<" p:"<<p<<endl;}
//    ~B() {
//        cout<<"delete B"<<this<<" p"<<p<<endl;
//        delete p;
//    }
//    B(const B& b){
//        p=b.p;
//        cout<<"Copy Constructor B:"<<this<<" p:"<<p<<endl;
//    }
//    B(B&& b){
//        p=b.p;
//        cout<<"Move Constructor B:"<<this<<" p:"<<p<<endl;
//    }
//};
//void f(B x){}
//int main()
//{   B b;
//    f(b);
//}
//class A {
//public:
//    A(int i)
//    { a=i;
//        cout<<"constructor called."<<a<<"address:"<<this<<endl;}
//    A() {
//        a=0;
//        cout<<"Default constructor called."<<a<<"address:"<<this<<endl;
//    }
//    ~A() { cout<<"Destructor called."<<a<<"address:"<<this<<endl; }
//
//    void Print() { cout<<a<<endl; }
//private:
//    int a;
//};
//void func( long a, long b) {
//    cout << "long";
//}
//void func(double a, double b) {
//    cout << "double";
//}
//class Base {
//public:
//    Base() {
//        cout<<"Base cons"<<endl;
//    }
//    ~Base() {
//        cout<<"Base des"<<endl;
//    }
//};
//class Deroved:public Base {
//public:
//    Deroved() {
//        cout<<"Derived cons"<<endl;
//    }
//    ~Deroved() {
//        cout<<"Derived des"<<endl;
//    }
//};
//
//int main(int argc, const char * argv[]) {
//    Base *p1 = new Deroved();
//    delete p1;
//    Deroved* p2 = new Deroved();
//    delete p2;
//    return 0;
//}
//void func(int *a,int *b){
//    *a=*a+*b;
//    *b=*a-*b;
//    *a=*a-*b;
//}
//void foo(int&a,int &b){
//    a=a+b;
//    b=a-b;
//    a=a-b;
//}
//int main(){
//    int a=5,b=3;
//    int*p1,*p2;
//    p1=&a;
//    p2=&b;
//    func(p1,p2);
//    foo(*p1,*p2);
//    func(&a,&b);
//    cout<<a<<b;
//}
//class Person{
//public:
//    void play(){cout<<"play"<<endl;}
//    void work(){cout<<"work"<<endl;}
//    void sleep(){cout<<"sleep"<<endl;}
//    void one_day()const{
//        play();
//
//    }
//};
class str{
public:
    str(string some){
        s=some;
    }
//    char& operator[](int i){
//        return s[i];
//    }
    const char& operator[](int i) const{
        return s[i];
    }
private:
    string s;
};
void print(const str& s){
    cout<<s[0];
}
int main(){
    str s("hello");
    print(s);
}


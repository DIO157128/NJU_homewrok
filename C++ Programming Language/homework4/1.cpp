//#include <iostream>
//
//class MyContainer {
//public:
//    MyContainer(int size) : _size(size) {
//        _count++;
//        this->_data=new int[size];
//    }
//
//    ~MyContainer() {
//        _count--;
//        delete _data;
//    }
//
//    MyContainer(const MyContainer &Other) {
//        _count++;
//        this->_size=Other._size;
//        this->_data=new int[Other._size];
//        for(int i=0;i<Other._size;i++){
//            _data[i]=Other._data[i];
//        }
//    }
//
//    MyContainer &operator=(const MyContainer &Other) {
//        for(int i=0;i<Other._size;i++){
//            _data[i]=Other._data[i];
//        }
//        return *this;
//    }
//
//    int size() const {
//        return _size;
//    }
//
//    int* data() const {
//        return _data;
//    }
//
//    static int count() {
//        return _count;
//    }
//
//    static int _count;
//
//private:
//    // C++11 引入的 initializer_list
//    int *_data{nullptr};
//    int _size{0};
//};
//
//int MyContainer::_count = 0;
//
//void test1(){
//    MyContainer m(5);
//    std::cout << m.count() << std::endl;
//
//    MyContainer m2(m);
//    std::cout << m2.count() << std::endl;
//
//    MyContainer m3 = m2;
//    std::cout << m3.count() << std::endl;
//}
//
//void test2(){
//    MyContainer m1(5);
//    std::cout << m1.count() << std::endl;
//
//    MyContainer m2 = m1;
//    std::cout << m2.count() << std::endl;
//    std::cout << (m2.data() == m1.data()) << std::endl;
//}
//
//void test3(){
//    MyContainer m1(3);
//    std::cout << m1.count() << std::endl;
//
//    MyContainer m2 = m1;
//    std::cout << m2.count() << std::endl;
//    std::cout << (m2.data() == m1.data()) << std::endl;
//
//    m1 = m2;
//    std::cout << m1.count() << std::endl;
//    std::cout << (m2.data() == m1.data()) << std::endl;
//
//    m2 = m1;
//    std::cout << m2.count() << std::endl;
//    std::cout << (m2.data() == m1.data()) << std::endl;
//
//    int * prev_ptr = m1.data();
//    m1 = m1;
//    std::cout << m1.count() << std::endl;
//    std::cout << (m1.data() == prev_ptr) << std::endl;
//
//}
//
//void test4(){
//    MyContainer m1(3);
//    std::cout << m1.count() << std::endl;
//
//    {
//        MyContainer m2 = m1;
//        std::cout << m2.count() << std::endl;
//        std::cout << (m2.data() == m1.data()) << std::endl;
//
//        m1 = m2;
//        std::cout << m1.count() << std::endl;
//        std::cout << (m2.data() == m1.data()) << std::endl;
//
//        m2 = m1;
//        std::cout << m2.count() << std::endl;
//        std::cout << (m2.data() == m1.data()) << std::endl;
//
//    }
//
//    std::cout << m1.count() << std::endl;
//}
//
//
////int main(){
////    test1();
////    test2();
////    test3();
////    test4();
////    return 0;
////}
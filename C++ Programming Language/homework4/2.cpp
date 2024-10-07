#include <iostream>
#include <string>

class Content {
public:
    explicit Content(int id) : id(id) {
        std::cout << "create " << std::to_string(id) << std::endl;
    }

    ~Content() {
        std::cout << "destroy " << std::to_string(id) << std::endl;
    }
    int ref_count{0};
private:
    int id{-1};
    char data[1024]{};
};

class SharedContainer {
public:
    //TODO
    explicit SharedContainer(int mem_id){
        _data=new Content(mem_id);
        _data->ref_count++;
    };
    //TODO
    ~SharedContainer(){
        _data->ref_count--;
        if(_data->ref_count==0){
            delete _data;
        }
    };
    //TODO
    SharedContainer(const SharedContainer &other){
        other._data->ref_count++;
        _data=other._data;
    };
    //TODO
    SharedContainer& operator=(const SharedContainer &other){
        other._data->ref_count++;
        if(_data->ref_count!=0){
            _data->ref_count--;
            if(_data->ref_count==0){
                delete _data;
            }
        }
        _data=other._data;
    };
    //TODO
    int get_count() const{
        return _data->ref_count;
    };
    bool unused{true};
    SharedContainer(const SharedContainer &&) = delete;
    SharedContainer &operator=(const SharedContainer &&) = delete;

private:
    Content *_data{nullptr};
    //TODO: design your own reference counting mechanism
};

void test1(){
    SharedContainer m1(1);
    SharedContainer m2 = m1;
    SharedContainer m3(m2);
    std::cout << m1.get_count() << std::endl;
    std::cout << m2.get_count() << std::endl;
    std::cout << m3.get_count() << std::endl;
}

void test2(){
    SharedContainer m1(1);
    SharedContainer m2 = m1;
    m1 = m1;
    {
        SharedContainer m3 = m1;
        std::cout << m1.get_count() << std::endl;
    }
    std::cout << m1.get_count() << std::endl;
    std::cout << m2.get_count() << std::endl;
}

void test3(){
    SharedContainer m1(1);
    SharedContainer m2(2);
    m1 = m2;
    std::cout << m1.get_count() << std::endl;
    std::cout << m2.get_count() << std::endl;
    {
        SharedContainer m3(3);
        m1 = m3;
        std::cout << m1.get_count() << std::endl;
        std::cout << m2.get_count() << std::endl;
        std::cout << m3.get_count() << std::endl;
    }
    std::cout << m1.get_count() << std::endl;
    std::cout << m2.get_count() << std::endl;

}

int main(){
    test1();
    test2();
    test3();
    return 0;

}
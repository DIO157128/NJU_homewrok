#include <iostream>
using namespace std;
class Plant{
public:
    int type;
    int atk;
    int hp;
    int x,y;
    bool valid=true;
    bool potatoactivated= false;
    Plant(int t,int a,int h,int x,int y):type(t),atk(a),hp(h),x(x),y(y){
    }
    Plant(){}
};
class Zombie{
public:
    int hp;
    int atk;
    int speed;
    int x,y;
    bool valid= true;
    Zombie(int h,int a,int s,int x):hp(h),atk(a),speed(s),x(x){
        y=9;
    }
    Zombie(){}
};
class Board{
public:
    int round=1;
    Plant* garden;
    Zombie* zone;
    int plantptr=0;
    int zombieptr=0;
    int plantcount=0;
    int zombiecount=0;
    bool plantwin= false;
    bool zombiewin= false;
    Board(){
        garden=new Plant[45];
        zone=new Zombie[100];

    }
    void addPlant(Plant p){
        garden[plantptr++]=p;
        plantcount++;
    }
    void addZombie(Zombie z){
        zone[zombieptr++]=z;
        zombiecount++;
    }
    void plantAttack(){
        for(int i=0;i<plantptr;i++){
            if(garden[i].valid){
                int line=garden[i].x;
                int atk=garden[i].atk;
                int type=garden[i].type;
                if(type==0){
                    int close=9;
                    for(int j=0;j<zombieptr;j++){
                        if(zone[j].valid&&zone[j].x==line&&zone[j].y<=close){
                            close=zone[j].y;
                            if(close==garden[i].y){
                                break;
                            }
                        }
                    }
                    for(int j=0;j<zombieptr;j++){
                        if(zone[j].valid&&zone[j].x==line&&zone[j].y==close){
                            zone[j].hp-=atk;
                            if(zone[j].hp<=0){
                                zone[j].valid= false;
                                zombiecount--;
                            }
                        }
                    }
                }
                if(type==2){
                    int x=garden[i].x;
                    int y=garden[i].y;
                    if(garden[i].potatoactivated){
                        garden[i].valid= false;
                        plantcount--;
                        for(int j=0;j<zombieptr;j++){
                            if(zone[j].valid){
                                if(zone[j].x<=x+1&&zone[j].x>=x-1&&zone[j].y<=y+1&&zone[j].y>=y-1){
                                    zone[j].hp-=atk;
                                }
                                if(zone[j].hp<=0){
                                    zone[j].valid= false;
                                    zombiecount--;
                                }
                            }
                        }
                    }
                }

            }
        }
        if(zombiecount==0){
            plantwin= true;
        }
    }
    void zombieMove(){
        for(int i=0;i<zombieptr;i++){
            if(zone[i].valid){
                zone[i]= move(zone[i]);
                if(zone[i].y<0){
                    zombiewin= true;
                    return;
                }
            }
        }
    }
    Zombie move(Zombie z){
        for(int i=0;i<z.speed;i++){
            for(int j=0;j<plantptr;j++){
                if(garden[j].type==2&&garden[j].valid){
                    garden[j].potatoactivated= true;
                } else{
                    if(garden[j].x==z.x&&garden[j].y==z.y&&garden[j].valid){
                        return z;
                    }
                }

            }
            z.y--;
        }
        return z;
    }
    void zombieAttack(){
        for(int i=0;i<zombieptr;i++){
            if(zone[i].valid){
                int x=zone[i].x;
                int y=zone[i].y;
                for(int j=0;j<plantptr;j++){
                    if(garden[j].x==x&&garden[j].y==y&&garden[j].valid&&garden[j].type!=2){
                        garden[j].hp-=zone[i].atk;
                        if(garden[j].hp<=0){
                            garden[j].valid= false;
                            plantcount--;
                        }
                    }
                }
            }

        }
    }
    void play(){
        while(true){
            plantAttack();
            zombieMove();
            zombieAttack();
            cout<<round<<" "<<plantcount<<" "<<zombiecount<<endl;
            if(plantwin){
                cout<<"plants win";
                return;
            }
            if(zombiewin){
                cout<<"zombies win";
                return;
            }
            round++;
        }
    }
};
int main() {
    int num_p,num_z;
    cin>>num_p>>num_z;
    Board b;
    for(int i=0;i<num_p;i++){
        string type;
        cin>>type;
        if(type=="pea"){
            int hp,atk,x,y;
            cin>>hp>>atk>>x>>y;
            Plant pea= Plant(0,atk,hp,x,y);
            b.addPlant(pea);
        }
        if(type=="nut"){
            int hp,x,y;
            cin>>hp>>x>>y;
            Plant nut= Plant(1,0,hp,x,y);
            b.addPlant(nut);
        }
        if(type=="potato"){
            int atk,x,y;
            cin>>atk>>x>>y;
            Plant potato= Plant(2,atk,999,x,y);
            b.addPlant(potato);
        }
    }
    for(int i=0;i<num_z;i++){
        int hp,atk,speed,x;
        cin>>hp>>atk>>speed>>x;
        Zombie z=Zombie(hp,atk,speed,x);
        b.addZombie(z);
    }
    b.play();
    return 0;
}

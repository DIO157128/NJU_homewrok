////
//// Created by dell on 2022/5/13.
////
//#include <iostream>
//class chess{
//public:
//    int x;
//    int y;
//    chess(int x,int y):x(x),y(y){}
//};
//class chessBoard{
//public:
//    int size;
//    int rule;
//    int** board;
//    bool Owins;
//    bool Xwins;
//    int count;
//    chessBoard(int size,int rule):size(size),rule(rule){
//        board=new int*[size];
//        count=0;
//        Owins= false;
//        Xwins= false;
//        for(int i=0;i<size;i++){
//            board[i]=new int[size];
//        }
//        for(int i=0;i<size;i++){
//            for(int j=0;j<size;j++){
//                board[i][j]=-1;
//            }
//        }
//    }
//    void putchess(chess chess){
//        board[chess.x][chess.y]=count%2;
//        count++;
//    }
//    void check(){
//        for(int i=0;i<=size-rule;i++){
//            for(int j=0;j<=size-rule;j++){
//                int temtype=board[i][j];
//                if(temtype!=-1){
//                    int row= temtype;
//                    int col= temtype;
//                    int sid= temtype;
//                    bool rowflag= true;
//                    bool colflag= true;
//                    bool sidflag= true;
//                    for(int k=1;k<rule;k++){
//                        rowflag&=(row==board[i][j+k]);
//                        row=board[i][j+k];
//                        colflag&=(col==board[i+k][j]);
//                        col=board[i+k][j];
//                        sidflag&=(sid==board[i+k][j+k]);
//                        sid=board[i+k][j+k];
//                    }
//                    if(rowflag||colflag||sidflag){
//                        temtype==0?Owins=true:Xwins= true;
//                        return;
//                    }
//                }
//            }
//        }
//    }
//};
//int main(){
//    int size,rule;
//    std::cin>>size>>rule;
//    chessBoard cb=chessBoard(size,rule);
//    while(true){
//        int x,y;
//        std::cin>>x>>y;
//        chess tem= chess(x,y);
//        cb.putchess(tem);
//        cb.check();
//        if(!cb.Owins&&!cb.Xwins&&cb.count!=size*size){
//            continue;
//        } else if(!cb.Owins&&!cb.Xwins&&cb.count==size*size){
//            std::cout<<"Dogfall";
//            break;
//        } else if(cb.Xwins){
//            std::cout<<"X Success";
//            break;
//        }
//        else if(cb.Owins){
//            std::cout<<"O Success";
//            break;
//        }
//    }
//    return 0;
//}

//#include <cstdio>
//#include <iostream>
//#include <string>
//
//using namespace std;
//
//enum Chess
//{
//    X,
//    O,
//    N
//};
//
//class Board
//{
//public:
//    //上一个chess
//    Chess last = Chess::N;
//    int count = 0;
//
//    Board(int n, int m) : n(n), m(m)
//    {
//        board = new Chess[n * n];
//        for (int i = 0; i < n * n; i++)
//        {
//            board[i] = Chess::N;
//        }
//    }
//
//    Chess &getChess(int x, int y)
//    {
//        return board[x * n + y];
//    }
//
//    void setChess(int x, int y, Chess last)
//    {
//        board[x * n + y] = last;
//    }
//
//    string judge()
//    {
//        //判断竖列
//        for (int i = 0; i <= n - m; i++)
//        {
//            for (int j = 0; j < n; j++)
//            {
//                Chess &chess = getChess(i, j);
//                if (chess == Chess::N)
//                    continue;
//                bool isSame = true;
//                for (int k = i + 1; k < i + m; k++)
//                {
//                    if (getChess(k, j) != chess)
//                    {
//                        isSame = false;
//                        break;
//                    }
//                }
//
//                if (isSame)
//                {
//                    return chess == Chess::X ? "X Success" : "O Success";
//                }
//            }
//        }
//
//        //判断横行
//        for (int i = 0; i <= n; i++)
//        {
//            for (int j = 0; j <= n - m; j++)
//            {
//                Chess &chess = getChess(i, j);
//                if (chess == Chess::N)
//                    continue;
//                bool isSame = true;
//                for (int k = j + 1; k < j + m; k++)
//                {
//                    if (getChess(i, k) != chess)
//                    {
//                        isSame = false;
//                        break;
//                    }
//                }
//
//                if (isSame)
//                {
//                    return chess == Chess::X ? "X Success" : "O Success";
//                }
//            }
//        }
//
//        //左对角线
//        for (int i = 0; i <= n - m; i++)
//        {
//            for (int j = 0; j <= n - m; j++)
//            {
//                Chess &chess = getChess(i, j);
//                if (chess == Chess::N)
//                    continue;
//                bool isSame = true;
//                for (int k = 1; k < m; k++)
//                {
//                    if (getChess(k + i, j + k) != chess)
//                    {
//                        isSame = false;
//                        break;
//                    }
//                }
//
//                if (isSame)
//                {
//                    return chess == Chess::X ? "X Success" : "O Success";
//                }
//            }
//        }
//
//        //判断右对角线
//        for (int i = 0; i <= n - m; i++)
//        {
//            for (int j = 0; j <= n - m; j++)
//            {
//                Chess &chess = getChess(n - i, j);
//                if (chess == Chess::N)
//                    continue;
//                bool isSame = true;
//                for (int k = 1; k < m; k++)
//                {
//                    if (getChess(n - k - i, j + k) != chess)
//                    {
//                        isSame = false;
//                        break;
//                    }
//                }
//
//                if (isSame)
//                {
//                    return chess == Chess::X ? "X Success" : "O Success";
//                }
//            }
//        }
//
//        if (count == n * n)
//        {
//            return "Dogfall";
//        }
//        else
//        {
//            return "can_continue";
//        }
//    }
//    string step()
//    {
//        count++;
//        int x, y;
//        cin >> x >> y;
//        if (last == Chess::X || last == Chess::N)
//        {
//            last = Chess::O;
//        }
//        else
//        {
//            last = Chess::X;
//        }
//
//        setChess(x, y, last);
//        return judge();
//    }
//
//private:
//    Chess *board;
//    int n = 0, m = 0;
//};
//
//int main()
//{
//    int n, m;
//    cin >> n >> m;
//    Board *board = new Board(n, m);
//
//    string state = "can_continue";
//
//    while (state == "can_continue")
//    {
//        state = board->step();
//    }
//
//    cout << state;
//
//    return 0;
//}
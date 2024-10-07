//此oj需要手动引入工具类，直接用会报“编译或运行失败”，如需要用ArrayList，需要手动引入，
//如：import java.util.ArrayList;

//只测试了暴力求解和公式法，都是可以通过的，测试用例比较少，不用担心；
//常见的问题都给了注释，还遇到bug找群里的小伙伴交流一下。

import java.util.ArrayList;
import java.util.List;

public class Josephus {
    public static int lastRemaining(int n, int m) {//不要更改这里的static修饰符
        List<Integer> l=new ArrayList<Integer>();
        for(int i=0;i<=n-1;i++){
            l.add(i);
        }
        int a=1;
        do{
            for(int i=0;i<l.size();i++){
                if(a==m){
                    l.remove(i);
                    i--;
                    a=0;
                }
                a++;
            }
        }while(l.size()!=1);
        return l.get(0);//这里return自己的结果
    }
}


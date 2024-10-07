import java.util.HashSet;

public class TraceBack {

    public HashSet<HashSet<Integer>> traceBack(int n, int k){
//        please enter your code here...
        HashSet<HashSet<Integer>> res = new HashSet<HashSet<Integer>>();
        HashSet<Integer> temp = new HashSet<Integer>();
        dofun(n,k,temp,res);

        return res;
    }

    public void dofun(int n,int r,HashSet<Integer> temp,HashSet<HashSet<Integer>> res){
        if(n<r) return;
        if(r == 0){
            HashSet<Integer> copy = new HashSet<Integer>(temp);
            res.add(copy);
            return;
        }
        else{
            temp.add(n);
            dofun(n-1,r-1, temp,res);
            temp.remove(n);
            dofun(n-1,r,temp,res);
        }

    }
}

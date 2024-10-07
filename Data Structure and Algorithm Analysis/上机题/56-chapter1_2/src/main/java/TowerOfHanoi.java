public class TowerOfHanoi {
    public void Hanoi(int n) {
        dosteps(n,'A','C','B');// please enter your code here...
    }
    public static void dosteps(int n,char from,char inter,char to){
        if(n==1){
            System.out.println("Move disk 1 from "+from+" to "+to);
            return;
        }
        else {
            dosteps(n-1,from,to,inter);
            System.out.println("Move disk "+n+" from "+from+" to "+to);
            dosteps(n-1,inter,from,to);
        }
    }
}

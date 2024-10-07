package edu.nju;


import java.util.*;

/**
 * @Author: pkun
 * @CreateTime: 2021-05-25 23:53
 */
public class Executor {

    ArrayList<Tape> tapes=new ArrayList<>();
    TuringMachine tm;
    State q;
    int steps = 0;
    String headinfo;
    boolean canRun;
    public Executor(TuringMachine tm, ArrayList<Tape> tapes) {
        this.tm = tm;
        q = tm.getInitState();
        loadTape(tapes);
        headinfo=snapShotTape();
        canRun = !(findtf(q, headinfo) == null);
        if(!containstateall(tm.F,tm.Q)){
            System.err.println("Error: 3");
            canRun=false;
        }
        if(tm.S.contains('_')){
            System.err.println("Error: 4");
            canRun=false;
        }
        if(!tm.G.contains('_')){
            System.err.println("Error: 5");
            canRun=false;
        }
        if(!tm.G.containsAll(tm.S)){
            System.err.println("Error: 6");
            canRun=false;
        }
        for(TransitionFunction tf :tm.Delta){
            if((!containstate(tm.Q,tf.getSourceState()))||(!containstate(tm.Q,tf.getDestinationState()))){
                System.err.println("Error: 7");
                canRun=false;
            }
            boolean f1=false;
            boolean f2=false;
            char []a1=tf.getInput().toCharArray();
            char []a2=tf.getOutput().toCharArray();
            for(char c: a1){
                if(!containchar(tm.G,c)){
                    f1=true;
                    break;
                }
            }
            for(char c: a2){
                if(!containchar(tm.G,c)){
                    f2=true;
                    break;
                }
            }
            if(f1){
                System.err.println("Error: 8");
                canRun=false;
            }
            if(f2){
                System.err.println("Error: 8");
                canRun=false;
            }
        }
        Set<TransitionFunction>tested=new HashSet<>();
        for(TransitionFunction tf:tm.Delta){
            tested.add(tf);
            for(TransitionFunction tem:tm.Delta) {
                if (!tested.contains(tem)) {
                    if (tf.getSourceState().getQ().equals(tem.getSourceState().getQ()) && tf.getInput().equals(tem.getInput())) {
                        if (tf.getOutput().equals(tem.getInput()) ||
                                tf.getDirection().equals(tem.getDirection()) ||
                                tf.getDestinationState().getQ().equals(tem.getDestinationState().getQ())) {
                            canRun = false;
                            System.err.println("Error: 9");
                            tested.add(tem);
                        }
                    }
                }
            }
        }
        if(containstate(tm.F,this.q)){
            canRun=false;
        }
    }
    public Boolean execute() {
        if(canRun){
            steps++;
            headinfo=snapShotTape();                                              //获取head信息
            TransitionFunction suitable=findtf(q,headinfo);
            char []dirinfo=suitable.getDirection().toCharArray();
            q=suitable.getDestinationState();
            char []res=suitable.getOutput().toCharArray();                        //存储transitionfunction的output
            int countinput=0;                                                      //用于给res这个数组计数
            int counttape=0;                                                        //用于给tapes这个arraylist计数
            for(Tape t:tapes){
                for(StringBuilder sb:t.tracks){
                    sb.replace(t.head,t.head+1, String.valueOf(res[countinput++]));
                }
                if(moveHeads(dirinfo[counttape],t.head)>=t.tracks.get(0).length()){      //movehead就是简单的head自增自减
                    for(StringBuilder sb:t.tracks){
                        sb.append('_');
                    }
                    t.head++;
                }
                else if(moveHeads(dirinfo[counttape],t.head)<0){
                    for(StringBuilder sb:t.tracks){
                        sb.insert(0,"_");
                    }
                    t.head=0;
                }
                else{
                    t.head=moveHeads(dirinfo[counttape],t.head);
                }
                t.updatestart();                                                        //更新磁带的有效长度
                t.updateend();
                counttape++;
            }
            canRun=!(tm.F.contains(q))&&!(findtf(q,snapShotTape())==null);
            return canRun;
        }
        else{
            return canRun;
        }
    }
    public void loadTape(ArrayList<Tape> tapes){
        Set<Character> tem1 = new HashSet(tm.S);
        tem1.add('_');
        if(tapes.size()!=tm.tapeNum){
            canRun=false;
            System.err.println("Error: 2");;
        }
        this.tapes.addAll(tapes);
        l1:     for(Tape t :this.tapes){
            for(StringBuilder s :t.tracks){
                char []tem=s.toString().toCharArray();
                for(char c: tem){
                    if(!tem1.contains(c)){
                        System.err.println("Error: 1");
                        canRun=false;
                        break l1;
                    }
                }
            }
        }
    }
    private String snapShotTape() {
        StringBuilder tem =new StringBuilder();
        for(Tape t:this.tapes){
            for(StringBuilder sb :t.tracks){
                tem.append(sb.toString().charAt(t.head));
            }
        }
        return tem.toString();

    }
    public String snapShot() {
        StringBuilder res =new StringBuilder();
        int maxtracknum=0;
        for(Tape t:tapes){
            if(t.tracks.size()>=maxtracknum){
                maxtracknum=t.tracks.size();
            }
        }
        int spacenum=Math.max(Integer.toString(maxtracknum).length()+5,Integer.toString(tm.tapeNum).length()+5);
        res.append("Step");
        res.append(printspace(spacenum,4));
        res.append(" ").append(steps).append(System.lineSeparator());
        for(int i=0;i< tapes.size();i++){                                                                       //遍历tapes列表
            res.append("Tape");
            res.append(i);
            res.append(printspace(spacenum,4+Integer.toString(i).length()));
            res.append(System.lineSeparator());
            res.append("Index");
            res.append(i);
            res.append(printspace(spacenum,5+Integer.toString(i).length()));
            for(int j=tapes.get(i).start;j<=tapes.get(i).end;j++){
                res.append(" ").append(j);
            }
            res.append(System.lineSeparator());
            for(int j=0;j<tapes.get(i).tracks.size();j++){
                int temindex=tapes.get(i).start;                                                     //遍历tapes列表的tape元素有的tracks列表
                res.append("Track").append(j);
                res.append(printspace(spacenum,5+Integer.toString(j).length()));
                char []in= tapes.get(i).tracks.get(j).substring(tapes.get(i).start,tapes.get(i).end+1).toCharArray();
                for(char c:in){
                    res.append(" ").append(c);
                    for(int k=0;k<Integer.toString(temindex).length()-1;k++){
                        res.append(" ");
                    }
                    temindex++;
                }
                if(res.charAt(res.length()-1)==' '){
                    res.delete(res.length()-1,res.length());
                }
                res.append(System.lineSeparator());
            }
            res.append("Head").append(i);
            res.append(printspace(spacenum,4+Integer.toString(i).length()));
            res.append(" ").append(tapes.get(i).head).append(System.lineSeparator());
        }
        res.append("State").append(printspace(spacenum,5));
        res.append(" ").append(q.getQ()).append(System.lineSeparator());
        return res.toString();
    }

    private int moveHeads(char direction,int head) {
        switch (direction){
            case 'l':
                head--;
                break;
            case 'r':
                head++;
                break;
            case '*':
                break;

        }
        return head;
    }
    private TransitionFunction findtf(State q, String input){
        for(TransitionFunction res:tm.Delta){
            if(res.getSourceState().getQ().equals(q.getQ())&&res.getInput().equals(input)){
                return res;
            }
        }
        return null;
    }
    private String printspace(int spacenum, int l){
        StringBuilder res=new StringBuilder();
        for(int i =0;i<spacenum-l;i++){
            res.append(" ");
        }
        return res.append(":").toString();
    }
    boolean containstate(Set<State> s, State q){
        for(State a:s){
            if(a.getQ().equals(q.getQ())){
                return true;
            }
        }
        return false;
    }
    boolean containstateall(Set<State> small, Set<State> big){
        int judnum=0;
        for(State sm:small){
            if(containstate(big,sm)){
                judnum++;
            }
        }
        return judnum==small.size();
    }
    boolean containchar(Set<Character> s, char q){
        for(char a:s){
            if(a==q){
                return true;
            }
        }
        return false;
    }
    boolean containcharall(Set<Character> small, Set<Character> big){
        int judnum=0;
        for(char sm:small){
            if(containchar(big,sm)){
                judnum++;
            }
        }
        return judnum==small.size();
    }
}



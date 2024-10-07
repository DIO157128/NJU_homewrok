package edu.nju;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: pkun
 * @CreateTime: 2021-05-23 16:15
 */
public class TuringMachine {

    // 状态集合
    Set<State> Q;
    // 输入符号集
    Set<Character> S;
    // 磁带符号集
    Set<Character> G;
    // 初始状态
    State q;
    // 终止状态集
    Set<State> F;
    // 空格符号
    Character B;
    // 磁带数
    Integer tapeNum;
    // 迁移函数集
    Set<TransitionFunction> Delta;

    public TuringMachine(Set<State> Q, Set<Character> S, Set<Character> G, State q, Set<State> F, char B, int tapeNum, Set<TransitionFunction> Delta) {
        this.Q = Q;
        this.S = S;
        this.G = G;
        this.q = q;
        this.F = F;
        this.B = B;
        this.tapeNum = tapeNum;
        this.Delta = Delta;
    }

    //TODO
    public TuringMachine(String tm) {
        int fQ = 0;
        int fS = 0;
        int fG = 0;
        int fq = 0;
        int fF = 0;
        int fB = 0;
        int fT = 0;
        int fD = 0;
        String[] s1 = tm.split(System.lineSeparator());
        int []lines=new int[s1.length];
        int validnum=0;
        ArrayList<String> s = new ArrayList<>();
        this.Q= new HashSet<State>();
        this.S= new HashSet<Character>();
        this.Delta=new HashSet<TransitionFunction>();
        this.tapeNum=0;
        for (int i = 0; i < s1.length; i++) {                                       /* 此处判断每行是否存在注释，同时去除首尾空格*/
            String tem = s1[i];
            int flag = 0;
            int index = 0;
            for (int j = 0; j < tem.length(); j++) {
                if (tem.charAt(j) == ';') {
                    flag = 1;
                    index = j;

                    break;
                }
            }
            if (flag == 0) {
                tem = tem.trim();
                if(!tem.equals("")){
                    s.add(tem);
                    lines[validnum++]=i+1;
                }}
            if (flag == 1) {
                tem=tem.substring(0,index);
                tem=tem.trim();
                if(!tem.equals("")){
                    s.add(tem);
                    lines[validnum++]=i+1;}
            }

        }
        for (int i = 0; i < s.size(); i++) {
            String todeal=s.get(i);
            String typ=todeal.substring(0,3);

            switch (typ){
                case "#Q ":{
                    fQ = 1;
                    checkformat(todeal,i,lines);
                        todeal = todeal.substring(6, todeal.length() - 1);
                        String[] cur = todeal.split(",");
                        for(String x:cur){
                            this.Q.add(new State((x.trim())));
                        }
                    break;
                }
                case "#S ": {
                    fS = 1;
                    checkformat(todeal, i, lines);
                        todeal = todeal.substring(6, todeal.length() - 1);
                        String[] cur = todeal.split(",");
                        for (String c : cur) {
                            this.S.add(c.charAt(0));
                        }

                    break;
                }
                case"#G ": {
                    fG = 1;
                    checkformat(todeal, i, lines);
                        todeal = todeal.substring(6, todeal.length() - 1);
                        String[] cur = todeal.split(",");
                        this.G=new HashSet<Character>();
                        for (String c : cur) {
                            this.G.add(c.charAt(0));
                        }


                    break;
                }
                case"#q0": {
                    fq = 1;
                    checkformat(todeal, i, lines);
                        todeal = todeal.substring(6);
                        this.q = new State(todeal);


                    break;
                }
                case"#B ": {
                    fB = 1;
                    checkformat(todeal, i, lines);
                        todeal = todeal.substring(5);
                        this.B = todeal.charAt(0);


                    break;
                }
                case"#F ":{
                    fF = 1;
                    checkformat(todeal, i, lines);
                        todeal = todeal.substring(6, todeal.length() - 1);
                        String[] cur = todeal.split(",");
                        this.F= new HashSet<State>();
                        for (String c : cur) {
                            this.F.add(new State(c));
                        }


                    break;
                }
                case"#N ": {
                    fT = 1;
                    checkformat(todeal, i, lines);
                        todeal = todeal.substring(5);
                        this.tapeNum = Integer.parseInt(todeal);


                    break;
                }
                case"#D ":{
                    checkformat(todeal, i, lines);
                        fD=1;
                        todeal = todeal.substring(3);
                        TransitionFunction tem=new TransitionFunction(todeal);
                        this.Delta.add(tem);

                    break;
                }
                default:{
                    System.err.println("Error: " + lines[i]);
                }
            }
        }
        if(fQ==0){
            System.err.println("Error: lack Q");
        }
        if(fS==0){
            System.err.println("Error: lack S");
        }
        if(fG==0){
            System.err.println("Error: lack G");
        }
        if(fq==0){
            System.err.println("Error: lack q0");
        }
        if(fB==0){
            System.err.println("Error: lack B");
        }
        if(fF==0){
            System.err.println("Error: lack F");
        }
        if(fT==0){
            System.err.println("Error: lack N");
        }
        if(fD==0){
            System.err.println("Error: lack D");
        }
    }
    public String toString () {
        StringBuilder sb=new StringBuilder();
        sb.append("#Q = {");
        for(State s:Q){
            sb.append(s.getQ()+",");
        }
        sb.setCharAt(sb.length()-1,'}');
        sb.append(System.lineSeparator());
        sb.append("#S = {");
        for(Character s:S){
            sb.append(s+",");
        }
        sb.setCharAt(sb.length()-1,'}');
        sb.append(System.lineSeparator());
        sb.append("#G = {");
        for(Character s:G){
            sb.append(s+",");
        }
        sb.setCharAt(sb.length()-1,'}');
        sb.append(System.lineSeparator());
        sb.append("#N = ");
        sb.append(tapeNum);
        sb.append(System.lineSeparator());
        sb.append("#F = {");
        for(State s:F){
            sb.append(s.getQ()+",");
        }
        sb.setCharAt(sb.length()-1,'}');
        sb.append(System.lineSeparator());
        sb.append("#q0 = ");
        sb.append(q);
        sb.append(System.lineSeparator());
        sb.append("#B = ");
        sb.append(B);
        sb.append(System.lineSeparator());
        for(TransitionFunction d :Delta){
            sb.append("#D ");
            sb.append(d.getSourceState().getQ()+" ");
            sb.append(d.getInput()+" ");
            sb.append(d.getOutput()+" ");
            sb.append(d.getDirection()+" ");
            sb.append(d.getSourceState().getQ());
            sb.append(System.lineSeparator());
        }
        sb.delete(sb.length()-1,sb.length()-1);
        return sb.toString();
    }
    public void checkformat(String s,int i,int []lines){
        if(s.substring(0,5).equals("#Q = ")) {
            if (s.charAt(s.length() - 1) == '}'&&s.charAt(5) == '{') {
                s = s.substring(6, s.length() - 1);
                String[] s1 = s.split(",");
                for (String x : s1) {
                    x=x.trim();
                    if (x.matches("^[a-z0-9A-Z_]+$")) {                /*？？？*/
                    }
                    else{
                        System.err.println("Error: " + lines[i]);
                    }
                }
            }
            else{
                System.err.println("Error: " + lines[i]);
            }
        }
        else if(s.substring(0,5).equals("#S = ")){
            if (s.charAt(s.length() - 1) == '}'&&s.charAt(5) == '{'){
                s=s.substring(6,s.length()-1);
                String []s1=s.split(",");
                for (String x:s1) {
                    x=x.trim();
                    if (hasSpecialChar1(x)||x.equals("")) {
                        System.err.println("Error: " + lines[i]);
                    }
                }
            }
            else{
                System.err.println("Error: " + lines[i]);
            }
        }
        else if(s.substring(0,5).equals("#G = ")) {
            if (s.charAt(s.length() - 1) == '}'&&s.charAt(5) == '{') {
                s = s.substring(6, s.length() - 1);
                String[] s1 = s.split(",");
                for (String x : s1) {
                    x=x.trim();
                    if (hasSpecialChar2(x) || x.equals("")) {
                        System.err.println("Error: " + lines[i]);
                    }
                }
            }
            else{
                System.err.println("Error: " + lines[i]);
            }
        }
        else if(s.substring(0,6).equals("#q0 = ")){
            if(s.substring(6).equals("")){
                System.err.println("Error: " + lines[i]);
            }
        }
        else if(s.substring(0,5).equals("#B = ")){
            if(!s.substring(5).equals("_")){
                System.err.println("Error: " + lines[i]);
            }
        }
        else if(s.substring(0,5).equals("#F = ")) {
            if (s.charAt(s.length() - 1) == '}'&&s.charAt(5) == '{') {
                s = s.substring(6, s.length() - 1);
                String[] s1 = s.split(",");
                for (String x : s1) {
                    x=x.trim();
                    if (!x.matches("^[a-z0-9A-Z_]+$")) {
                        System.err.println("Error: " + lines[i]);
                    }
                }
            }
            else{
                System.err.println("Error: " + lines[i]);
            }
        }
        else if(s.substring(0,5).equals("#N = ")){
            if(!s.substring(5).matches("^[0-9]+$")){
                System.err.println("Error: " + lines[i]);
            }
        }
        else if (s.substring(0,3).equals("#D ")){
            TransitionFunction d=new TransitionFunction(s.substring(3));
            if(d.getInput().length()==d.getOutput().length()) {
                if ((!d.getSourceState().getQ().matches("^[a-z0-9A-Z_]+$")) || d.getSourceState().getQ().equals("")) {
                    System.err.println("Error: " + lines[i]);
                } else if ((!d.getDestinationState().getQ().matches("^[a-z0-9A-Z_]+$")) || d.getDestinationState().getQ().equals("")) {
                    System.err.println("Error: " + lines[i]);
                } else if (hasSpecialChar2(d.getInput()) || d.getInput().equals("")) {
                    System.err.println("Error: " + lines[i]);
                } else if (hasSpecialChar2(d.getOutput()) || d.getOutput().equals("")) {
                    System.err.println("Error: " + lines[i]);
                } else if ((!d.getDirection().matches("^[lr*]+$")) || d.getDirection().equals("")) {
                    System.err.println("Error: " + lines[i]);
                }
            }
            else{
                System.err.println("Error: " + lines[i]);
            }
        }
    }
    public  boolean hasSpecialChar1(String str) {
        String regEx = "[ ,;{}*]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }
    public  boolean hasSpecialChar2(String str) {
        String regEx = "[ ,;{}*]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public State getInitState() {return q;
    }
}
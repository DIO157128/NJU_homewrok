package cpu.nbcdu;

import cpu.alu.ALU;
import util.DataType;
import util.Transformer;

public class NBCDU {

    /**
     * @param src  A 32-bits NBCD String
     * @param dest A 32-bits NBCD String
     * @return dest + src
     */
    DataType add(DataType src, DataType dest) {
        String Ssrc=src.toString().substring(0,4);
        String Sdes=dest.toString().substring(0,4);
        String Msrc=src.toString().substring(4);
        String Mdes= dest.toString().substring(4);
        int validrad=Math.min(validjud(Mdes),validjud(Msrc));
        if(!Sdes.equals(Ssrc)){
            if(Ssrc.equals("1101")){
                Msrc=negdealer(Msrc,validrad);
            }
            if(Sdes.equals("1101")){
                Mdes=negdealer(Mdes,validrad);
            }
        }
        int over=0;
        String []res=new String[8];
        Transformer tf=new Transformer();
        ALU alu=new ALU();
        for(int i=6;i>=validrad;i--){
            DataType temsrc=new DataType(enlarger(Msrc.substring(4*i,4*i+4)));
            DataType temdes=new DataType(enlarger(Mdes.substring(4*i,4*i+4)));
            DataType temovr=new DataType(tf.intToBinary(String.valueOf(over)));
            DataType temfixer=new DataType("0000000000000000000000000000"+"0110");
            DataType temres=alu.add(temsrc,temdes);
            temres=alu.add(temres,temovr);
            if(Integer.parseInt(temres.toString(),2)>=10){
                temres=alu.add(temres,temfixer);
            }
            String tres=temres.toString();
            over=Integer.parseInt(tres,2)>=16?1:0;
            res[i+1]=tres.substring(28);
        }
        for(int i=1;i<validrad+1;i++){
            res[i]="0000";
        }
        if(Sdes.equals(Ssrc)){
            res[0]=Sdes;
            if(over==1&&validrad>=1){
                res[validrad]="0001";
            }
        }
        else{
            if(over==1){
                res[0]="1100";
            }
            else {
                res[0]="1101";
                for(int i=validrad;i<=6;i++){
                    res[i+1]=reverse(res[i+1]);
                }
                res[7]=alu.add(new DataType(enlarger(res[7])),new DataType(tf.intToBinary("1"))).toString().substring(28);
            }
        }
        over=0;
        for(int i=6;i>=validrad;i--){
            String tem=res[i+1];
            DataType temdat=new DataType(enlarger(tem));
            DataType temovr=new DataType(tf.intToBinary(String.valueOf(over)));
            DataType temres=new ALU().add(temdat,temovr);
            if(Integer.parseInt(temres.toString(),2)>=10){
                DataType temfixer=new DataType("0000000000000000000000000000"+"0110");
                temres=new ALU().add(temres,temfixer);
            }
            over=Integer.parseInt(temres.toString(),2)>=16?1:0;
            res[i+1]=temres.toString().substring(28);
        }
        over=0;
        for(int i=6;i>=validrad;i--){
            String tem=res[i+1];
            DataType temdat=new DataType(enlarger(tem));
            DataType temovr=new DataType(tf.intToBinary(String.valueOf(over)));
            DataType temres=new ALU().add(temdat,temovr);
            if(Integer.parseInt(tem,2)>=10){
                DataType temfixer=new DataType("0000000000000000000000000000"+"0110");
                temres=new ALU().add(temres,temfixer);
            }
            over=Integer.parseInt(temres.toString(),2)>=16?1:0;
            res[i+1]=temres.toString().substring(28);
        }
        over=0;
        for(int i=6;i>=validrad;i--){
            String tem=res[i+1];
            DataType temdat=new DataType(enlarger(tem));
            DataType temovr=new DataType(tf.intToBinary(String.valueOf(over)));
            DataType temres=new ALU().add(temdat,temovr);
            if(Integer.parseInt(tem,2)>=10){
                DataType temfixer=new DataType("0000000000000000000000000000"+"0110");
                temres=new ALU().add(temres,temfixer);
            }
            over=Integer.parseInt(temres.toString(),2)>=16?1:0;
            res[i+1]=temres.toString().substring(28);
        }
        StringBuilder sb=new StringBuilder();
        for(String s:res){
            sb.append(s);
        }
        return new DataType(sb.toString());
    }

    /***
     *
     * @param src A 32-bits NBCD String
     * @param dest A 32-bits NBCD String
     * @return dest - src
     */
    DataType sub(DataType src, DataType dest) {
        StringBuilder sb=new StringBuilder();
        if(src.toString().substring(0,4).equals("1101")){
            sb.append("1100");
        }
        else sb.append("1101");
        sb.append(src.toString().substring(4));
        DataType minsrc=new DataType(sb.toString());
        return add(minsrc,dest);
    }
    String reverse(String a){
        int i=9-Integer.parseInt(a,2);
        return new Transformer().intToBinary(String.valueOf(i)).substring(28);
    }
    String negdealer(String a,int valid){
        StringBuilder sb=new StringBuilder();
        sb.append("0000");
        for(int i=0;i<valid;i++){
            sb.append("0000");
        }
        for(int i=valid;i<7;i++){
            String tem=a.substring(4*i,4*i+4);
            sb.append(reverse(tem));

        }
        String res=sb.toString();
        DataType r=new ALU().add(new DataType(res),new DataType(new Transformer().intToBinary("1")));
        return  r.toString().substring(4);
    }
    private int validjud(String a ){
        int rad=0;
        for(int i=0;i<7;i++){
            if(a.substring(4*i,4*i+4).equals("0000")){
                rad++;
            }
            else break;
        }
        return rad==7?6:rad;
    }
    private String enlarger(String a){
        int len=a.length();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<32-len;i++){
            sb.append(0);
        }
        sb.append(a);
        return sb.toString();
    }
    private boolean checkformat(String []res){
        for(int i=1;i<8;i++){
            if(Integer.parseInt(res[i])>=10){
                return false;
            }
        }
        return true;
    }
    private void formater(int validrad,String []res){
        int over=0;
        Transformer tf=new Transformer();
        for(int i=6;i>=validrad;i--){
            String tem=res[i+1];
            DataType temdat=new DataType(enlarger(tem));
            DataType temovr=new DataType(tf.intToBinary(String.valueOf(over)));
            DataType temres=new ALU().add(temdat,temovr);
            if(Integer.parseInt(tem,2)>=10){
                DataType temfixer=new DataType("0000000000000000000000000000"+"0110");
                temres=new ALU().add(temres,temfixer);
            }
            over=Integer.parseInt(temres.toString(),2)>=16?1:0;
            res[i+1]=temres.toString().substring(28);
        }
    }
}

package cpu.alu;

import util.DataType;
import util.Transformer;

import javax.xml.crypto.Data;

/**
 * Arithmetic Logic Unit
 * ALU封装类
 */
public class ALU {

    DataType remainderReg;

    /**
     * 返回两个二进制整数的和
     * dest + src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public DataType add(DataType src, DataType dest) {
        String srcs=src.toString();
        String dests=dest.toString();
        StringBuilder res=new StringBuilder();
        int tem;
        int cout=0;
        for(int i=31;i>=0;i--){
            int srctem=Character.getNumericValue(srcs.charAt(i));
            int destem=Character.getNumericValue(dests.charAt(i));
            tem=srctem+destem+cout;
            if(tem<2){
                res.append(tem);
                cout=0;
            }
            if(tem>=2){
                res.append(tem-2);
                cout=1;
            }
        }
        return new DataType(res.reverse().toString());
    }


    /**
     * 返回两个二进制整数的差
     * dest - src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public DataType sub(DataType src, DataType dest) {
        int tem0= Integer.parseInt(new Transformer().binaryToInt(src.toString()));
        String srcs=new Transformer().intToBinary(String.valueOf(-tem0));
        String dests=dest.toString();
        StringBuilder res=new StringBuilder();
        int tem;
        int cout=0;
        for(int i=31;i>=0;i--){
            int srctem=Character.getNumericValue(srcs.charAt(i));
            int destem=Character.getNumericValue(dests.charAt(i));
            tem=srctem+destem+cout;
            if(tem<2){
                res.append(tem);
                cout=0;
            }
            if(tem>=2){
                res.append(tem-2);
                cout=1;
            }
        }
        return new DataType(res.reverse().toString());
    }


    /**
     * 返回两个二进制整数的乘积(结果低位截取后32位)
     * dest * src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public DataType mul(DataType src, DataType dest) {
        String srcs=src.toString();
        String dests=dest.toString();
        int tem0= Integer.parseInt(new Transformer().binaryToInt(dest.toString()));
        String mindests=new Transformer().intToBinary(String.valueOf(-tem0));
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<32;i++){
            sb.append(0);
        }
        StringBuilder sb1=new StringBuilder();
        StringBuilder sb2=new StringBuilder();
        StringBuilder sb3=new StringBuilder();
        DataType u0=new DataType(sb.toString());
        DataType ud=new DataType(sb1.append(dests).toString());
        DataType udm=new DataType(sb2.append(mindests).toString());
        DataType us=new DataType(sb3.append(srcs).toString());
        int y0=0;
        for(int i=0;i<32;i++){
            int tem=Character.getNumericValue(us.toString().charAt(i));
            if(y0-tem<0){
                u0=add(u0,udm);
            }
            if(y0-tem>0){
                u0=add(ud,u0);
            }
            us=Artmovus(us,u0);
            u0=Artmovu0(u0);
            y0=tem;
        }
        return us;
    }


    /**
     * 返回两个二进制整数的除法结果
     * 请注意使用不恢复余数除法方式实现
     * dest ÷ src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public DataType div(DataType src, DataType dest) {
        if(Integer.parseInt(new Transformer().binaryToInt(src.toString()))==0)
            throw new ArithmeticException();
        String srcs=src.toString();
        String dests=dest.toString();
        StringBuilder sb0=new StringBuilder();
        StringBuilder sb1=new StringBuilder();
        for(int i=0;i<32;i++){
            sb0.append(0);
            sb1.append(1);
        }
        DataType R=new DataType(dests.charAt(0)=='0'?sb0.toString():sb1.toString());
        DataType Q=new DataType(dests);
        DataType div=new DataType(srcs);
        int Q0;
        int tem0=Integer.parseInt(new Transformer().binaryToInt(srcs));
        DataType divm=new DataType(new Transformer().intToBinary(String.valueOf(-tem0)));
        if(signjud(dest,div)){
            R=add(R,divm);
        }
        else {
            R=add(R,div);
        }
        if(signjud(R,div)){
            Q0=1;
        }
        else {
            Q0=0;
        }
        for(int i=0;i<32;i++){
            if(signjud(R,div)){
                R=lefmov(R,Q);
                Q=lefmov(Q,Q0);
                R=add(R,divm);
            }else {
                R=lefmov(R,Q);
                Q=lefmov(Q,Q0);
                R=add(R,div);
            }
            Q0=signjud(R,div)?1:0;
        }
        Q=lefmov(Q,Q0);
        if(signjud(R,dest)){
            remainderReg=R;
        }
        else {
            if(signjud(src,dest)){
                remainderReg=add(R,div);
            }
            else remainderReg=add(R,divm);
        }
        boolean flag=false;
        if(!signjud(src,dest)){
            Q=add(Q,new DataType(new Transformer().intToBinary("1")));
            flag=true;
        }
        if(dest.toString().charAt(0)=='1'){
            DataType Qe=Q;
            if(flag){
                Qe=sub(new DataType(new Transformer().intToBinary("1")),Q);
            }
            if(signjud(src,dest)){
                Qe=add(Q,new DataType(new Transformer().intToBinary("1")));
            }
            if(modjud(Qe,div,dest)){
                Q=Qe;
                remainderReg=sub(mul(Q,div),dest);
            }
        }
        return Q;
    }
    public DataType Artmovu0(DataType todo){
        String t=todo.toString();
        StringBuilder sb=new StringBuilder();
        sb.append(t.charAt(0)).append(t.substring(0,t.length()-1));
        return new DataType(sb.toString());
    }
    public DataType Artmovus(DataType todo,DataType former){
        String t=todo.toString();
        String f=former.toString();
        StringBuilder sb=new StringBuilder();
        sb.append(f.charAt(f.length()-1)).append(t.substring(0,t.length()-1));
        return new DataType(sb.toString());
    }
    public boolean signjud(DataType d1,DataType d2){
        if(d1.toString().charAt(0)==d2.toString().charAt(0)){
            return true;
        }
        else return false;
    }
    public DataType lefmov(DataType todo,DataType sup){
        StringBuilder sb=new StringBuilder();
        sb.append(todo.toString().substring(1));
        sb.append(sup.toString().charAt(0));
        return new DataType(sb.toString());
    }
    public DataType lefmov(DataType todo,int sup){
        StringBuilder sb=new StringBuilder();
        sb.append(todo.toString().substring(1));
        sb.append(sup);
        return new DataType(sb.toString());
    }
    public boolean modjud(DataType Q,DataType div,DataType dest){
        DataType res=mul(Q,div);
        return dest.toString().equals(res.toString());
    }
}

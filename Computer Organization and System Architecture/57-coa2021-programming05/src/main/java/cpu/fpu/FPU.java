package cpu.fpu;

import cpu.alu.ALU;
import util.BinaryIntegers;
import util.DataType;
import util.IEEE754Float;
import util.Transformer;

import java.nio.charset.StandardCharsets;

/**
 * floating point unit
 * 执行浮点运算的抽象单元
 * 浮点数精度：使用3位保护位进行计算
 */
public class FPU {

    private final String[][] mulCorner = new String[][]{
            {IEEE754Float.P_ZERO, IEEE754Float.N_ZERO, IEEE754Float.N_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.P_ZERO, IEEE754Float.N_ZERO},
            {IEEE754Float.P_ZERO, IEEE754Float.P_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.N_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.P_ZERO, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.P_ZERO, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.N_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.N_ZERO, IEEE754Float.NaN}
    };

    private final String[][] divCorner = new String[][]{
            {IEEE754Float.P_ZERO, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.N_ZERO, IEEE754Float.NaN},
            {IEEE754Float.P_ZERO, IEEE754Float.N_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.P_INF, IEEE754Float.NaN},
    };


    /**
     * compute the float mul of dest * src
     */
    public DataType mul(DataType src, DataType dest) {
        String a = dest.toString();
        String b = src.toString();
        if (a.matches(IEEE754Float.NaN_Regular) || b.matches(IEEE754Float.NaN_Regular))
        {
            return new DataType(IEEE754Float.NaN);
        }
        String conc=cornerCheck(mulCorner,a,b);
        if(conc!=null){
            return new DataType(conc);
        }
        int Ssrc=Integer.parseInt(src.toString().substring(0,1));
        int Sdes=Integer.parseInt(dest.toString().substring(0,1));
        int Esrc=Integer.parseInt(src.toString().substring(1,9),2);
        int Edes=Integer.parseInt(dest.toString().substring(1,9),2);
        char Sres=(char)('0'+Sdes^Ssrc);
        String Msrc=Mdealer(Esrc,b.substring(9,32));
        String Mdes=Mdealer(Edes,a.substring(9,32));
        if(Edes==0){
            Edes++;
        }
        if (Esrc==0){
            Esrc++;
        }
        int Eres=Edes+Esrc-127+1;
        String Mres=muldealer(Msrc,Mdes);
        while (Mres.charAt(0) == '0' && Eres > 0) {
            Mres=leftshift(Mres);
            Eres--;
        }
        while (Mres.contains("1") && Eres < 0) {
            Mres=rightShift(Mres,1);
            Eres++;
        }

        if (Eres>=255) {
            return Sres=='1'?new DataType(IEEE754Float.N_INF):new DataType(IEEE754Float.P_INF);
        } else if (Eres<0) {
            return Sres=='1'?new DataType(IEEE754Float.N_ZERO):new DataType(IEEE754Float.P_ZERO);
        } else if(Eres == 0) {
            Mres=rightShift(Mres,1);
        }
        String E=new Transformer().intToBinary(String.valueOf(Eres)).substring(24);
        String res=round(Sres,E,Mres);
        return new DataType(res);
    }

    /**
     * compute the float mul of dest / src
     */
    public DataType div(DataType src, DataType dest) {
        String a = dest.toString();
        String b = src.toString();
        if (a.matches(IEEE754Float.NaN_Regular) || b.matches(IEEE754Float.NaN_Regular))
        {
            return new DataType(IEEE754Float.NaN);
        }
        String conc=cornerCheck(divCorner,a,b);
        if(conc!=null){
            return new DataType(conc);
        }
        if (src.toString().equals(BinaryIntegers.ZERO)) throw new ArithmeticException();
        int Ssrc=Integer.parseInt(src.toString().substring(0,1));
        int Sdes=Integer.parseInt(dest.toString().substring(0,1));
        int Esrc=Integer.parseInt(src.toString().substring(1,9),2);
        int Edes=Integer.parseInt(dest.toString().substring(1,9),2);
        char Sres=(char)('0'+Sdes^Ssrc);
        String Msrc=Mdealer(Esrc,b.substring(9,32));
        String Mdes=Mdealer(Edes,a.substring(9,32));
        if(Edes==0){
            Edes++;
        }
        if (Esrc==0){
            Esrc++;
        }
        int Eres=Edes-Esrc+127;
        String Mres=divdealer(Msrc,Mdes);
        if (Eres>=255) {
            return Sres=='1'?new DataType(IEEE754Float.N_INF):new DataType(IEEE754Float.P_INF);
        } else if (Eres<0) {
            return Sres=='1'?new DataType(IEEE754Float.N_ZERO):new DataType(IEEE754Float.P_ZERO);
        } else if(Eres == 0) {
            Mres=rightShift(Mres,1);
        }
        if(Mres.charAt(0)=='0'){
            Eres--;
            Mres=leftshift(Mres);
        }
        String E=new Transformer().intToBinary(String.valueOf(Eres)).substring(24);
        String res=round(Sres,E,Mres);
        return new DataType(res);
    }


    private String cornerCheck(String[][] cornerMatrix, String oprA, String oprB) {
        for (String[] matrix : cornerMatrix) {
            if (oprA.equals(matrix[0]) &&
                    oprB.equals(matrix[1])) {
                return matrix[2];
            }
        }
        return null;
    }

    /**
     * right shift a num without considering its sign using its string format
     *
     * @param operand to be moved
     * @param n       moving nums of bits
     * @return after moving
     */
    private String rightShift(String operand, int n) {
        StringBuilder result = new StringBuilder(operand);  //保证位数不变
        boolean sticky = false;
        for (int i = 0; i < n; i++) {
            sticky = sticky || result.toString().endsWith("1");
            result.insert(0, "0");
            result.deleteCharAt(result.length() - 1);
        }
        if (sticky) {
            result.replace(operand.length() - 1, operand.length(), "1");
        }
        return result.substring(0, operand.length());
    }

    /**
     * 对GRS保护位进行舍入
     *
     * @param sign    符号位
     * @param exp     阶码
     * @param sig_grs 带隐藏位和保护位的尾数
     * @return 舍入后的结果
     */
    private String round(char sign, String exp, String sig_grs) {
        int grs = Integer.parseInt(sig_grs.substring(24, 27), 2);
        if ((sig_grs.substring(27).contains("1")) && (grs % 2 == 0)) {
            grs++;
        }
        String sig = sig_grs.substring(0, 24); // 隐藏位+23位
        if (grs > 4) {
            sig = oneAdder(sig);
        } else if (grs == 4 && sig.endsWith("1")) {
            sig = oneAdder(sig);
        }

        if (Integer.parseInt(sig.substring(0, sig.length() - 23), 2) > 1) {
            sig = rightShift(sig, 1);
            exp = oneAdder(exp).substring(1);
        }
        if (exp.equals("11111111")) {
            return IEEE754Float.P_INF;
        }

        return sign + exp + sig.substring(sig.length() - 23);
    }

    /**
     * add one to the operand
     *
     * @param operand the operand
     * @return result after adding, the first position means overflow (not equal to the carray to the next) and the remains means the result
     */
    private String oneAdder(String operand) {
        int len = operand.length();
        StringBuffer temp = new StringBuffer(operand);
        temp = temp.reverse();
        int[] num = new int[len];
        for (int i = 0; i < len; i++) num[i] = temp.charAt(i) - '0';  //先转化为反转后对应的int数组
        int bit = 0x0;
        int carry = 0x1;
        char[] res = new char[len];
        for (int i = 0; i < len; i++) {
            bit = num[i] ^ carry;
            carry = num[i] & carry;
            res[i] = (char) ('0' + bit);  //显示转化为char
        }
        String result = new StringBuffer(new String(res)).reverse().toString();
        return "" + (result.charAt(0) == operand.charAt(0) ? '0' : '1') + result;  //注意有进位不等于溢出，溢出要另外判断
    }
    private String Mdealer(int E,String a){
        StringBuilder sb=new StringBuilder();
        sb.append(E==0?0:1).append(a).append("000");
        return sb.toString();
    }
    private String muldealer(String src,String des){
        String P="0"+"000000000000000000000000000";
        String Y=des;
        String X="0"+src;
        StringBuilder sb=new StringBuilder();
        sb.append(des);
        sb.reverse();
        String jud=sb.toString();
        for(Character c: jud.toCharArray()){
            if(c=='1'){
                P=adder(P,X);
            }
            Y=rightshift(P,Y);
            P=rightshift(P);
        }
        String res=P+Y;
        return res.substring(1);
    }

    private String divdealer(String src,String des){
        int len=src.length();
        StringBuilder sb=new StringBuilder();
        String remainer=des;
        for(int i=0;i<len;i++){
            if(Integer.parseInt(remainer,2)>=Integer.parseInt(src,2)){
                sb.append(1);
                remainer=suber(remainer,src);
            }
            else {
                sb.append(0);

            }
            remainer=leftshift(remainer);
        }
        return sb.toString();
    }
    private String adder(String a,String b){
        ALU alu=new ALU();
        DataType a1=new DataType("0000"+a);
        DataType b1=new DataType("0000"+b);
        DataType res=alu.add(a1,b1);
        return res.toString().substring(4);
    }

    private String suber(String a,String b){
        ALU alu=new ALU();
        DataType a1=new DataType(lendealer(a));
        DataType b1=new DataType(lendealer(b));
        DataType res=alu.sub(b1,a1);
        return res.toString().substring(4);
    }

    private String lendealer(String a){
        int len=a.length();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<32-len;i++){
            sb.append(0);
        }
        return sb.append(a).toString();
    }
    private String rightshift(String a ){
        StringBuilder sb=new StringBuilder();
        sb.append(0);
        sb.append(a.substring(0,a.length()-1));
        return sb.toString();
    }
    private String rightshift(String former ,String latter){
        StringBuilder sb=new StringBuilder();
        sb.append(former.charAt(former.length()-1));
        sb.append(latter.substring(0,latter.length()-1));
        return sb.toString();
    }
    private String leftshift(String a){
        StringBuilder sb=new StringBuilder();
        sb.append(a.substring(1)).append(0);
        return sb.toString();
    }
}

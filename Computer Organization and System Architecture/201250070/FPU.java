package cpu.fpu;

import cpu.alu.ALU;
import util.DataType;
import util.IEEE754Float;
import util.Transformer;

/**
 * floating point unit
 * 执行浮点运算的抽象单元
 * 浮点数精度：使用3位保护位进行计算
 */
public class

FPU {

    private final String[][] addCorner = new String[][]{
            {IEEE754Float.P_ZERO, IEEE754Float.P_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.P_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.P_ZERO, IEEE754Float.N_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.N_ZERO, IEEE754Float.N_ZERO},
            {IEEE754Float.P_INF, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.N_INF, IEEE754Float.N_INF},
            {IEEE754Float.P_INF, IEEE754Float.P_INF, IEEE754Float.P_INF}

    };

    private final String[][] subCorner = new String[][]{
            {IEEE754Float.P_ZERO, IEEE754Float.P_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.P_ZERO, IEEE754Float.N_ZERO},
            {IEEE754Float.P_ZERO, IEEE754Float.N_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.N_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.P_INF, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.N_INF, IEEE754Float.NaN},
    };

    /**
     * compute the float add of (dest + src)
     */
    public DataType add(DataType src, DataType dest) {
        String a = dest.toString();
        String b = src.toString();
        if (a.matches(IEEE754Float.NaN_Regular) || b.matches(IEEE754Float.NaN_Regular))
        {
            return new DataType(IEEE754Float.NaN);
        }
        String conc=cornerCheck(addCorner,a,b);
        if(conc!=null){
            return new DataType(conc);
        }
        int Ssrc=Integer.parseInt(src.toString().substring(0,1));
        int Sdes=Integer.parseInt(dest.toString().substring(0,1));
        int Esrc=Integer.parseInt(src.toString().substring(1,9),2);
        int Edes=Integer.parseInt(dest.toString().substring(1,9),2);
        String Msrc=Mdealer(Esrc,b.substring(9,32));
        String Mdes=Mdealer(Edes,a.substring(9,32));
        if(Edes==0){
            Edes++;
        }
        if (Esrc==0){
            Esrc++;
        }
        if(Edes>=Esrc){
            Msrc=rightShift(Msrc,Edes-Esrc);
        }
        else {
            Mdes=rightShift(Mdes,Esrc-Edes);
        }ALU alu=new ALU();
        Transformer tf=new Transformer();
        if(Ssrc==1){
            Msrc = oneAdder(tf.negation(Msrc)).substring(1);
            Msrc="11111"+Msrc;
        }
        else Msrc="00000"+Msrc;
        if(Sdes==1){
            Mdes = oneAdder(tf.negation(Mdes)).substring(1);
            Mdes="11111"+Mdes;
        }
        else Mdes="00000"+Mdes;
        DataType MSRC=new DataType(Msrc);
        DataType MDES=new DataType(Mdes);
        DataType MRES=alu.add(MSRC,MDES);
        char Sres= Ssrc!=Sdes?MRES.toString().charAt(0):(char) (Sdes+'0');
        int Eres=Math.max(Edes,Esrc);
        if(MRES.toString().equals(tf.intToBinary("0"))){
            return new DataType(Sres==1?IEEE754Float.N_ZERO:IEEE754Float.P_ZERO);
        }
        String OMRES=Otrans(MRES.toString());
        char overflow=OMRES.charAt(4);
        OMRES=OMRES.substring(5);
        /*if(Edes==0&&OMRES.charAt(0)=='1'){
            Eres++;
            OMRES=leftshift(OMRES);
        }*/
        if(overflow=='1'){
            Eres++;
            if(Eres>=255){
                return Sres=='1'?new DataType(IEEE754Float.N_INF):new DataType(IEEE754Float.P_INF);
            }
            OMRES=rightShift(OMRES,1);
        }
        else {
            int firstoneindex=OMRES.indexOf('1');
            for(int i=0;i<firstoneindex;i++){
                if(Eres<1){
                    break;
                }
                else {
                    if(Eres==1){
                        if(OMRES.charAt(0)=='0'){
                            Eres=0;
                        }
                    }
                    else {
                        Eres--;
                        OMRES=leftshift(OMRES);
                    }
                }
            }
        }

        String E=tf.intToBinary(String.valueOf(Eres)).substring(24);
        String res=round(Sres,E,OMRES);
        return new DataType(res);
    }

    /**
     * compute the float add of (dest - src)
     */
    public DataType sub(DataType src, DataType dest) {
        DataType minsrc;
        if(src.toString().charAt(0)=='1'){
            StringBuilder sb=new StringBuilder();
            sb.append(0);
            sb.append(src.toString().substring(1));
            minsrc=new DataType(sb.toString());
        }
        else {
            StringBuilder sb=new StringBuilder();
            sb.append(1);
            sb.append(src.toString().substring(1));
            minsrc=new DataType(sb.toString());
        }
        return add(minsrc,dest);
    }


    private String cornerCheck(String[][] cornerMatrix, String oprA, String oprB) {
        for (String[] matrix : cornerMatrix) {
            if (oprA.equals(matrix[0]) && oprB.equals(matrix[1])) {
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
        int grs = Integer.parseInt(sig_grs.substring(24), 2);
        String sig = sig_grs.substring(0, 24);
        if (grs > 4) {
            sig = oneAdder(sig);
        } else if (grs == 4 && sig.endsWith("1")) {
            sig = oneAdder(sig);
        }

        if (Integer.parseInt(sig.substring(0, sig.length() - 23), 2) > 1) {
            sig = rightShift(sig, 1);
            exp = oneAdder(exp).substring(1);
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
        StringBuilder temp = new StringBuilder(operand);
        temp.reverse();
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
    private String Otrans(String a){
        Transformer tf1=new Transformer();
        if(a.charAt(0)=='0'){
            return a;
        }
        else {
            int Oa= Integer.parseInt(tf1.binaryToInt(a))*-1;
            return tf1.intToBinary(String.valueOf(Oa));
        }
    }
    private  String leftshift(String a){
        StringBuilder sb=new StringBuilder();
        sb.append(a.substring(1));
        sb.append(0);
        return sb.toString();
    }
    private boolean overflowcheck(DataType MSRC,DataType MDES ){
        char ssrc=MSRC.toString().charAt(0);
        char sdes=MDES.toString().charAt(0);
        ALU alu=new ALU();
        DataType MRES=alu.add(MSRC,MDES);
        if(sdes==ssrc&&sdes=='0'){
            return MRES.toString().charAt(4)=='1';
        }
        else if(sdes==ssrc&&sdes=='1'){
            return alu.OF.equals("1");
        }
        else return false;
    }

}

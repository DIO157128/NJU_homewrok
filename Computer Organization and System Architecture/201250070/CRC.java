package util;

public class CRC {

    /**
     * CRC计算器
     *
     * @param data       数据流
     * @param polynomial 多项式
     * @return CheckCode
     */
    public static char[] Calculate(char[] data, String polynomial) {
        String binstr=new String(data);
        int k=polynomial.length()-1;
        StringBuilder sb1=new StringBuilder();
        sb1.append(binstr);
        for(int i=0;i<k;i++){
            sb1.append(0);
        }
        binstr= sb1.toString();
        String tem= binstr.substring(0,k);
        StringBuilder sb=new StringBuilder();
        for(int j=0;j<k+1;j++){
            sb.append(0);
        }
        String zero=sb.toString();
        for(int i=k;i<binstr.length();i++){
            tem+=binstr.charAt(i);
            String remain;
            if(tem.charAt(0)=='1'){
                remain=polynomial;
            }
            else{
                remain=zero;
            }
            int opr1=Integer.parseInt(tem,2);
            int opr2=Integer.parseInt(remain,2);
            int r=opr1^opr2;
            String res=Integer.toBinaryString(r);
            if(res.length()<k+1){
                StringBuilder temsb=new StringBuilder();
                temsb.append(res);
                temsb.reverse();
                int l=res.length();
                for(int j=0;j<k+1-l;j++){
                    temsb.append(0);
                }
                temsb.reverse();
                res=temsb.toString();
            }
            tem=res.substring(1);
        }
        return tem.toCharArray();
    }

    /**
     * CRC校验器
     *
     * @param data       接收方接受的数据流
     * @param polynomial 多项式
     * @param CheckCode  CheckCode
     * @return 余数
     */
    public static char[] Check(char[] data, String polynomial, char[] CheckCode) {
        String d=new String(data);
        String c=new String(CheckCode);
        StringBuilder sb=new StringBuilder();
        sb.append(d).append(c);
        return Calculate(sb.toString().toCharArray(),polynomial);
    }

    public static void main(String[] args){
        char[] data = "100011".toCharArray();
        String p = "1001";
        String s=new String(Calculate(data,p));
        System.out.println(s);
    }
}

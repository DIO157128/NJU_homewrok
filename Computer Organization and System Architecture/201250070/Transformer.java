package transformer;

public class Transformer {
    /**
     * Integer to binaryString
     *
     * @param numStr to be converted
     * @return result
     */
    public String intToBinary(String numStr) {
        int dec=Integer.parseInt(numStr);
        String bin=Integer.toBinaryString(dec);
        StringBuilder sb=new StringBuilder();
        if(dec>=0){
            sb.append(bin).reverse();
            int l=sb.length();
            for(int i=0;i<32-l;i++){
                sb.append("0");
            }
            sb=sb.reverse();
        }
        else{
            sb.append(bin);
        }
        return sb.toString();
    }

    /**
     * BinaryString to Integer
     *
     * @param binStr : Binary string in 2's complement
     * @return :result
     */
    public String binaryToInt(String binStr) {
        if(binStr.charAt(0)=='0'){
            String res=String.valueOf(Integer.parseInt(binStr,2));
            return res;
        }
        else{
            String temstr=binStr.substring(1);
            char []temarr=temstr.toCharArray();
            for(int i=0;i<temarr.length;i++){
                if(temarr[i]=='0'){
                    temarr[i]='1';
                }
                else{
                    temarr[i]='0';
                }
            }
            String bin=new String(temarr);
            String res=String.valueOf(Integer.parseInt(bin,2)*-1-1);
            return res;
        }

    }
    /**
     * The decimal number to its NBCD code
     * */
    public String decimalToNBCD(String decimalStr) {
        int decnum=Integer.parseInt(decimalStr);
        int sign=decnum>=0?1:0;
        int absnum=Math.abs(decnum);
        char[] num=String.valueOf(absnum).toCharArray();
        StringBuilder sb=new StringBuilder();
        for(char c : num){
            StringBuilder temsb=new StringBuilder();
            temsb.append(Integer.toBinaryString(Character.valueOf(c)-48));
            temsb.reverse();
            int l=temsb.length();
            for(int i=0;i<4-l;i++){
                temsb.append(0);
            }
            sb.append(temsb.reverse());
        }
        sb.reverse();
        int l1=sb.length();
        for(int i=0;i<28-l1;i++){
            sb.append(0);
        }
        sb.append(sign==1?"0011":"1011");
        return sb.reverse().toString();
    }

    /**
     * NBCD code to its decimal number
     * */
    public String NBCDToDecimal(String NBCDStr) {
        String head=NBCDStr.substring(0,4);
        StringBuilder sb=new StringBuilder();
        int []temnum=new int[7];
        if(head.equals("1101")){
            sb.append("-");
        }
        for(int i=4;i<32;i+=4){
            temnum[i/4-1]=Integer.valueOf(NBCDStr.substring(i,i+4),2);
        }
        int useful=7;
        for(int i=0;i<7;i++){
            if(temnum[i]!=0){
                useful=i;
                break;
            }
        }
        if(useful==7){
            sb.append("0");
        }
        else{
            for(int i=useful;i<7;i++){
                sb.append(temnum[i]);
            }
        }
        return sb.toString();
    }



    /**
     * Float true value to binaryString
     * @param floatStr : The string of the float true value
     * */
    public String floatToBinary(String floatStr) {
        double floatnum=Math.abs(Double.parseDouble(floatStr));
        char S=Float.parseFloat(floatStr)>=0 ? '0' : '1' ;
        String E = new String();
        String M=  new String();
        if(floatnum==0.0){
            E="00000000";
            M="00000000000000000000000";
        }
        else if(floatnum<Math.pow(2,-126)){
            E="00000000";
            double nownum=floatnum*Math.pow(2,126);
            double tem=nownum;
            StringBuilder sb=new StringBuilder();
            for(int i=0;i<23;i++){
                tem*=2;
                if(tem>=1){
                    sb.append(1);
                    tem-=1;
                }
                else{
                    sb.append(0);
                }
            }
            M=sb.toString();
        }
        else if(floatnum>=Math.pow(2,126)){
            return Float.parseFloat(floatStr)>=0 ? "+Inf" : "-Inf" ;
        }
        else{
            double integer=Math.floor(floatnum);
            double decimal=floatnum-integer;
            String intstring=new String();
            String decstring=new String();
            StringBuilder temsb1=new StringBuilder();
            StringBuilder temsb2=new StringBuilder();
            double nownum1=integer;
            int count=0;
            while(nownum1>0){
                temsb1.append(nownum1%2==0.0?0:1);
                nownum1=Math.floor(nownum1/2);
            }
            intstring=temsb1.append(0).reverse().toString();
            count=0;
            double nownum2=decimal;
            while(count<=149){
                nownum2*=2;
                if(nownum2>=1){
                    temsb2.append(1);
                    nownum2-=1;
                }
                else{
                    temsb2.append(0);
                }
                count++;
            }
            decstring=temsb2.toString();
            String res=new String(intstring+"."+decstring);
            int first1=res.indexOf("1");
            int firstdot=res.indexOf(".");
            int eofnum;
            if(firstdot==first1+1){
                eofnum=127;
            }
            else if(first1==firstdot+1){
                eofnum=126;
            }
            else if(firstdot>first1){
                eofnum=firstdot-first1-1+127;
            }
            else {
                eofnum=firstdot-first1+127;
            }
            StringBuilder Esb=new StringBuilder();
            Esb.append(Integer.toBinaryString(eofnum));
            Esb.reverse();
            int l=Esb.length();
            for(int i=0;i<8-l;i++){
                Esb.append(0);
            }
            E=Esb.reverse().toString();
            res=intstring+decstring;
            M=res.substring(res.indexOf("1")+1,res.indexOf("1")+24);
        }
        return S+E+M;
    }

    /**
     * Binary code to its float true value
     * */
    public String binaryToFloat(String binStr) {
        int sign=binStr.charAt(0)=='0'?0:1;
        String E=binStr.substring(1,9);
        String M=binStr.substring(9);
        if(E.equals("11111111")){
            if(M.contains("1")){
                return "NaN";
            }
            else{
                return sign==0?"+Inf":"-Inf";
            }
        }
        if(E.equals("00000000")){
            double res=0.0;
            if(M.contains("1")){
                for(int i=0;i<23;i++){
                    res+=((int)M.charAt(i)-48)/Math.pow(2,1+i);
                }
                res*=Math.pow(2,-126);
                return sign==0?String.valueOf(res):String.valueOf(-res);
            }
            else{
                return "0.0";
            }
        }
        int e=0;
        for(int i=0;i<8;i++){
            e+=((int)E.charAt(i)-48)*Math.pow(2,7-i);
        }
        e-=127;
        double res=1.0;
        for(int i=0;i<23;i++){
            res+=((int)M.charAt(i)-48)*Math.pow(2,-1-i);
        }
        res*=Math.pow(2,e);
        return sign==0?String.valueOf(res):String.valueOf(-res);
    }


}





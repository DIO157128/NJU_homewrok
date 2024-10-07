package com.nju.edu.erp.utils;


import java.util.Vector;

public class TaxComputing {

    /**
     * 现行税法为累积扣税，即本月所缴税额与本年度以往的工资均相关，
     * 但为方便起见，暂时认为每月缴纳税额独立，可以用月工资唯一确定
     * <3000,3%
     * 3000-12000 10%
     * 12000-25000 20%
     * 25000-35000 25%
     * 35000-55000 30%
     * 55000-80000 35%
     * 80000+  45%
     */
    private static double[] taxation = {90, 900, 2600, 2500, 6000, 8750};

    private static double[] tax = {3000, 12000, 25000, 35000, 55000, 80000};

    private static double[] taxRate = {0.03, 0.10, 0.20, 0.25, 0.3, 0.35, 0.45}; //存放临界税率的数组


    /**
     * 计算税金方法：
     * 首先，设置一个变量j，然后执行0~tax.length长度次的循环。循环里的if语句用来判断
     * 收入是否超过临界金额或等于临界金额。当为是时，变量amountOfTax加上对应的临界金额并把i值赋给j。
     * 循环结束后的if(j==-1)语句判断收入是否小于第一个临界金额并再为是时进行 |收入*相应税率| 的计算。
     * if(j==tax.length-1)语句判断金额是否大于最大临界金额并再为是时进行 |（收入-最大临界金额）*相应税率|。
     * （此时，由于收入大于最大临界金额，循环内的所有临界税率金额都以加上）。
     * if(getAmount()>tax[getState()][j] && getAmount()<tax[getState()][j+1])语句判断收入是否再两个
     * 临界金额范围内，当为是时，执行|（收入-上一个临界金额）*相应税率|操作。
     */
    //计算税金方法
    public static double compute(double _amount) {
        assert (_amount>=0);
        double amountOfTax = 0;
        int j = -1;
        for (int i = 0; i < tax.length; i++) {
            if (_amount > tax[i] || Math.abs(_amount - tax[i]) < 0.00001) {
                amountOfTax += taxation[i];
                j = i;
            }
        }

        if (j == -1)
            return _amount * taxRate[0];
        if (j == tax.length - 1) {
            amountOfTax += (_amount - tax[tax.length - 1]) * taxRate[taxRate.length - 1];
            return amountOfTax;
        }
        if (_amount > tax[j] && _amount < tax[j + 1])
            amountOfTax += (_amount - tax[j]) * taxRate[j + 1];

        return amountOfTax;
    }


    /**
     * 计算税务的链接：
     * http://www.ningbo.gov.cn/art/2019/12/3/art_1229099781_51832298.html?ivk_sa=1024320u
     *
     * @param _amount
     * @return
     */

    //速算扣除数
    private static double[] quickDeduction = {0, 210, 1410, 2660, 4410, 7160, 15160};

    public static Vector<Double> computeNoTrap(double _amount) {
        assert (_amount>=0);
        Vector<Double> vec = new Vector<>();
        double amountOfTax = 0;
        int j = -1;
        for (int i = 0; i < tax.length; i++) {
            if (_amount > tax[i] || Math.abs(_amount - tax[i]) < 0.00001) {
                j = i;
            }
        }
        // -1 3000 0 12000 1 25000 2 35000 3 55000 4 80000 5

        amountOfTax = _amount * taxRate[j + 1] - quickDeduction[j + 1];
        //计算上一个等级，防止出现税收陷阱
        if (j == -1) {
            vec.add(_amount);
            vec.add(amountOfTax);
            return vec;//j==-1没有税收陷阱
        }

        double lastTax = tax[j] * taxRate[j] - quickDeduction[j];  //上个折点的税

        if(tax[j]-lastTax<_amount-amountOfTax){                     //还是现在的税后工资更高，则返回上个节点
            vec.add(_amount);
            vec.add(amountOfTax);
            return vec;
        }
        //否则，则是上个折点的税后工资更高
        vec.add(tax[j]);
        vec.add(lastTax);
        return vec;             //返回较小者
    }


}

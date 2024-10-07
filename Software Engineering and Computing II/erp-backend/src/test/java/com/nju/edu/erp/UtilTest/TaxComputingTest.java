package com.nju.edu.erp.UtilTest;

import com.nju.edu.erp.utils.TaxComputing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Vector;

public class TaxComputingTest {

    /**
     * 测试 no-trap 的
     */
    @Test
    public void test_no_trapTest(){
        Vector<Double> vec = TaxComputing.computeNoTrap(5000);
        Assertions.assertEquals(vec.get(0),5000);
        Assertions.assertEquals(vec.get(1),290);        //正常交税，5000*0.10-210(速算扣除数)
    }

    @Test
    public void test_over_no_trapTest(){                                    //超过80000
        Vector<Double> vec = TaxComputing.computeNoTrap(120000);
        Assertions.assertEquals(vec.get(0),120000);
        Assertions.assertEquals(vec.get(1),38840);        //正常交税，5000*0.10-210(速算扣除数)
    }

    @Test
    public void test_overTest(){                                    //超过80000
        double tax = TaxComputing.compute(120000);
        Assertions.assertEquals(tax,38840);
    }

    @Test
    public void test_lowTest(){                                     //低于3000
        double tax = TaxComputing.compute(2000);
        Assertions.assertEquals(tax,60);                  //0.03的税
    }

    @Test
    public void test_bound_1Test(){                                    //边界值测试
        double tax = TaxComputing.compute(0);
        Assertions.assertEquals(tax,0);
    }

    @Test
    public void test_bound_2Test(){                                    //边界值测试
        Vector<Double> vec = TaxComputing.computeNoTrap(0);
        Assertions.assertEquals(vec.get(0),0);
        Assertions.assertEquals(vec.get(0),0);
    }

}

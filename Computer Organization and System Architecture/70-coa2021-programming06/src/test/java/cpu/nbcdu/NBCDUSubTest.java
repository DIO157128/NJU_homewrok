package cpu.nbcdu;

import org.junit.ComparisonFailure;
import org.junit.Test;
import util.DataType;
import util.Transformer;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class NBCDUSubTest {

    private final NBCDU nbcdu = new NBCDU();
    private final Transformer transformer = new Transformer();
    private DataType src;
    private DataType dest;
    private DataType result;
    private DataType expected;
    @Test
    public void SubTest1() {
        src = new DataType("11000000000110000110000001010011");
        dest = new DataType("11010000001101000110000001010011");
        result = nbcdu.add(src, dest);
        assertEquals("11010000000101100000000000000000", result.toString());
    }
    @Test
    public void SubTestrandom() {
        int i = 10000;
        while (i > 0) {
            try {
                Random r = new Random();
                int a1 = r.nextInt(1000000) - 500000;
                int a2 = r.nextInt(1000000) - 500000;
                src = new DataType(new Transformer().decimalToNBCD(String.valueOf(a1)));
                dest = new DataType(new Transformer().decimalToNBCD(String.valueOf(a2)));
                result = nbcdu.add(src, dest);
                expected = new DataType(new Transformer().decimalToNBCD(String.valueOf(a2 + a1)));
                assertEquals(expected.toString(), result.toString());
            } catch (ComparisonFailure c) {
                System.out.println("expected:" + expected + " but was:" + result + " src:" + src + " dest:" + dest);
            }
            i--;
        }
    }
}

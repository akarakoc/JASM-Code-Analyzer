import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    @Test
    public void testSub() {
        Calculator calc = new Calculator();
        assertEquals(1, calc.sub(3, 2));
    }


    @Test
    public void testDivide() {
        Calculator calc = new Calculator();
        assertEquals(4, calc.div(8, 2));
    }


    @Test
    public void testMult() {
        Calculator calc = new Calculator();
        assertEquals(10, calc.mult(5, 2));
    }


    @Test
    public void complex() {
        Calculator calc = new Calculator();
        calc.other();
    }

    @Test
    public void testMultiIfElse() {
        Calculator data = new Calculator();
        data.multiIfElse();
    }


}
package calc;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyCalculatorTest  {

    @Test
    public void testCorrectExprCalculate() {
        assertEquals(6, MyCalculator.calculate("2+2*2"), 0.1);
        assertEquals(8, MyCalculator.calculate("(2+2)*2"), 0.1);
        assertEquals(48, MyCalculator.calculate("(2+2)!*2"), 0.1);
        assertEquals(9, MyCalculator.calculate("-1 + sumFromTo(1,5)"), 0.1);
        assertEquals(10, MyCalculator.calculate("2+2^3"), 0.1);
        assertEquals(18, MyCalculator.calculate("2+2^3 * 2"), 0.1);
        assertEquals(1.5, MyCalculator.calculate("3*(1/2)"), 0.1);
        assertEquals(54, MyCalculator.calculate("2*(3*[4+5])"), 0.1);
        assertEquals(4.5, MyCalculator.calculate("2.25 * 2"), 0.1);
        assertEquals(5.85, MyCalculator.calculate("pi + e"), 0.1);
        assertEquals(1, MyCalculator.calculate("sin(1)^2 + cos(1)^1"), 0.1);
        assertEquals(0, MyCalculator.calculate("0"), 0.1);
    }

    @Test
    public void testIncorrectExprCalculate() {
        assertThrows(RuntimeException.class, ()->MyCalculator.calculate(""));
        assertThrows(RuntimeException.class, ()->MyCalculator.calculate("12)"));
        assertThrows(RuntimeException.class, ()->MyCalculator.calculate("--1"));
        assertThrows(RuntimeException.class, ()->MyCalculator.calculate("badToken + 1"));
        assertThrows(RuntimeException.class, ()->MyCalculator.calculate("sin(1,2)"));
    }
}
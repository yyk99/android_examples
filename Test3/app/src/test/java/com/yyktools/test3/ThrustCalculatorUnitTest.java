package com.yyktools.test3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by y.kuznetsov on 12/21/2017.
 */

public class ThrustCalculatorUnitTest {
    @Test
    public void calculate_isCorrect() throws Exception {
        //assertEquals(4, 2 + 2);

        ThrustCalculator tc = new ThrustCalculator();

        tc.calculate(10, 6, 10000, 2);
        assertEquals(0.42858, tc.getHp(), 1E-5);
        assertEquals(60000, tc.getLoad(), 1E-5);
        assertEquals(56.82, tc.getSpeed(), 1E-5);
        assertEquals(2.83, tc.getThrust(), 1E-5);

        tc.calculate(10, 6, 10000, 3);
        assertEquals(0.64287, tc.getHp(), 1E-5);
        assertEquals(60000, tc.getLoad(), 1E-5); // TODO: check if load depens on number of blades?
        assertEquals(56.82, tc.getSpeed(), 1E-5);
        assertEquals(3.962, tc.getThrust(), 1E-5);
    }

    @Test (expected = RuntimeException.class)
    public void calculate_isCorrect2() throws Exception {
        ThrustCalculator tc = new ThrustCalculator();
        tc.calculate(10, 6, 10000, 1);
    }
}

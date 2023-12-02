package io.raytracer.algebra.solvers;

import org.apache.commons.math3.complex.Complex;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DurandKernerTest {

    @Test
    void solveQuadratic() {
        double[] testCoefficients = new double[]{2, -3, 1};
        DurandKerner solver = new DurandKerner(testCoefficients);

        Complex[] expectedRoots = new Complex[] {new Complex(1.0), new Complex(2.0)};
        Complex[] actualRoots = solver.solve();
        Arrays.sort(actualRoots, Comparator.comparingDouble(Complex::getReal));

        assertTrue(solver.iterationsCount < DurandKerner.maxIterations);
        assertAll(IntStream.range(0, 2).mapToObj(i -> () -> assertTrue(Complex.equals(expectedRoots[i], actualRoots[i], 1e-6))));
    }

    @Test
    void solveQuartic() {
        double[] testCoefficients = new double[]{24, -50, 35, -10, 1};
        DurandKerner solver = new DurandKerner(testCoefficients);

        Complex[] expectedRoots = new Complex[] {new Complex(1), new Complex(2), new Complex(3), new Complex(4)};
        Complex[] actualRoots = solver.solve();
        Arrays.sort(actualRoots, Comparator.comparingDouble(Complex::getReal));

        assertTrue(solver.iterationsCount < DurandKerner.maxIterations);
        assertAll(IntStream.range(0, 4).mapToObj(i -> () -> assertTrue(Complex.equals(expectedRoots[i], actualRoots[i], 1e-6))));
    }
}
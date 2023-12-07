package io.raytracer.algebra.solvers;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlgebraicSolverTest {

    @Test
    void solveQuarticRealRoots() {
        double[] solutions = AlgebraicSolver.solveQuartic(24, -50, 35, -10, 1);
        Arrays.sort(solutions);

        assertAll(IntStream.range(0, 4).mapToObj(i -> () -> assertEquals(i+1, solutions[i], 1e-6)));
    }

    @Test
    void solveQuarticFourRealRoots() {
        double[] solutions = AlgebraicSolver.solveQuartic(60963.1204, -15802.0951408, 1517.361926146, -63.981274357, 1);
        Arrays.sort(solutions);

        assertEquals(4, solutions.length);
        assertTrue(solutions[0] > 12 && solutions[0] < 12.4);
        assertTrue(solutions[1] > 14.2 && solutions[1] < 14.4);
        assertTrue(solutions[2] > 17.6 && solutions[2] < 17.8);
        assertTrue(solutions[3] > 19.8 && solutions[3] < 20);
    }

    @Test
    void solveQuarticNoRealRoots() {
        double[] solutions = AlgebraicSolver.solveQuartic(1, 0, 0, 0, 1);

        assertEquals(0, solutions.length);
    }

    @Test
    void solveQuarticTwoRealRoots() {
        double[] solutions = AlgebraicSolver.solveQuartic(-0.9, -3.9, 2.7, -1.3, 1);
        Arrays.sort(solutions);

        assertEquals(2, solutions.length);
        assertEquals(-0.2, solutions[0], 1e-3);
        assertEquals(1.5, solutions[1], 1e-3);
    }
}
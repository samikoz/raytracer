package io.raytracer.algebra.solvers;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AlgebraicSolverTest {

    @Test
    void solveQuarticRealRoots() {
        double[] solutions = AlgebraicSolver.solveQuartic(24, -50, 35, -10, 1);
        Arrays.sort(solutions);

        assertAll(IntStream.range(0, 4).mapToObj(i -> () -> assertEquals(i+1, solutions[i], 1e-6)));
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
package io.raytracer.algebra;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EquationTest {
    @Test
    void solveSecondOrder() {
        IEquation eqn = new Equation(0, -4, 1);
        double[] solutions = eqn.solve();

        assertEquals(0, solutions[0]);
        assertEquals(4, solutions[1]);
    }

    @Test
    void reduceDegreeReturnRealRoots() {
        IEquation eqn = new Equation(-2, 1, -2, 1, 5e-8);
        double[] solutions = eqn.solve();

        assertEquals(1, solutions.length);
        assertEquals(2, solutions[0], 1e-3);
    }
}
package io.raytracer.algebra;

import io.raytracer.algebra.Equation;
import io.raytracer.algebra.IEquation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquationTest {

    @Test
    void solveSecondOrder() {
        IEquation eqn = new Equation(0, -4, 1);
        double[] solutions = eqn.solve();

        assertEquals(0, solutions[0]);
        assertEquals(4, solutions[1]);
    }
}
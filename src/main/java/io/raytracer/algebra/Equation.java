package io.raytracer.algebra;

import io.raytracer.algebra.solvers.AlgebraicSolver;

public class Equation implements IEquation {
    private final double[] coefficients;

    private static final double tolerance = 1e-7;

    public Equation(double... coefficients) {
        this.coefficients = coefficients;
    }

    @Override
    public double[] solve() {
        if (this.coefficients.length == 3 && Math.abs(this.coefficients[2]) > Equation.tolerance) {
            return AlgebraicSolver.solveSecondOrder(this.coefficients[0], this.coefficients[1], this.coefficients[2]);
        }
        return new double[0];
    }
}

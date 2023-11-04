package io.raytracer.geometry;

public class Equation implements IEquation {
    private final double[] coefficients;

    private static final double tolerance = 1e-7;

    public Equation(double... coefficients) {
        this.coefficients = coefficients;
    }

    @Override
    public double[] solve() {
        if (this.coefficients.length == 3 && Math.abs(this.coefficients[2]) > Equation.tolerance) {
            return EquationSolver.solveSecondOrder(this.coefficients[0], this.coefficients[1], this.coefficients[2]);
        }
        return new double[0];
    }
}

class EquationSolver {
    static double[] solveSecondOrder(double c, double b, double a) {
        double delta = Math.pow(b, 2) - 4*a*c;
        if (delta < 0) {
            return new double[0];
        }
        double deltaRoot = Math.sqrt(delta);
        return new double[] { (-b - deltaRoot)/(2*a), (-b + deltaRoot)/(2*a) };
    }
}

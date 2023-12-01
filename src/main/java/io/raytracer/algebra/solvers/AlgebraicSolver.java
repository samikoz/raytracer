package io.raytracer.algebra.solvers;

public class AlgebraicSolver {
    public static double[] solveSecondOrder(double c, double b, double a) {
        double delta = Math.pow(b, 2) - 4*a*c;
        if (delta < 0) {
            return new double[0];
        }
        double deltaRoot = Math.sqrt(delta);
        return new double[] { (-b - deltaRoot)/(2*a), (-b + deltaRoot)/(2*a) };
    }
}
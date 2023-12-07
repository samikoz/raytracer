package io.raytracer.algebra;

import io.raytracer.algebra.solvers.AlgebraicSolver;
import io.raytracer.algebra.solvers.DurandKerner;
import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;

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
        else if (this.coefficients.length > 3)
        {
            int equationOrder = this.coefficients.length - 1;
            while (Math.abs(this.coefficients[equationOrder]) < Equation.tolerance) {
                equationOrder--;
            }
            double[] reducedCoefficients;
            if (equationOrder != this.coefficients.length - 1) {
                reducedCoefficients = new double[equationOrder+1];
                System.arraycopy(this.coefficients, 0, reducedCoefficients, 0, equationOrder + 1);
            }
            else {
                reducedCoefficients = this.coefficients;
            }
            int trial = 0;
            while (trial++ < 2) {
                DurandKerner solver = new DurandKerner(reducedCoefficients, trial);
                Complex[] roots = solver.solve();
                if (solver.iterationsCount < DurandKerner.maxIterations) {
                    List<Double> realRoots = new ArrayList<>();
                    for (Complex root : roots) {
                        if (Math.abs(root.getImaginary()) < Equation.tolerance) {
                            realRoots.add(root.getReal());
                        }
                    }
                    double[] solutions = new double[realRoots.size()];
                    for (int i = 0; i < realRoots.size(); i++) {
                        solutions[i] = realRoots.get(i);
                    }
                    return solutions;
                }
            }
        }
        return new double[0];
    }
}

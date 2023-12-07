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
        double[] reducedCoeffs = this.reduceInsignificantLeadingCoeffs(this.coefficients);
        if (reducedCoeffs.length == 3) {
            return AlgebraicSolver.solveSecondOrder(reducedCoeffs[0], reducedCoeffs[1], reducedCoeffs[2]);
        }
        else if (reducedCoeffs.length == 5) {
            return AlgebraicSolver.solveQuartic(reducedCoeffs[0], reducedCoeffs[1], reducedCoeffs[2], reducedCoeffs[3], reducedCoeffs[4]);
        }
        else if (reducedCoeffs.length == 4 || reducedCoeffs.length > 5) {
            int trial = 0;
            while (trial++ < 2) {
                DurandKerner solver = new DurandKerner(reducedCoeffs, trial);
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

    private double[] reduceInsignificantLeadingCoeffs(double[] coeffs) {
        int equationOrder = coeffs.length - 1;
        while (Math.abs(coeffs[equationOrder]) < Equation.tolerance && equationOrder > 0) {
            equationOrder--;
        }
        double[] reducedCoefficients;
        if (equationOrder == coeffs.length - 1) {
            return coeffs;
        }
        reducedCoefficients = new double[equationOrder+1];
        System.arraycopy(coeffs, 0, reducedCoefficients, 0, equationOrder + 1);
        return reducedCoefficients;
    }
}

package io.raytracer.algebra.solvers;

import org.apache.commons.math3.complex.Complex;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

public class DurandKerner {
    public int iterationsCount;

    private final Function<Complex, Complex> polynomial;
    private final int degree;
    private final Random rand;

    public static final int maxIterations = 100;
    private static final double convergenceThreshold = 1e-6;

    public DurandKerner(double[] coefficients, long seed) {
        this.polynomial = this.makePolynomial(coefficients);
        this.degree = coefficients.length - 1;
        this.iterationsCount = 0;
        this.rand = new Random(seed);
    }

    public DurandKerner(double[] coefficients) {
        this(coefficients, 1);
    }

    public Complex[] solve() {
        Complex[] roots = new Complex[this.degree];

        double angle = 2*Math.PI * this.rand.nextDouble() + 2.0 * Math.PI / this.degree;
        for (int i = 0; i < this.degree; i++) {
            roots[i] = new Complex(Math.cos(i * angle), Math.sin(i * angle));
        }


        while (this.iterationsCount++ < DurandKerner.maxIterations) {
            Complex[] newRoots = Arrays.copyOf(roots, this.degree);

            for (int i = 0; i < degree; i++) {
                newRoots[i] = this.evaluateContraction(i, newRoots);
            }

            boolean converged = true;
            for (int i = 0; i < degree; i++) {
                if (newRoots[i].subtract(roots[i]).abs() > DurandKerner.convergenceThreshold) {
                    converged = false;
                    break;
                }
            }

            if (converged) {
                return newRoots;
            }

            roots = newRoots;
        }

        return roots;
    }

    private Function<Complex, Complex> makePolynomial(double[] coeffs) {
        return z -> {
            Complex sum = new Complex(coeffs[0]);
            for (int i = 1; i < coeffs.length; i++) {
                sum = sum.add(z.pow(i).multiply(coeffs[i]));
            }
            return sum;
        };
    }

   private Complex evaluateContraction(int rootIndex, Complex[] approximateRoots) {
       Complex numerator = this.polynomial.apply(approximateRoots[rootIndex]);
       Complex denominator = Complex.ONE;

       for (int j = 0; j < this.degree; j++) {
           if (j != rootIndex) {
               denominator = denominator.multiply(approximateRoots[rootIndex].subtract(approximateRoots[j]));
           }
       }

       return approximateRoots[rootIndex].subtract(numerator.divide(denominator));
   }
}

package io.raytracer.algebra.solvers;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;

public class AlgebraicSolver {
    private static double tolerance = 1e-7;

    public static double[] solveSecondOrder(double c, double b, double a) {
        double delta = Math.pow(b, 2) - 4*a*c;
        if (delta < 0) {
            return new double[0];
        }
        double deltaRoot = Math.sqrt(delta);
        return new double[] { (-b - deltaRoot)/(2*a), (-b + deltaRoot)/(2*a) };
    }

    public static double[] solveQuartic(double e, double d, double c, double b, double a) {
        double b2 = Math.pow(b,2);
        double a2 = Math.pow(a,2);
        double a3 = Math.pow(a,3);
        double A = -3*b2/(8*a2) + c/a;
        double B = Math.pow(b,3)/(8*a3) - b*c/(2*a2) + d/a;
        double C = -3*Math.pow(b,4)/(256*Math.pow(a,4)) + c*b2/(16*a3) - b*d/(4*a2) + e/a;
        if (Math.abs(B) < AlgebraicSolver.tolerance) {
            double quasiDeltaSq = Math.pow(A,2) - 4*C;
            if (quasiDeltaSq < 0) {
                return new double[] {};
            }
            double quasiDelta = Math.sqrt(quasiDeltaSq);
            double front = -b/(4*a);
            if (-A - quasiDelta > 0) {
                return new double[]{
                    front + Math.sqrt((-A + quasiDelta) / 2),
                    front + Math.sqrt((-A - quasiDelta) / 2),
                    front - Math.sqrt((-A + quasiDelta) / 2),
                    front - Math.sqrt((-A - quasiDelta) / 2)
                };
            }
            else if (-A + quasiDelta > 0) {
                return new double[] {
                    front + Math.sqrt((-A + quasiDelta) / 2),
                    front - Math.sqrt((-A + quasiDelta) / 2),
                };
            }
            return new double[] {};
        }
        else {
            double p = -Math.pow(A,2)/12 - C;
            double q = -Math.pow(A,3)/108 + A*C/3 - Math.pow(B,2)/8;
            double rrootSq = Math.pow(q,2)/4 + Math.pow(p,3)/27;
            Complex r = new Complex(rrootSq).sqrt().add(-q/2);
            Complex y;
            if (r.abs() < AlgebraicSolver.tolerance) {
                y = new Complex(-5*A/6 - Math.pow(q, 1.0/3));
            }
            else {
                //apparently can so choose the cube root here so that y is real
                Complex u = r.pow(1.0/3);
                y = new Complex(-5*A/6).add(u).subtract(new Complex(p).divide(u.multiply(3)));
            }
            Complex w = y.multiply(2).add(A).sqrt();
            List<Double> roots = new ArrayList<>();
            Complex negativeSubRoot = y.multiply(-2).subtract(3*A).subtract(new Complex(2*B).divide(w));
            Complex rootOne = negativeSubRoot.sqrt().add(w).divide(2).add(-b/(4*a));
            if (Math.abs(rootOne.getImaginary()) < AlgebraicSolver.tolerance) {
                roots.add(rootOne.getReal());
            }
            Complex rootTwo = negativeSubRoot.sqrt().multiply(-1).add(w).divide(2).add(-b/(4*a));
            if (Math.abs(rootTwo.getImaginary()) < AlgebraicSolver.tolerance) {
                roots.add(rootTwo.getReal());
            }
            Complex positiveSubRoot = negativeSubRoot.add(new Complex(4*B).divide(w));
            Complex rootThree = positiveSubRoot.sqrt().subtract(w).divide(2).add(-b/(4*a));
            if (Math.abs(rootThree.getImaginary()) < AlgebraicSolver.tolerance) {
                roots.add(rootThree.getReal());
            }
            Complex rootFour = positiveSubRoot.sqrt().multiply(-1).subtract(w).divide(2).add(-b/(4*a));
            if (Math.abs(rootFour.getImaginary()) < AlgebraicSolver.tolerance) {
                roots.add(rootFour.getReal());
            }
            double[] realRoots = new double[roots.size()];
            for (int i = 0; i < roots.size(); i++) {
                realRoots[i] = roots.get(i);
            }
            return realRoots;
        }
    }
}
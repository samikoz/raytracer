package io.raytracer.algebra.solvers;

import java.util.ArrayList;
import java.util.List;

public class AlgebraicSolver {
    private static double tolerance = 1e-6;
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
            if (rrootSq < 0) {
                return new double[]{};
            }
            double r = -q/2 + Math.sqrt(rrootSq);
            double y;
            if (Math.abs(r) < AlgebraicSolver.tolerance) {
                y = -5*A/6 - Math.pow(q, 1.0/3);
            }
            else {
                double u = Math.pow(r, 1.0/3);
                y = -5*A/6 + u - p/(3*u);
            }
            double wSq = A + 2*y;
            if (wSq < 0) {
                return new double[] {};
            }
            double w = Math.sqrt(wSq);
            List<Double> solRoots = new ArrayList<>();
            double firstSquared = -(3*A + 2*y + 2*B/w);
            if (firstSquared > 0) {
                solRoots.add(-b/(4*a) + (w + Math.sqrt(firstSquared))/2);
                solRoots.add(-b/(4*a) + (w - Math.sqrt(firstSquared))/2);
            }
            double secondSquared = -(3*A + 2*y - 2*B/w);
            if (secondSquared > 0) {
                solRoots.add(-b/(4*a) - (w + Math.sqrt(secondSquared))/2);
                solRoots.add(-b/(4*a) +- (w - Math.sqrt(secondSquared))/2);
            }
            double[] realSolutions = new double[solRoots.size()];
            for (int i = 0; i < solRoots.size(); i++) {
                realSolutions[i] = solRoots.get(i);
            }
            return realSolutions;
        }
    }
}
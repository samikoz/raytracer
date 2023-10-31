package io.raytracer.geometry;

import java.util.Arrays;

public class TwoMatrix extends CofactorDetMatrix {
    private final double[] entries;
    private final static double equalityTolerance = 1e-3;

    TwoMatrix() {
        entries = new double[4];
    }

    public TwoMatrix(double... entries) {
        this.entries = new double[]{entries[0], entries[1], entries[2], entries[3]};
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.entries);
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;
        TwoMatrix themMatrix = (TwoMatrix) them;

        return (
                Math.abs(this.entries[0] - themMatrix.entries[0]) < equalityTolerance &&
                        Math.abs(this.entries[1] - themMatrix.entries[1]) < equalityTolerance &&
                        Math.abs(this.entries[2] - themMatrix.entries[2]) < equalityTolerance &&
                        Math.abs(this.entries[3] - themMatrix.entries[3]) < equalityTolerance
        );
    }

    void set(int x, int y, double element) {
        entries[x*2 + y] = element;
    }

    @Override
    double get(int x, int y) {
        return entries[x*2 + y];
    }

    public double det() {
        return entries[0] * entries[3] - entries[1] * entries[2];
    }

    @Override
    CofactorDetMatrix submatrix(int rowToSkip, int ColToSkip) {
        return null;
    }
}
package io.raytracer.mathsy;

import java.util.Arrays;

public class RealSquareMatrix implements Matrix {
    private int dim;
    private double[][] rows;
    private double[][] cols;

    public RealSquareMatrix(double... entries) {
        double sizeRoot = Math.sqrt(entries.length);
        dim = (int) sizeRoot;
        assert Math.abs(sizeRoot - dim) < 1e-3;

        rows = new double[dim][dim];
        cols = new double[dim][dim];

        int entriesIndex = 0;
        for (int rowIndex = 0; rowIndex < dim; ++rowIndex) {
            for (int i = 0; i < dim; ++i) {
                rows[rowIndex][i] = entries[entriesIndex++];
                cols[i][rowIndex] = rows[rowIndex][i];
            }
        }
    }

    public double get(int x, int y) {
        return rows[x][y];
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(rows);
    }

    @Override
    public boolean equals(Object them) {
        if (this.getClass() != them.getClass()) return false;
        RealSquareMatrix themMatrix = (RealSquareMatrix) them;
        if (this.dim != themMatrix.dim) return false;

        double maxEntryDifference = 0;
        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                double diff = Math.abs(this.get(x, y) - themMatrix.get(x, y));
                if (diff > maxEntryDifference) maxEntryDifference = diff;
            }
        }
        return (maxEntryDifference < 1e-3);
    }
}

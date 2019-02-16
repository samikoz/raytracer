package io.raytracer.mathsy;

import java.util.Arrays;

public class RealSquareMatrix implements Matrix {
    private int dim;
    private double[][] entries;

    private RealSquareMatrix(int dimension) {
        dim = dimension;
        entries = new double[dim][dim];
    }

    public RealSquareMatrix(double... entries) {
        double sizeRoot = Math.sqrt(entries.length);
        dim = (int) sizeRoot;
        assert Math.abs(sizeRoot - dim) < 1e-3;

        this.entries = new double[dim][dim];

        int entriesIndex = 0;
        for (int rowIndex = 0; rowIndex < dim; ++rowIndex) {
            for (int i = 0; i < dim; ++i) {
                this.entries[rowIndex][i] = entries[entriesIndex++];
            }
        }
    }

    @Override
    public int dim() {
        return dim;
    }

    @Override
    public double get(int x, int y) {
        return entries[x][y];
    }

    private void set(int x, int y, double element) {
        entries[x][y] = element;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(entries);
    }

    @Override
    public boolean equals(Object them) {
        if (this.getClass() != them.getClass()) return false;
        Matrix themMatrix = (Matrix) them;
        if (this.dim() != themMatrix.dim()) return false;

        double maxEntryDifference = 0;
        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                double diff = Math.abs(this.get(x, y) - themMatrix.get(x, y));
                if (diff > maxEntryDifference) maxEntryDifference = diff;
            }
        }
        return (maxEntryDifference < 1e-3);
    }

    private static double dot(double[] row, double[] column) {
        double dotted = 0;
        for (int i = 0; i < row.length; i++) {
            dotted += row[i]*column[i];
        }
        return dotted;
    }

    @Override
    public Matrix multiply(Matrix them) {
        assert this.dim() == them.dim();
        int dim = this.dim();

        RealSquareMatrix product = new RealSquareMatrix(dim);
        for (int y = 0; y < dim; y++) {
            double[] theirColumn = new double[dim];
            for (int i = 0; i < dim; i++) {
                theirColumn[i] = them.get(i, y);
            }

            for (int x = 0; x < dim; x++) {
                product.set(x, y, RealSquareMatrix.dot(this.entries[x], theirColumn));
            }
        }
        return product;
    }

    @Override
    public Vector multiply(Vector them) {
        assert this.dim() == them.dim();

        double[] productCoords = new double[them.dim()];
        for (int coordIndex = 0; coordIndex < them.dim(); coordIndex++) {
            productCoords[coordIndex] = RealSquareMatrix.dot(entries[coordIndex], them.toArray());
        }

        return new RealTuple(productCoords);
    }

    public static Matrix id (int dim) {
        RealSquareMatrix identity = new RealSquareMatrix(dim);

        for (int diagonalIndex = 0; diagonalIndex < dim; diagonalIndex++) {
            identity.set(diagonalIndex, diagonalIndex, 1);
        }

        return identity;
    }
}

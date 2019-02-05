package io.raytracer.mathsy;

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
}

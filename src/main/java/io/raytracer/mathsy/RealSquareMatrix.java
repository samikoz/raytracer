package io.raytracer.mathsy;

import java.util.Arrays;
import java.util.stream.IntStream;

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
        assert Math.abs(sizeRoot - dim) < 1e-3 && dim > 0;

        this.entries = new double[dim][dim];
        IntStream.range(0, dim).forEach(rowIndex ->
            Arrays.setAll(this.entries[rowIndex], colIndex -> entries[dim*rowIndex + colIndex])
        );
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

        double allowedDifference = 1e-3;
        double maxDifference = IntStream.range(0, dim).mapToDouble(x ->
            IntStream.range(0, dim).mapToDouble(y ->
                Math.abs(this.get(x, y) - themMatrix.get(x, y))
            ).max().orElse(allowedDifference + 1)
        ).max().orElse(allowedDifference + 1);

        return (maxDifference < allowedDifference);
    }

    private static double dot(double[] row, double[] column) {
        return IntStream.range(0, row.length).mapToDouble(i -> row[i]*column[i]).sum();
    }

    @Override
    public Matrix multiply(Matrix them) {
        assert this.dim() == them.dim();

        RealSquareMatrix product = new RealSquareMatrix(dim);
        IntStream.range(0, dim).forEach(y -> {
            double[] theirColumn = new double[dim];
            Arrays.setAll(theirColumn, i -> them.get(i, y));
            IntStream.range(0, dim).forEach(x ->
                product.set(x, y, RealSquareMatrix.dot(this.entries[x], theirColumn))
            );
        });

        return product;
    }

    @Override
    public Vector multiply(Vector them) {
        assert this.dim() == them.dim();

        return new RealTuple(IntStream.range(0, dim).mapToDouble(
            coordinateIndex -> RealSquareMatrix.dot(entries[coordinateIndex], them.toArray())
        ).toArray());
    }

    public static Matrix id (int dim) {
        RealSquareMatrix identity = new RealSquareMatrix(dim);

        IntStream.range(0, dim).forEach(diagonalIndex -> identity.set(diagonalIndex, diagonalIndex, 1));

        return identity;
    }

    @Override
    public Matrix transpose() {
        RealSquareMatrix transposed = new RealSquareMatrix(dim);
        IntStream.range(0, dim).forEach(i ->
            IntStream.range(0, dim).forEach(j -> transposed.set(i, j, this.get(j, i)))
        );
        return transposed;
    }

    @Override
    public boolean isInvertible() {
        return det() != 0;
    }

    @Override
    public double det() {
        if (dim == 2) {
            return get(0,0)*get(1, 1) - get(0, 1)*get(1, 0);
        }
        else {
            return IntStream.range(0, dim).mapToDouble(firstRowIndex ->
                get(0, firstRowIndex)*cofactor(0, firstRowIndex)
            ).sum();
        }
    }

    RealSquareMatrix submatrix(int rowToSkip, int colToSkip) {
        RealSquareMatrix sub = new RealSquareMatrix(dim -1);

        IntStream.range(0, dim - 1).forEach(x ->
            IntStream.range(0, dim - 1).forEach(y ->
                sub.set(x, y, this.get(
                    x >= rowToSkip ? x + 1 : x,
                    y >= colToSkip ? y + 1 : y
                ))
            )
        );
        return sub;
    }

    double cofactor(int row, int col) {
        return Math.pow(-1, row + col)*submatrix(row, col).det();
    }

    @Override
    public Matrix inverse() {
        RealSquareMatrix inverted = new RealSquareMatrix(dim);
        double det = det();

        IntStream.range(0, dim).forEach(row ->
            IntStream.range(0, dim).forEach(col ->
                inverted.set(col, row, cofactor(row, col)/det)
            )
        );
        return inverted;
    }
}

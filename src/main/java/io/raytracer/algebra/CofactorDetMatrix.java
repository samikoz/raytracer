package io.raytracer.algebra;

import java.util.stream.IntStream;

public abstract class CofactorDetMatrix {
    abstract double get(int x, int y);

    abstract double det();

    protected double det(int dim) {
        return IntStream.range(0, dim).mapToDouble(firstRowIndex ->
                this.get(0, firstRowIndex) * cofactor(0, firstRowIndex)
        ).sum();
    }

    protected double maxEntryDifference(int dim, CofactorDetMatrix them) {
        return IntStream.range(0, dim).mapToDouble(x ->
                IntStream.range(0, dim).mapToDouble(y ->
                        Math.abs(this.get(x, y) - them.get(x, y))
                ).max().orElse(1)
        ).max().orElse(1);
    }

    double cofactor(int row, int col) {
        return Math.pow(-1, row + col) * submatrix(row, col).det();
    }

    abstract CofactorDetMatrix submatrix(int rowToSkip, int ColToSkip);
}

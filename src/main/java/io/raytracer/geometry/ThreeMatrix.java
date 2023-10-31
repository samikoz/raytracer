package io.raytracer.geometry;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ThreeMatrix extends CofactorDetMatrix {
    private final double[][] entries;
    private final static double equalityTolerance = 1e-3;

    ThreeMatrix() {
        entries = new double[3][3];
    }

    public ThreeMatrix(double... entries) {
        this.entries = new double[3][3];
        IntStream.range(0, 3).forEach(rowIndex ->
                Arrays.setAll(this.entries[rowIndex], colIndex -> entries[3 * rowIndex + colIndex])
        );
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(entries);
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;
        ThreeMatrix themMatrix = (ThreeMatrix) them;

        double maxEntryDifference = super.maxEntryDifference(3, themMatrix);

        return (maxEntryDifference < equalityTolerance);
    }

    public double get(int x, int y) {
        return entries[x][y];
    }

    void set(int x, int y, double element) {
        entries[x][y] = element;
    }

    public double det() {
        return super.det(3);
    }

    CofactorDetMatrix submatrix(int rowToSkip, int colToSkip) {
        TwoMatrix submatrix = new TwoMatrix();

        IntStream.range(0, 2).forEach(x ->
                IntStream.range(0, 2).forEach(y ->
                        submatrix.set(x, y, this.get(
                                x >= rowToSkip ? x + 1 : x,
                                y >= colToSkip ? y + 1 : y
                        ))
                )
        );
        return submatrix;
    }
}

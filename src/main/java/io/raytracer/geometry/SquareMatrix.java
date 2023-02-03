package io.raytracer.geometry;

import lombok.ToString;

import java.util.Arrays;
import java.util.stream.IntStream;

@ToString
public class SquareMatrix implements ISquareMatrix {
    private final int dim;
    private final double[][] entries;
    private final static double equalityTolerance = 1e-3;

    private SquareMatrix(int dimension) {
        assert dimension > 0;
        dim = dimension;
        entries = new double[dim][dim];
    }

    public SquareMatrix(double... entries) {
        double sizeRoot = Math.sqrt(entries.length);
        dim = (int) sizeRoot;
        assert Math.abs(sizeRoot - dim) < equalityTolerance && dim > 0;

        this.entries = new double[dim][dim];
        IntStream.range(0, dim).forEach(rowIndex ->
                Arrays.setAll(this.entries[rowIndex], colIndex -> entries[dim * rowIndex + colIndex])
        );
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(entries);
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;
        ISquareMatrix themMatrix = (ISquareMatrix) them;
        if (this.dim() != themMatrix.dim()) return false;

        double maxEntryDifference = IntStream.range(0, dim).mapToDouble(x ->
                IntStream.range(0, dim).mapToDouble(y ->
                        Math.abs(this.get(x, y) - themMatrix.get(x, y))
                ).max().orElse(1)
        ).max().orElse(1);

        return (maxEntryDifference < equalityTolerance);
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

    private static double dot(double[] row, double[] column) {
        return (new Vector(row)).dot(new Vector(column));
    }

    @Override
    public ISquareMatrix multiply(ISquareMatrix them) {
        assert this.dim() == them.dim();

        SquareMatrix product = new SquareMatrix(dim);
        IntStream.range(0, dim).forEach(y -> {
            double[] theirColumn = IntStream.range(0, dim).mapToDouble(i -> them.get(i, y)).toArray();
            IntStream.range(0, dim).forEach(x ->
                    product.set(x, y, SquareMatrix.dot(this.entries[x], theirColumn))
            );
        });

        return product;
    }

    @Override
    public Tuple multiply(ITuple them) {
        assert this.dim() == them.dim();

        double[] themArray = IntStream.range(0, them.dim()).mapToDouble(them::get).toArray();

        return new Tuple(IntStream.range(0, dim).mapToDouble(
                coordinateIndex -> SquareMatrix.dot(entries[coordinateIndex], themArray)
        ).toArray());
    }

    public static ISquareMatrix id(int dim) {
        SquareMatrix identity = new SquareMatrix(dim);

        IntStream.range(0, dim).forEach(diagonalIndex -> identity.set(diagonalIndex, diagonalIndex, 1));

        return identity;
    }

    @Override
    public ISquareMatrix transpose() {
        SquareMatrix transposed = new SquareMatrix(dim);
        IntStream.range(0, dim).forEach(i ->
                IntStream.range(0, dim).forEach(j -> transposed.set(i, j, this.get(j, i)))
        );
        return transposed;
    }

    @Override
    public double det() {
        if (dim == 1) {
            return this.get(0, 0);
        } else {
            return IntStream.range(0, dim).mapToDouble(firstRowIndex ->
                    get(0, firstRowIndex) * cofactor(0, firstRowIndex)
            ).sum();
        }
    }

    double cofactor(int row, int col) {
        return Math.pow(-1, row + col) * submatrix(row, col).det();
    }

    SquareMatrix submatrix(int rowToSkip, int colToSkip) {
        SquareMatrix submatrix = new SquareMatrix(dim - 1);

        IntStream.range(0, dim - 1).forEach(x ->
            IntStream.range(0, dim - 1).forEach(y ->
                submatrix.set(x, y, this.get(
                    x >= rowToSkip ? x + 1 : x,
                    y >= colToSkip ? y + 1 : y
                ))
            )
        );
        return submatrix;
    }

    @Override
    public ISquareMatrix inverse() {
        SquareMatrix inverted = new SquareMatrix(dim);
        double det = det();
        assert det != 0;

        IntStream.range(0, dim).forEach(row ->
                IntStream.range(0, dim).forEach(col ->
                        inverted.set(col, row, cofactor(row, col) / det)
                )
        );
        return inverted;
    }
}

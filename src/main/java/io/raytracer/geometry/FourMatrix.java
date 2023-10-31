package io.raytracer.geometry;


import java.util.Arrays;
import java.util.stream.IntStream;

public class FourMatrix extends CofactorDetMatrix implements ISquareMatrix {
    private final double[][] entries;
    private final static double equalityTolerance = 1e-3;

    protected static final double[][] id4 = new double[4][4];
    static {
        IntStream.range(0, 4).forEach(i -> id4[i][i] = 1);
    }
    private boolean isIdentity;

    private FourMatrix() {
        entries = new double[4][4];
        isIdentity = false;
    }

    public FourMatrix(double... entries) {
        this.entries = new double[4][4];
        IntStream.range(0, 4).forEach(rowIndex ->
                Arrays.setAll(this.entries[rowIndex], colIndex -> entries[4 * rowIndex + colIndex])
        );
        this.isIdentity();
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(entries);
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;
        FourMatrix themMatrix = (FourMatrix) them;

        double maxEntryDifference = super.maxEntryDifference(4, themMatrix);

        return (maxEntryDifference < equalityTolerance);
    }

    @Override
    public String toString() {
        return "SquareMatrix(" + Arrays.deepToString(this.entries) + ")";
    }

    private void isIdentity() {
        this.isIdentity = Arrays.deepEquals(this.entries, FourMatrix.id4);
    }

    @Override
    public double get(int x, int y) {
        return entries[x][y];
    }

    private void set(int x, int y, double element) {
        entries[x][y] = element;
    }

    private static double dot(double[] a, double[] b) {
        return a[0]*b[0] + a[1]*b[1] + a[2]*b[2] + a[3]*b[3];
    }

    @Override
    public ISquareMatrix multiply(ISquareMatrix them) {
        FourMatrix product = new FourMatrix();
        IntStream.range(0, 4).forEach(y -> {
            double[] theirColumn = new double[] { them.get(0, y), them.get(1, y), them.get(2, y), them.get(3, y) };
            IntStream.range(0, 4).forEach(x ->
                    product.set(x, y, FourMatrix.dot(this.entries[x], theirColumn))
            );
        });
        product.isIdentity();

        return product;
    }

    @Override
    public Tuple4 multiply(Tuple4 them) {
        if (this.isIdentity) {
            return them;
        }
        double[] themArray = new double[] {them.x, them.y, them.z, them.w};
        return new Tuple4(
            FourMatrix.dot(entries[0], themArray),
            FourMatrix.dot(entries[1], themArray),
            FourMatrix.dot(entries[2], themArray),
            FourMatrix.dot(entries[3], themArray)
        );
    }

    @Override
    public ISquareMatrix transpose() {
        if (this.isIdentity) {
            return this;
        }
        FourMatrix transposed = new FourMatrix();
        IntStream.range(0, 4).forEach(i ->
                IntStream.range(0, 4).forEach(j -> transposed.set(i, j, this.get(j, i)))
        );
        return transposed;
    }

    @Override
    public double det() {
        return super.det(4);
    }

    ThreeMatrix submatrix(int rowToSkip, int colToSkip) {
        ThreeMatrix submatrix = new ThreeMatrix();

        IntStream.range(0, 3).forEach(x ->
            IntStream.range(0, 3).forEach(y ->
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
        if (this.isIdentity) {
            return this;
        }
        FourMatrix inverted = new FourMatrix();
        double det = det();
        assert det != 0;

        IntStream.range(0, 4).forEach(row ->
                IntStream.range(0, 4).forEach(col ->
                        inverted.set(col, row, cofactor(row, col) / det)
                )
        );
        return inverted;
    }
}

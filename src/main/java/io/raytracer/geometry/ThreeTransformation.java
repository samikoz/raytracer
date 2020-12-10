package io.raytracer.geometry;

public class ThreeTransformation implements Transformation {
    private final SquareMatrix underlyingMatrix;

    public ThreeTransformation() {
        underlyingMatrix = new SquareMatrixImpl(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        );
    }

    private ThreeTransformation(SquareMatrix m) {
        underlyingMatrix = m;
    }

    static ThreeTransformation translation(double x, double y, double z) {
        return new ThreeTransformation(new SquareMatrixImpl(
                1, 0, 0, x,
                0, 1, 0, y,
                0, 0, 1, z,
                0, 0, 0, 1
        ));
    }

    static ThreeTransformation scaling(double x, double y, double z) {
        return new ThreeTransformation(new SquareMatrixImpl(
                x, 0, 0, 0,
                0, y, 0, 0,
                0, 0, z, 0,
                0, 0, 0, 1
        ));
    }

    static ThreeTransformation rotation_x(double angle) {
        return new ThreeTransformation(new SquareMatrixImpl(
                1, 0, 0, 0,
                0, Math.cos(angle), -Math.sin(angle), 0,
                0, Math.sin(angle), Math.cos(angle), 0,
                0, 0, 0, 1
        ));
    }

    static ThreeTransformation rotation_y(double angle) {
        return new ThreeTransformation(new SquareMatrixImpl(
                Math.cos(angle), 0, Math.sin(angle), 0,
                0, 1, 0, 0,
                - Math.sin(angle), 0, Math.cos(angle), 0,
                0, 0, 0, 1
        ));
    }

    @Override
    public Transformation inverse() {
        return new ThreeTransformation(this.underlyingMatrix.inverse());
    }

    @Override
    public Point act(Point p) {
        Tuple higherTuple = underlyingMatrix.multiply(new PointImpl(p.get(0), p.get(1), p.get(2), 1));
        return new PointImpl(higherTuple.get(0), higherTuple.get(1), higherTuple.get(2));
    }

    @Override
    public Vector act(Vector v) {
        Tuple higherTuple = underlyingMatrix.multiply(new PointImpl(v.get(0), v.get(1), v.get(2), 0));
        return new VectorImpl(higherTuple.get(0), higherTuple.get(1), higherTuple.get(2));
    }
}

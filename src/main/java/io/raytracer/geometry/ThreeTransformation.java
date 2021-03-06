package io.raytracer.geometry;

import lombok.ToString;

@ToString
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

    public static ThreeTransformation translation(double x, double y, double z) {
        return new ThreeTransformation(new SquareMatrixImpl(
                1, 0, 0, x,
                0, 1, 0, y,
                0, 0, 1, z,
                0, 0, 0, 1
        ));
    }

    public ThreeTransformation translate(double x, double y, double z) {
        return new ThreeTransformation(new SquareMatrixImpl(
                1, 0, 0, x,
                0, 1, 0, y,
                0, 0, 1, z,
                0, 0, 0, 1
        ).multiply(this.underlyingMatrix));
    }

    public static ThreeTransformation scaling(double x, double y, double z) {
        return new ThreeTransformation(new SquareMatrixImpl(
                x, 0, 0, 0,
                0, y, 0, 0,
                0, 0, z, 0,
                0, 0, 0, 1
        ));
    }

    public ThreeTransformation scale(double x, double y, double z) {
        return new ThreeTransformation(new SquareMatrixImpl(
                x, 0, 0, 0,
                0, y, 0, 0,
                0, 0, z, 0,
                0, 0, 0, 1
        ).multiply(this.underlyingMatrix));
    }

    public static ThreeTransformation rotation_x(double angle) {
        return new ThreeTransformation(new SquareMatrixImpl(
                1, 0, 0, 0,
                0, Math.cos(angle), -Math.sin(angle), 0,
                0, Math.sin(angle), Math.cos(angle), 0,
                0, 0, 0, 1
        ));
    }

    public ThreeTransformation rotate_x(double angle) {
        return new ThreeTransformation(new SquareMatrixImpl(
                1, 0, 0, 0,
                0, Math.cos(angle), -Math.sin(angle), 0,
                0, Math.sin(angle), Math.cos(angle), 0,
                0, 0, 0, 1
        ).multiply(this.underlyingMatrix));
    }

    public static ThreeTransformation rotation_y(double angle) {
        return new ThreeTransformation(new SquareMatrixImpl(
                Math.cos(angle), 0, Math.sin(angle), 0,
                0, 1, 0, 0,
                -Math.sin(angle), 0, Math.cos(angle), 0,
                0, 0, 0, 1
        ));
    }

    public ThreeTransformation rotate_y(double angle) {
        return new ThreeTransformation(new SquareMatrixImpl(
                Math.cos(angle), 0, Math.sin(angle), 0,
                0, 1, 0, 0,
                -Math.sin(angle), 0, Math.cos(angle), 0,
                0, 0, 0, 1
        ).multiply(this.underlyingMatrix));
    }

    public static ThreeTransformation rotation_z(double angle) {
        return new ThreeTransformation(new SquareMatrixImpl(
                Math.cos(angle), -Math.sin(angle), 0, 0,
                Math.sin(angle), Math.cos(angle), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        ));
    }

    public ThreeTransformation rotate_z(double angle) {
        return new ThreeTransformation(new SquareMatrixImpl(
                Math.cos(angle), -Math.sin(angle), 0, 0,
                Math.sin(angle), Math.cos(angle), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        ).multiply(this.underlyingMatrix));
    }

    public static ThreeTransformation shear(double xy, double xz, double yx, double yz, double zx, double zy) {
        return new ThreeTransformation(new SquareMatrixImpl(
           1, xy, xz, 0,
           yx, 1, yz, 0,
           zx, zy, 1, 0,
           0, 0, 0, 1
        ));
    }

    public ThreeTransformation doShear(double xy, double xz, double yx, double yz, double zx, double zy) {
        return new ThreeTransformation(new SquareMatrixImpl(
                1, xy, xz, 0,
                yx, 1, yz, 0,
                zx, zy, 1, 0,
                0, 0, 0, 1
        ).multiply(this.underlyingMatrix));
    }
    
    public static ThreeTransformation transformation(SquareMatrix m) {
        return new ThreeTransformation(m);
    }
    
    public ThreeTransformation transform(SquareMatrix m) {
        return new ThreeTransformation(m.multiply(this.underlyingMatrix));
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        ThreeTransformation themTransformation = (ThreeTransformation) them;
        return this.underlyingMatrix.equals(themTransformation.underlyingMatrix);
    }

    @Override
    public int hashCode() {
        return this.underlyingMatrix.hashCode();
    }

    @Override
    public Transformation inverse() {
        return new ThreeTransformation(this.underlyingMatrix.inverse());
    }

    @Override
    public Transformation transpose() {
        return new ThreeTransformation(this.underlyingMatrix.transpose());
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

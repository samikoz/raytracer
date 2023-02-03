package io.raytracer.geometry;

import lombok.ToString;

@ToString
public class ThreeTransform implements ITransform {
    private final ISquareMatrix underlyingMatrix;

    public ThreeTransform() {
        underlyingMatrix = new SquareMatrix(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        );
    }

    private ThreeTransform(ISquareMatrix m) {
        underlyingMatrix = m;
    }

    public static ThreeTransform translation(double x, double y, double z) {
        return new ThreeTransform(new SquareMatrix(
                1, 0, 0, x,
                0, 1, 0, y,
                0, 0, 1, z,
                0, 0, 0, 1
        ));
    }

    public ThreeTransform translate(double x, double y, double z) {
        return new ThreeTransform(new SquareMatrix(
                1, 0, 0, x,
                0, 1, 0, y,
                0, 0, 1, z,
                0, 0, 0, 1
        ).multiply(this.underlyingMatrix));
    }

    public static ThreeTransform scaling(double x, double y, double z) {
        return new ThreeTransform(new SquareMatrix(
                x, 0, 0, 0,
                0, y, 0, 0,
                0, 0, z, 0,
                0, 0, 0, 1
        ));
    }

    public ThreeTransform scale(double x, double y, double z) {
        return new ThreeTransform(new SquareMatrix(
                x, 0, 0, 0,
                0, y, 0, 0,
                0, 0, z, 0,
                0, 0, 0, 1
        ).multiply(this.underlyingMatrix));
    }

    public static ThreeTransform rotation_x(double angle) {
        return new ThreeTransform(new SquareMatrix(
                1, 0, 0, 0,
                0, Math.cos(angle), -Math.sin(angle), 0,
                0, Math.sin(angle), Math.cos(angle), 0,
                0, 0, 0, 1
        ));
    }

    public ThreeTransform rotate_x(double angle) {
        return new ThreeTransform(new SquareMatrix(
                1, 0, 0, 0,
                0, Math.cos(angle), -Math.sin(angle), 0,
                0, Math.sin(angle), Math.cos(angle), 0,
                0, 0, 0, 1
        ).multiply(this.underlyingMatrix));
    }

    public static ThreeTransform rotation_y(double angle) {
        return new ThreeTransform(new SquareMatrix(
                Math.cos(angle), 0, Math.sin(angle), 0,
                0, 1, 0, 0,
                -Math.sin(angle), 0, Math.cos(angle), 0,
                0, 0, 0, 1
        ));
    }

    public ThreeTransform rotate_y(double angle) {
        return new ThreeTransform(new SquareMatrix(
                Math.cos(angle), 0, Math.sin(angle), 0,
                0, 1, 0, 0,
                -Math.sin(angle), 0, Math.cos(angle), 0,
                0, 0, 0, 1
        ).multiply(this.underlyingMatrix));
    }

    public static ThreeTransform rotation_z(double angle) {
        return new ThreeTransform(new SquareMatrix(
                Math.cos(angle), -Math.sin(angle), 0, 0,
                Math.sin(angle), Math.cos(angle), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        ));
    }

    public ThreeTransform rotate_z(double angle) {
        return new ThreeTransform(new SquareMatrix(
                Math.cos(angle), -Math.sin(angle), 0, 0,
                Math.sin(angle), Math.cos(angle), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        ).multiply(this.underlyingMatrix));
    }

    public static ThreeTransform shear(double xy, double xz, double yx, double yz, double zx, double zy) {
        return new ThreeTransform(new SquareMatrix(
           1, xy, xz, 0,
           yx, 1, yz, 0,
           zx, zy, 1, 0,
           0, 0, 0, 1
        ));
    }

    public ThreeTransform doShear(double xy, double xz, double yx, double yz, double zx, double zy) {
        return new ThreeTransform(new SquareMatrix(
                1, xy, xz, 0,
                yx, 1, yz, 0,
                zx, zy, 1, 0,
                0, 0, 0, 1
        ).multiply(this.underlyingMatrix));
    }
    
    public static ThreeTransform transformation(ISquareMatrix m) {
        return new ThreeTransform(m);
    }
    
    public ThreeTransform transform(ISquareMatrix m) {
        return new ThreeTransform(m.multiply(this.underlyingMatrix));
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        ThreeTransform themTransformation = (ThreeTransform) them;
        return this.underlyingMatrix.equals(themTransformation.underlyingMatrix);
    }

    @Override
    public int hashCode() {
        return this.underlyingMatrix.hashCode();
    }

    @Override
    public ITransform inverse() {
        return new ThreeTransform(this.underlyingMatrix.inverse());
    }

    @Override
    public ITransform transpose() {
        return new ThreeTransform(this.underlyingMatrix.transpose());
    }

    @Override
    public IPoint act(IPoint p) {
        ITuple higherTuple = underlyingMatrix.multiply(new Point(p.get(0), p.get(1), p.get(2), 1));
        return new Point(higherTuple.get(0), higherTuple.get(1), higherTuple.get(2));
    }

    @Override
    public IVector act(IVector v) {
        ITuple higherTuple = underlyingMatrix.multiply(new Point(v.get(0), v.get(1), v.get(2), 0));
        return new Vector(higherTuple.get(0), higherTuple.get(1), higherTuple.get(2));
    }
}

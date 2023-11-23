package io.raytracer.geometry;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ThreeTransform implements ITransform {
    private final ISquareMatrix matrix;


    public ThreeTransform() {
        matrix = new FourMatrix(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        );
    }

    private ThreeTransform(ISquareMatrix m) {
        matrix = m;
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        ThreeTransform themTransformation = (ThreeTransform) them;
        return this.matrix.equals(themTransformation.matrix);
    }

    @Override
    public int hashCode() {
        return this.matrix.hashCode();
    }

    public static ThreeTransform translation(double x, double y, double z) {
        return new ThreeTransform(new FourMatrix(
                1, 0, 0, x,
                0, 1, 0, y,
                0, 0, 1, z,
                0, 0, 0, 1
        ));
    }

    public ThreeTransform translate(double x, double y, double z) {
        return new ThreeTransform(new FourMatrix(
                1, 0, 0, x,
                0, 1, 0, y,
                0, 0, 1, z,
                0, 0, 0, 1
        ).multiply(this.matrix));
    }

    public static ThreeTransform scaling(double x, double y, double z) {
        return new ThreeTransform(new FourMatrix(
                x, 0, 0, 0,
                0, y, 0, 0,
                0, 0, z, 0,
                0, 0, 0, 1
        ));
    }

    public static ThreeTransform scaling(double x) {
        return new ThreeTransform(new FourMatrix(
                x, 0, 0, 0,
                0, x, 0, 0,
                0, 0, x, 0,
                0, 0, 0, 1
        ));
    }

    public ThreeTransform scale(double x, double y, double z) {
        return new ThreeTransform(new FourMatrix(
                x, 0, 0, 0,
                0, y, 0, 0,
                0, 0, z, 0,
                0, 0, 0, 1
        ).multiply(this.matrix));
    }

    public ThreeTransform scale(double x) {
        return new ThreeTransform(new FourMatrix(
                x, 0, 0, 0,
                0, x, 0, 0,
                0, 0, x, 0,
                0, 0, 0, 1
            ).multiply(this.matrix)
        );
    }

    public static ThreeTransform rotation_x(double angle) {
        return new ThreeTransform(new FourMatrix(
                1, 0, 0, 0,
                0, Math.cos(angle), -Math.sin(angle), 0,
                0, Math.sin(angle), Math.cos(angle), 0,
                0, 0, 0, 1
        ));
    }

    public static ThreeTransform rotation_x(double angle, IPoint around) {
        return ThreeTransform.rotation_x(angle).conjugateTranslating(around);
    }

    public ThreeTransform rotate_x(double angle) {
        return new ThreeTransform(new FourMatrix(
                1, 0, 0, 0,
                0, Math.cos(angle), -Math.sin(angle), 0,
                0, Math.sin(angle), Math.cos(angle), 0,
                0, 0, 0, 1
        ).multiply(this.matrix));
    }

    public ThreeTransform rotate_x(double angle, IPoint around) {
        return this.rotate_x(angle).conjugateTranslating(around);
    }

    public static ThreeTransform rotation_y(double angle) {
        return new ThreeTransform(new FourMatrix(
                Math.cos(angle), 0, Math.sin(angle), 0,
                0, 1, 0, 0,
                -Math.sin(angle), 0, Math.cos(angle), 0,
                0, 0, 0, 1
        ));
    }

    public static ThreeTransform rotation_y(double angle, IPoint around) {
        return ThreeTransform.rotation_y(angle).conjugateTranslating(around);
    }

    public ThreeTransform rotate_y(double angle) {
        return new ThreeTransform(new FourMatrix(
                Math.cos(angle), 0, Math.sin(angle), 0,
                0, 1, 0, 0,
                -Math.sin(angle), 0, Math.cos(angle), 0,
                0, 0, 0, 1
        ).multiply(this.matrix));
    }

    public ThreeTransform rotate_y(double angle, IPoint around) {
        return this.rotate_y(angle).conjugateTranslating(around);
    }

    public static ThreeTransform rotation_z(double angle) {
        return new ThreeTransform(new FourMatrix(
                Math.cos(angle), -Math.sin(angle), 0, 0,
                Math.sin(angle), Math.cos(angle), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        ));
    }

    public static ThreeTransform rotation_z(double angle, IPoint around) {
        return ThreeTransform.rotation_z(angle).conjugateTranslating(around);
    }

    public ThreeTransform rotate_z(double angle) {
        return new ThreeTransform(new FourMatrix(
                Math.cos(angle), -Math.sin(angle), 0, 0,
                Math.sin(angle), Math.cos(angle), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        ).multiply(this.matrix));
    }

    public ThreeTransform rotate_z(double angle, IPoint around) {
        return this.rotate_z(angle).conjugateTranslating(around);
    }

    public static ThreeTransform shear(double xy, double xz, double yx, double yz, double zx, double zy) {
        return new ThreeTransform(new FourMatrix(
           1, xy, xz, 0,
           yx, 1, yz, 0,
           zx, zy, 1, 0,
           0, 0, 0, 1
        ));
    }

    public ThreeTransform doShear(double xy, double xz, double yx, double yz, double zx, double zy) {
        return new ThreeTransform(new FourMatrix(
                1, xy, xz, 0,
                yx, 1, yz, 0,
                zx, zy, 1, 0,
                0, 0, 0, 1
        ).multiply(this.matrix));
    }

    public ThreeTransform conjugateTranslating(IPoint pointToOrigin) {
        return ThreeTransform.translation(-pointToOrigin.x(), -pointToOrigin.y(), -pointToOrigin.z())
                .transform(this)
                .translate(pointToOrigin.x(), pointToOrigin.y(), pointToOrigin.z());
    }
    
    public ThreeTransform transform(ISquareMatrix m) {
        return new ThreeTransform(m.multiply(this.matrix));
    }

    public ThreeTransform transform(ITransform t) {
        return this.transform(t.getMatrix());
    }

    @Override
    public ITransform inverse() {
        return new ThreeTransform(this.matrix.inverse());
    }

    @Override
    public ITransform transpose() {
        return new ThreeTransform(this.matrix.transpose());
    }

    @Override
    public IPoint act(IPoint p) {
        Tuple4 higherTuple = matrix.multiply(new Tuple4(p.x(), p.y(), p.z(), 1));
        return new Point(higherTuple.x, higherTuple.y, higherTuple.z);
    }

    @Override
    public IVector act(IVector v) {
        Tuple4 higherTuple = matrix.multiply(new Tuple4(v.x(), v.y(), v.z(), 0));
        return new Vector(higherTuple.x, higherTuple.y, higherTuple.z);
    }
}

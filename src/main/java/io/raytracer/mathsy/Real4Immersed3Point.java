package io.raytracer.mathsy;

public class Real4Immersed3Point extends RealPoint implements TransformablePoint {
    public Real4Immersed3Point(double x, double y, double z) {
        super(x, y, z, 1);
    }

    private Real4Immersed3Point(Tuple them) {
        this(them.get(0), them.get(1), them.get(2));

        assert them.get(3) == 1;
    }

    private Real4Immersed3Point transform(Matrix operator) {
        return new Real4Immersed3Point(operator.multiply(this));
    }

    @Override
    public Real4Immersed3Point translate(Vector direction) {
        assert direction.dim() == 3;

        return transform(new RealSquareMatrix(
            1, 0, 0, direction.get(0),
            0, 1, 0, direction.get(1),
            0, 0, 1, direction.get(2),
            0, 0, 0, 1
        ));
    }
}

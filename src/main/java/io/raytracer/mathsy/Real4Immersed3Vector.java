package io.raytracer.mathsy;

public class Real4Immersed3Vector extends RealVector implements ThreeVector, TransformableVector {
    public Real4Immersed3Vector(double x, double y, double z) {
        super(x, y, z, 0);
    }

    @Override
    public Real4Immersed3Vector cross(ThreeVector them) {
        assert this.dim() == them.dim();

        return new Real4Immersed3Vector(
                this.get(1)*them.get(2) - this.get(2)*them.get(1),
                this.get(2)*them.get(0) - this.get(0)*them.get(2),
                this.get(0)*them.get(1) - this.get(1)*them.get(0)
        );
    }

    @Override
    public Real4Immersed3Vector translate(Vector direction) {
        return this;
    }
}

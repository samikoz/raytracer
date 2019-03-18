package io.raytracer.mathsy;

public class Real3Vector extends RealVector implements ThreeVector{
    public Real3Vector(double x, double y, double z) {
        super(x, y, z);
    }

    @Override
    public Real3Vector cross(ThreeVector them) {
        return new Real3Vector(
            this.get(1)*them.get(2) - this.get(2)*them.get(1),
            this.get(2)*them.get(0) - this.get(0)*them.get(2),
            this.get(0)*them.get(1) - this.get(1)*them.get(0)
        );
    }
}

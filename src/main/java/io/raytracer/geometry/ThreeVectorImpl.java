package io.raytracer.geometry;

public class ThreeVectorImpl extends VectorImpl implements ThreeVector {
    public ThreeVectorImpl(double x, double y, double z) {
        super(x, y, z);
    }

    @Override
    public ThreeVectorImpl cross(ThreeVector them) {
        return new ThreeVectorImpl(
                this.get(1) * them.get(2) - this.get(2) * them.get(1),
                this.get(2) * them.get(0) - this.get(0) * them.get(2),
                this.get(0) * them.get(1) - this.get(1) * them.get(0)
        );
    }
}

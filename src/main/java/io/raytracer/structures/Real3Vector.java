package io.raytracer.structures;

public final class Real3Vector extends Real3Tuple implements Vector {
    public Real3Vector(double x, double y, double z) {
        super(x, y, z);
    }

    Real3Vector(Tuple tuple) {
        this(tuple.getX(), tuple.getY(), tuple.getZ());
    }

    @Override
    public double distance(Vector them) {
        return super.distance(them);
    }

    @Override
    public Real3Vector add(Vector them) {
        return new Real3Vector(
            this.getX() + them.getX(),
            this.getY() + them.getY(),
            this.getZ() + them.getZ()
        );
    }

    @Override
    public Real3Vector subtract(Vector them) {
        return new Real3Vector(super.subtract(them));
    }

    @Override
    public Real3Vector negate() {
        return new Real3Vector(new Real3Vector(0.0, 0.0, 0.0).subtract(this));
    }
}

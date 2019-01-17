package io.raytracer.mathsy;

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
    public double norm() {
        return super.distance(new Real3Vector(0,0,0));
    }

    @Override
    public Vector normalise() {
        return this.multiply(1/this.norm());
    }

    @Override
    public Real3Vector add(Vector them) {
        return new Real3Vector(super.add(them));
    }

    @Override
    public Real3Vector subtract(Vector them) {
        return new Real3Vector(super.subtract(them));
    }

    @Override
    public Real3Vector negate() {
        return new Real3Vector(0, 0, 0).subtract(this);
    }

    @Override
    public Vector multiply(double scalar) {
        return new Real3Vector(super.multiply(scalar));
    }

    @Override
    public double dot(Vector them) {
        return this.getX()*them.getX() + this.getY()*them.getY() + this.getZ()*them.getZ();
    }

    @Override
    public Vector cross(Vector them) {
        return new Real3Vector(
            this.getY()*them.getZ() - this.getZ()*them.getY(),
            this.getZ()*them.getX() - this.getX()*them.getZ(),
            this.getX()*them.getY() - this.getY()*them.getX()
        );
    }
}

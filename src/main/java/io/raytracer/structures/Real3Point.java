package io.raytracer.structures;

public final class Real3Point extends Real3Tuple implements Point {
    public Real3Point(double x, double y, double z) {
        super(x, y, z);
    }

    Real3Point(Tuple tuple) {
        this(tuple.getX(), tuple.getY(), tuple.getZ());
    }

    @Override
    public double distance(Point them) {
        return super.distance(them);
    }

    @Override
    public Real3Point add(Vector displacement) {
        return new Real3Point(super.add(displacement));
    }

    @Override
    public Real3Vector subtract(Point them) {
        return new Real3Vector(super.subtract(them));
    }

    @Override
    public Real3Point subtract(Vector displacement) {
        return new Real3Point(super.subtract(displacement));
    }
}
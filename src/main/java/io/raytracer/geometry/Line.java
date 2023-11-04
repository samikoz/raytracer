package io.raytracer.geometry;

public class Line implements ILine {
    public final IPoint origin;
    public final IVector direction;

    public Line(IPoint from, IVector direction) {
        this.origin = from;
        this.direction = direction;
    }

    public Line(IPoint from, IPoint to) {
        this.origin = from;
        this.direction = to.subtract(from);
    }

    @Override
    public IPoint pointAt(double t) {
        return this.origin.add(this.direction.multiply(t));
    }
}

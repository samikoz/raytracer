package io.raytracer.geometry;

import lombok.NonNull;

import java.util.Optional;

public class Point extends Tuple implements IPoint {
    public Point(double x, double y, double z) {
        super(x, y, z);
    }

    private Point(ITuple them) {
        this(them.x(), them.y(), them.z());
    }

    @Override
    public double distance(IPoint them) {
        return euclideanDistance(them);
    }

    @Override
    public Point multiply(double scalar) {
        return new Point(super.multiply(scalar));
    }

    @Override
    public Point add(IVector them) {
        return new Point(super.add(them));
    }

    @Override
    public IVector subtract(@NonNull IPoint toSubtract) {
        return new Vector(this.x() - toSubtract.x(), this.y() - toSubtract.y(), this.z() - toSubtract.z());
    }

    @Override
    public IPoint transform(ITransform t) {
        return t.act(this);
    }

    @Override
    public Optional<IPoint> project(IPlane onto, IPoint through) {
        ILine lineThrough = new Line(this, through);
        return onto.intersect(lineThrough);
    }

    @Override
    public String toString() {
        return String.format("Point(%f,%f,%f)", x(), y(), z());
    }
}

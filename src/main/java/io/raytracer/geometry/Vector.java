package io.raytracer.geometry;

public class Vector extends Tuple implements IVector {
    public Vector(double x, double y, double z) {
        super(x, y, z);
    }

    Vector(double[] coords) {
        this(coords[0], coords[1], coords[2]);
    }

    Vector(ITuple them) {
        this(them.x(), them.y(), them.z());
    }

    @Override
    public Vector multiply(double scalar) {
        return new Vector(super.multiply(scalar));
    }

    @Override
    public Vector add(IVector them) {
        return new Vector(super.add(them));
    }

    @Override
    public double distance(IVector them) {
        return euclideanDistance(them);
    }

    @Override
    public double norm() {
        return distance(new Vector(0, 0, 0));
    }

    @Override
    public Vector normalise() {
        return new Vector(this.multiply(1 / this.norm()));
    }

    @Override
    public double dot(IVector them) {
        return this.x()*them.x() + this.y()*them.y() + this.z()*them.z();
    }

    @Override
    public Vector cross(IVector them) {
        return new Vector(
                this.y() * them.z() - this.z() * them.y(),
                this.z() * them.x() - this.x() * them.z(),
                this.x() * them.y() - this.y() * them.x()
        );
    }

    @Override
    public IVector reflect(IVector normal) {
        return this.subtract(normal.multiply(2 * this.dot(normal)));
    }

    @Override
    public IVector transform(ITransform t) {
        return t.act(this);
    }
}

package io.raytracer.geometry;

public interface IVector extends ITuple {
    double distance(IVector them);

    IVector multiply(double scalar);
    default IVector negate() {
        return multiply(-1);
    }

    double norm();
    IVector normalise();

    IVector add(IVector them);
    default IVector subtract(IVector them) {
        return add(them.negate());
    }

    IVector reflect(IVector normal);

    double dot(IVector them);
    IVector cross(IVector them);

    IVector transform(ITransform t);
}

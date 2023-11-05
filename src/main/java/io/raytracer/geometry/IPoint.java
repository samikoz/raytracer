package io.raytracer.geometry;

public interface IPoint extends ITuple {
    double distance(IPoint them);

    IPoint multiply(double scalar);

    IPoint add(IVector displacement);
    default IPoint subtract(IVector displacement) {
        return add(displacement.multiply(-1));
    }

    IVector subtract(IPoint them);

    IPoint transform(ITransform t);

    IPoint project(IPlane onto, IPoint through);
}
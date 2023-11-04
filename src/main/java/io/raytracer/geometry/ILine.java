package io.raytracer.geometry;

public interface ILine {
    IVector getDirection();
    IPoint getOrigin();

    IPoint pointAt(double t);

    double intersect(IPlane plane);

    IPoint closestTo(IPoint p);
}

package io.raytracer.geometry;

import java.util.Optional;

public interface ILine {
    IVector getDirection();
    IPoint getOrigin();

    IPoint pointAt(double t);

    Optional<Double> intersect(IPlane plane);

    IPoint closestTo(IPoint p);
}

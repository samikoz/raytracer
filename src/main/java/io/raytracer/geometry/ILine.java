package io.raytracer.geometry;

public interface ILine {
    IPoint pointAt(double t);

    IPoint closestTo(IPoint p);
}

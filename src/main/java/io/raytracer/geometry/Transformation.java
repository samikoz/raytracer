package io.raytracer.geometry;

public interface Transformation {
    Transformation inverse();
    Transformation transpose();

    Point act(Point p);
    Vector act(Vector v);
}

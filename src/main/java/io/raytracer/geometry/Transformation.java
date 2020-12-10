package io.raytracer.geometry;

public interface Transformation {
    Transformation inverse();

    Point act(Point p);
    Vector act(Vector v);
}

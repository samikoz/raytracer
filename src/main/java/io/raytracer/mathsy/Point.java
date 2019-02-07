package io.raytracer.mathsy;

public interface Point {
    double get(int coordinate);

    double distance(Point them);

    Point add(Vector displacement);

    Vector subtract(Point them);
    Point subtract(Vector displacement);
}
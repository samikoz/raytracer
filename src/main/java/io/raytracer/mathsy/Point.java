package io.raytracer.mathsy;

public interface Point {
    double getX();
    double getY();
    double getZ();

    double distance(Point them);

    Point add(Vector displacement);

    Vector subtract(Point them);
    Point subtract(Vector displacement);
}
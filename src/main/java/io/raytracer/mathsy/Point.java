package io.raytracer.mathsy;

public interface Point extends Tuple {
    double distance(Point them);

    Point multiply(double scalar);

    Point add(Vector displacement);

    Vector subtract(Point them);
    Point subtract(Vector displacement);
}
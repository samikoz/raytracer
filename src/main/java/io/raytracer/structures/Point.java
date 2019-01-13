package io.raytracer.structures;

public interface Point extends Tuple {
    double distance(Point them);

    Point add(Vector displacement);

    Vector subtract(Point them);
    Point subtract(Vector displacement);
}
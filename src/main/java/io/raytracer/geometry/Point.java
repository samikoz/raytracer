package io.raytracer.geometry;

public interface Point extends Tuple {
    double distance(Point them);

    Point multiply(double scalar);

    Point add(Vector displacement);
    default Point subtract(Vector displacement) {
        return add(displacement.multiply(-1));
    }

    Vector subtract(Point them);
}
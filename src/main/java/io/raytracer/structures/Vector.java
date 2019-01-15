package io.raytracer.structures;

public interface Vector extends Tuple {
    double distance(Vector them);

    double norm();

    Vector normalise();

    Vector add(Vector them);

    Vector subtract(Vector them);

    Vector negate();

    Vector multiply(double scalar);

    double dot(Vector them);
}

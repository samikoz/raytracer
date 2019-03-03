package io.raytracer.mathsy;

public interface Vector extends Tuple{
    double distance(Vector them);

    Vector multiply(double scalar);
    Vector negate();

    double norm();
    Vector normalise();

    Vector add(Vector them);
    Vector subtract(Vector them);

    double dot(Vector them);
}

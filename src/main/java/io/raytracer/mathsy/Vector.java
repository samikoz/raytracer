package io.raytracer.mathsy;

public interface Vector {
    double getX();
    double getY();
    double getZ();

    double distance(Vector them);

    double norm();
    Vector normalise();

    Vector add(Vector them);
    Vector subtract(Vector them);
    Vector negate();
    Vector multiply(double scalar);

    double dot(Vector them);
    Vector cross(Vector them);
}

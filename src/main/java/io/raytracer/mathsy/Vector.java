package io.raytracer.mathsy;

public interface Vector {
    int dim();
    double get(int coordinate);
    double[] toArray();

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

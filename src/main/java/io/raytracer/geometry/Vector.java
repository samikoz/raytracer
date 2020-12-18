package io.raytracer.geometry;

public interface Vector extends Tuple{
    double distance(Vector them);

    Vector multiply(double scalar);
    default Vector negate() {
        return multiply(-1);
    }

    double norm();
    Vector normalise();

    Vector add(Vector them);
    default Vector subtract(Vector them) {
        return add(them.negate());
    }

    Vector reflect(Vector reflector);

    double dot(Vector them);

    Vector transform(Transformation t);
}

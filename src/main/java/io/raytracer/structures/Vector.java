package io.raytracer.structures;

public interface Vector extends Tuple {
    double distance(Vector them);

    Vector add(Vector them);

    Vector subtract(Vector them);

    Vector negate();
}

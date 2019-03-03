package io.raytracer.mathsy;

public interface Tuple {
    int dim();
    double get(int coordinate);

    Tuple multiply(double scalar);
    Tuple negate();
}

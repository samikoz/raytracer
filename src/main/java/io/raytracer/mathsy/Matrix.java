package io.raytracer.mathsy;

public interface Matrix {
    int dim();
    double get(int x, int y);

    Matrix multiply(Matrix them);
    Tuple multiply(Tuple them);

    Matrix transpose();

    double det();
    boolean isInvertible();

    Matrix inverse();
}

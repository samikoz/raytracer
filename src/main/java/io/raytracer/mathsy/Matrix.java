package io.raytracer.mathsy;

public interface Matrix {
    int dim();
    double get(int x, int y);

    Matrix multiply(Matrix them);
    Tuple multiply(Tuple them);

    Matrix transpose();

    double det();
    default boolean isInvertible() {
        return det() != 0;
    }

    Matrix inverse();
}

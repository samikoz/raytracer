package io.raytracer.mathsy;

public interface Matrix {
    double get(int x, int y);

    Matrix multiply(Matrix them);
}

package io.raytracer.mathsy;

public interface Matrix {
    int dim();
    double get(int x, int y);

    Matrix multiply(Matrix them);
    Vector multiply(Vector them);
}

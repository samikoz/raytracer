package io.raytracer.geometry;

public interface ISquareMatrix {
    int dim();

    double get(int x, int y);

    ISquareMatrix multiply(ISquareMatrix them);
    ITuple multiply(ITuple them);

    ISquareMatrix transpose();

    double det();
    default boolean isInvertible() {
        return det() != 0;
    }

    ISquareMatrix inverse();
}

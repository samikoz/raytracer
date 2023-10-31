package io.raytracer.geometry;

public interface ISquareMatrix {

    double get(int x, int y);

    ISquareMatrix multiply(ISquareMatrix them);
    Tuple4 multiply(Tuple4 them);

    ISquareMatrix transpose();

    double det();
    default boolean isInvertible() {
        return det() != 0;
    }

    ISquareMatrix inverse();
}

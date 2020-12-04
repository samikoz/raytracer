package io.raytracer.mathsy;

public interface SquareMatrix {
    int dim();
    double get(int x, int y);

    SquareMatrix multiply(SquareMatrix them);
    Tuple multiply(Tuple them);

    SquareMatrix transpose();

    double det();
    default boolean isInvertible() {
        return det() != 0;
    }

    SquareMatrix inverse();
}

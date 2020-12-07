package io.raytracer.geometry.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.raytracer.geometry.SquareMatrix;

public class MatrixComparator {
    public static void compareMatrices(SquareMatrix expected, SquareMatrix actual) {
        assertEquals(expected.dim(), actual.dim(), "Matrix dimensions should be the same");

        for (int x = 0; x < expected.dim(); x++) {
            for (int y = 0; y < expected.dim(); y++) {
                assertEquals(expected.get(x, y), actual.get(x, y), 1e-3,
                        "(" + x + "," + y + ")-coordinates should be equal"
                );
            }
        }
    }
}
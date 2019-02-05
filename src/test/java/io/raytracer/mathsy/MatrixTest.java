package io.raytracer.mathsy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {

    @Test
    void correctEntryAccess() {
        Matrix m = new RealSquareMatrix(
            -3, -5, 0,
            1, -2, -7,
            0, 1, 1
        );
        assertAll(
            "Matrix indices should refer to row and column number respectively",
            () -> assertEquals(0, m.get(0, 2)),
            () -> assertEquals(1, m.get(1, 0))
        );
    }

    @Test
    void equalityOfMatrices() {
        Matrix first = new RealSquareMatrix(0.0, -2.0, 1e-4, 12);
        Matrix second = new RealSquareMatrix(0.0, -2.0, 0.0, 12);
        Matrix third = new RealSquareMatrix(0, -2, -2, 12, 1, 1, 1, 1, 1);
        Matrix fourth = new RealSquareMatrix(0, 0, 0, 0);

        assertAll(
            "Equality of matrices should be coordinate-wise up to a small factor",
            () -> assertEquals(first, second, "Equality of matrices should be up to a small delta"),
            () -> assertNotEquals(second, third, "Matrices of different dimensions should be different"),
            () -> assertNotEquals(second, fourth, "Should not give false equality for same dimensions")
        );
    }

    @Test
    void multiply() {
        Matrix first = new RealSquareMatrix(
            1, 2, 3, 4,
            5, 6, 7, 8,
            9, 8, 7, 6,
            5, 4, 3, 2
        );
        Matrix second = new RealSquareMatrix(
            -2, 1, 2, 3,
            3, 2, 1, -1,
            4, 3, 6, 5,
            1, 2, 7, 8
        );
        Matrix product = first.multiply(second);
        Matrix expectedProduct = new RealSquareMatrix(
            20, 22, 50, 48,
            44, 54, 114, 108,
            40, 58, 110, 102,
            16, 26, 46, 42
        );

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                assertEquals(
                    product.get(x, y),
                    expectedProduct.get(x, y),
                    "(" + x + "," + y + ")-coordinates should be equal"
                );
            }
        }
    }
}
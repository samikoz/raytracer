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

        assertAll(
            "Equality of matrices should be coordinate-wise up to a small factor",
            () -> assertEquals(first, second, "Equality of matrices should be up to a small delta"),
            () -> assertNotEquals(second, third, "Matrices of different dimensions should be different")
        );
    }
}
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
}
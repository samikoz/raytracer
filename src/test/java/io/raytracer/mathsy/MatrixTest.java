package io.raytracer.mathsy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {
    private static void compareMatrices(Matrix expected, Matrix actual) {
        assertEquals(expected.dim(), actual.dim(), "Matrix dimensions should be the same");
        for (int x = 0; x < expected.dim(); x++) {
            for (int y = 0; y < expected.dim(); y++) {
                assertEquals(
                    expected.get(x, y),
                    actual.get(x, y),
                    "(" + x + "," + y + ")-coordinates should be equal"
                );
            }
        }
    }

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
    void matrixMultiplication() {
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

        compareMatrices(expectedProduct, product);
    }

    @Test
    void multiplicationByVector() {
        Matrix A = new RealSquareMatrix(
            1, 2, 3,
            2, 4, 4,
            8, 6, 4
        );
        Vector x = new RealTuple(1, 2, 3);
        Vector multiplied = A.multiply(x);
        Vector expectedProduct = new RealTuple(14, 22, 32);

        assertEquals(
            expectedProduct,
            multiplied,
            "Product of a matrix and a vector should be a vector. " +
                TupleComparator.compareCoordinates(expectedProduct, multiplied));
    }

    @Test
    void multiplicationByIdentityMatrix() {
        Matrix id = RealSquareMatrix.id(4);
        Matrix A = new RealSquareMatrix(
            0, 1, 2, 4,
            1, 2, 4, 8,
            2, 4, 8, 16,
            4, 8, 16, 32
        );

        compareMatrices(A, id.multiply(A));
    }

    @Test
    void transpose() {
        Matrix A = new RealSquareMatrix(
            0, 9, 3, 0,
            9, 8, 0, 8,
            1, 8, 5, 3,
            0, 0, 5, 8
        );
        Matrix transposed = A.transpose();
        Matrix expectedTranspose = new RealSquareMatrix(
            0, 9, 1, 0,
            9, 8, 8, 0,
            3, 0, 5, 5,
            0, 8, 3, 8
        );

        compareMatrices(expectedTranspose, transposed);
    }
}
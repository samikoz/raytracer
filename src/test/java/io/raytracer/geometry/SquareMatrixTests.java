package io.raytracer.geometry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SquareMatrixTest {
    private static void compareMatrices(SquareMatrix expected, SquareMatrix actual) {
        assertEquals(expected.dim(), actual.dim(), "Matrix dimensions should be the same");

        for (int x = 0; x < expected.dim(); x++) {
            for (int y = 0; y < expected.dim(); y++) {
                assertEquals(
                    expected.get(x, y),
                    actual.get(x, y),
                    1e-3,
                    "(" + x + "," + y + ")-coordinates should be equal"
                );
            }
        }
    }

    @Test
    void correctEntryAccess() {
        SquareMatrix m = new SquareMatrixImpl(
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
    void equalityUpToADelta() {
        SquareMatrix first = new SquareMatrixImpl(0.0, -2.0, 1e-4, 12);
        SquareMatrix second = new SquareMatrixImpl(0.0, -2.0, 0.0, 12);
        assertEquals(first, second, "Equality of matrices should be up to a small delta");
    }

    @Test
    void inequality() {
        SquareMatrix first = new SquareMatrixImpl(0.0, -2.0, 1e-4, 12);
        SquareMatrix second = new SquareMatrixImpl(0, 0, 0, 0);
        assertNotEquals(first, second, "Should not give false equality for same dimensions");
    }

    @Test
    void comparingDifferentDimensionMatrices() {
        SquareMatrix first = new SquareMatrixImpl(0.0, -2.0, 1e-4, 12);
        SquareMatrix second = new SquareMatrixImpl(0, -2, -2, 12, 1, 1, 1, 1, 1);
        assertNotEquals(first, second, "Matrices of different dimensions should be different");
    }

    @Test
    void matrixMultiplication() {
        SquareMatrix first = new SquareMatrixImpl(
            1, 2, 3, 4,
            5, 6, 7, 8,
            9, 8, 7, 6,
            5, 4, 3, 2
        );
        SquareMatrix second = new SquareMatrixImpl(
            -2, 1, 2, 3,
            3, 2, 1, -1,
            4, 3, 6, 5,
            1, 2, 7, 8
        );
        SquareMatrix product = first.multiply(second);
        SquareMatrix expectedProduct = new SquareMatrixImpl(
            20, 22, 50, 48,
            44, 54, 114, 108,
            40, 58, 110, 102,
            16, 26, 46, 42
        );

        compareMatrices(expectedProduct, product);
    }

    @Test
    void multiplicationByTuple() {
        SquareMatrix A = new SquareMatrixImpl(
            1, 2, 3,
            2, 4, 4,
            8, 6, 4
        );
        Tuple x = new TupleImpl(1, 2, 3);
        Tuple multiplied = A.multiply(x);
        Tuple expectedProduct = new TupleImpl(14, 22, 32);

        assertEquals(
            expectedProduct,
            multiplied,
            "Product of a matrix and a tuple should be a tuple. " +
                TupleComparator.messageComparingCoordinates(expectedProduct, multiplied));
    }

    @Test
    void multiplicationByIdentityMatrix() {
        SquareMatrix id = SquareMatrixImpl.id(4);
        SquareMatrix A = new SquareMatrixImpl(
            0, 1, 2, 4,
            1, 2, 4, 8,
            2, 4, 8, 16,
            4, 8, 16, 32
        );

        compareMatrices(A, id.multiply(A));
    }

    @Test
    void transpose() {
        SquareMatrix A = new SquareMatrixImpl(
            0, 9, 3, 0,
            9, 8, 0, 8,
            1, 8, 5, 3,
            0, 0, 5, 8
        );
        SquareMatrix transposed = A.transpose();
        SquareMatrix expectedTranspose = new SquareMatrixImpl(
            0, 9, 1, 0,
            9, 8, 8, 0,
            3, 0, 5, 5,
            0, 8, 3, 8
        );

        compareMatrices(expectedTranspose, transposed);
    }

    @Test
    void determinant2x2() {
        SquareMatrix A = new SquareMatrixImpl(1, 5, -3, 2);
        double expected2x2Determinant = 17;
        assertEquals(expected2x2Determinant, A.det(), "Should correctly compute 2x2 matrix determinant");
    }

    @Test
    void determinant3x3() {
        SquareMatrix B = new SquareMatrixImpl(
            1, 2, 6,
            -5, 8, -4,
           2, 6, 4
        );
        double expected3x3Determinant = -196;
        assertEquals(expected3x3Determinant, B.det(), "Should correctly compute 3x3 matrix determinant");
    }

    @Test
    void determinant4x4() {
        SquareMatrix C = new SquareMatrixImpl(
            -2, -8, 3, 5,
            -3, 1, 7, 3,
            1, 2, -9, 6,
            -6, 7, 7, -9
        );
        double expected4x4Determinant = -4071;
        assertEquals(expected4x4Determinant, C.det(), "Should correctly compute 4x4 matrix determinant");
    }

    @Test
    void submatrix2x2() {
        SquareMatrixImpl M = new SquareMatrixImpl(1, 2, 3, 4);
        SquareMatrixImpl subM = M.submatrix(1, 1);
        SquareMatrixImpl expectedSubmatrix = new SquareMatrixImpl(1);

        compareMatrices(expectedSubmatrix, subM);
    }

    @Test
    void submatrix3x3() {
        SquareMatrixImpl A = new SquareMatrixImpl(
            1, 5, 0,
            -3, 2, 7,
            0, 6, -3
        );
        SquareMatrixImpl subA = A.submatrix(0, 2);
        SquareMatrixImpl expectedSubmatrix = new SquareMatrixImpl(-3, 2, 0, 6);

        compareMatrices(expectedSubmatrix, subA);
    }

    @Test
    void submatrix4x4() {
        SquareMatrixImpl B = new SquareMatrixImpl(
            -6, 1, 1, 6,
            -8, 5, 8, 6,
            -1, 0, 8, 2,
            -7, 1, -1, 1
        );
        SquareMatrixImpl subB = B.submatrix(2, 1);
        SquareMatrixImpl expectedSubmatrix = new SquareMatrixImpl(
            -6, 1, 6,
            -8, 8, 6,
            -7, -1, 1
        );

        compareMatrices(expectedSubmatrix, subB);
    }

    @Test
    void cofactor() {
        SquareMatrixImpl A = new SquareMatrixImpl(
            3, 5, 0,
            2, -1, -7,
            6, -1, 5
        );
        double expectedCofactor = -25;
        double cofactor = A.cofactor(1, 0);

        assertEquals(expectedCofactor, cofactor, "Should correctly compute 3x3 cofactor");
    }

    @Test
    void isInvertible() {
        SquareMatrix notInvertible = new SquareMatrixImpl(
            -4, 2, -2, -3,
            9, 6, 2, 6,
            0, -5, 1, -5,
            0, 0, 0, 0
        );

        assertFalse(notInvertible.isInvertible(), "Should recognise non-invertible matrices");
    }

    @Test
    void inverse() {
        SquareMatrix invertibleA = new SquareMatrixImpl(
                -5, 2, 6, -8,
                1, -5, 1, 8,
                7, 7, -6, -7,
                1, -3, 7, 4
        );
        SquareMatrix expectedAInverse = new SquareMatrixImpl(
                0.21805, 0.45113, 0.24060, -0.04511,
                -0.80827, -1.45677, -0.44361, 0.52068,
                -0.07895, -0.22368, -0.05263, 0.19737,
                -0.52256, -0.81391, -0.30075, 0.30639
        );

        compareMatrices(expectedAInverse, invertibleA.inverse());

        SquareMatrix invertibleB = new SquareMatrixImpl(
                8, -5, 9, 2,
                7, 5, 6, 1,
                -6, 0, 9, 6,
                -3, 0, -9, -4
        );
        SquareMatrix expectedBInverse = new SquareMatrixImpl(
                -0.15385, -0.15385, -0.28205, -0.53846,
                -0.07692, 0.12308, 0.02564, 0.03077,
                0.35897, 0.35897, 0.43590, 0.92308,
                -0.69231, -0.69231, -0.76923, -1.92308
        );
        compareMatrices(expectedBInverse, invertibleB.inverse());
    }

    @Test
    void multiplyingInversesCancelsOut() {
        SquareMatrix firstFactor = new SquareMatrixImpl(
            9, 3, 0, 9,
            -5, -2, -6, -3,
            -4, 9, 6, 4,
            -7, 6, 6, 2
        );
        SquareMatrix secondFactor = new SquareMatrixImpl(
            8, 2, 2, 2,
            3, -1, 7, 0,
            7, 0, 5, 4,
            6, -2, 0, 5
        );
        compareMatrices(firstFactor.multiply(secondFactor).multiply(secondFactor.inverse()), firstFactor);
    }
}
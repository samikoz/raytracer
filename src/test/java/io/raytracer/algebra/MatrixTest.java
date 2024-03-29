package io.raytracer.algebra;

import io.raytracer.algebra.CofactorDetMatrix;
import io.raytracer.algebra.FourMatrix;
import io.raytracer.algebra.ISquareMatrix;
import io.raytracer.algebra.ThreeMatrix;
import io.raytracer.algebra.TwoMatrix;
import io.raytracer.geometry.Tuple4;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MatrixTest {
    @Test
    void correctEntryAccess() {
        ThreeMatrix m = new ThreeMatrix(
            -3, -5, 0,
            1, -2, -7,
            0, 1, 1
        );
        assertAll("Matrix indices should refer to row and column number respectively",
            () -> assertEquals(0, m.get(0, 2)),
            () -> assertEquals(1, m.get(1, 0))
        );
    }

    @Test
    void equalityUpToADelta() {
        TwoMatrix first = new TwoMatrix(0.0, -2.0, 1e-4, 12);
        TwoMatrix second = new TwoMatrix(0.0, -2.0, 0.0, 12);
        assertEquals(first, second, "Equality of matrices should be up to a small delta");
    }

    @Test
    void inequality() {
        TwoMatrix first = new TwoMatrix(0.0, -2.0, 1e-4, 12);
        TwoMatrix second = new TwoMatrix(0, 0, 0, 0);
        assertNotEquals(first, second, "Should not give false equality for same dimensions");
    }


    @Test
    void matrixMultiplication() {
        ISquareMatrix first = new FourMatrix(
            1, 2, 3, 4,
            5, 6, 7, 8,
            9, 8, 7, 6,
            5, 4, 3, 2
        );
        ISquareMatrix second = new FourMatrix(
            -2, 1, 2, 3,
            3, 2, 1, -1,
            4, 3, 6, 5,
            1, 2, 7, 8
        );
        ISquareMatrix product = first.multiply(second);
        ISquareMatrix expectedProduct = new FourMatrix(
            20, 22, 50, 48,
            44, 54, 114, 108,
            40, 58, 110, 102,
            16, 26, 46, 42
        );

        assertEquals(expectedProduct, product);
    }

    @Test
    void multiplicationByTuple() {
        ISquareMatrix A = new FourMatrix(
            1, 2, 3, 0,
            2, 4, 4, 0,
            8, 6, 4, 0,
            1, 1, 1, 0
        );
        Tuple4 x = new Tuple4(1, 2, 3, 0);
        Tuple4 multiplied = A.multiply(x);
        Tuple4 expectedProduct = new Tuple4(14, 22, 32, 6);

        assertEquals(expectedProduct, multiplied);
    }

    @Test
    void transpose() {
        ISquareMatrix A = new FourMatrix(
            0, 9, 3, 0,
            9, 8, 0, 8,
            1, 8, 5, 3,
            0, 0, 5, 8
        );
        ISquareMatrix transposed = A.transpose();
        ISquareMatrix expectedTranspose = new FourMatrix(
            0, 9, 1, 0,
            9, 8, 8, 0,
            3, 0, 5, 5,
            0, 8, 3, 8
        );

        assertEquals(expectedTranspose, transposed);
    }

    @Test
    void determinant2x2() {
        TwoMatrix A = new TwoMatrix(1, 5, -3, 2);
        double expected2x2Determinant = 17;
        assertEquals(expected2x2Determinant, A.det());
    }

    @Test
    void determinant3x3() {
        ThreeMatrix B = new ThreeMatrix(
            1, 2, 6,
            -5, 8, -4,
           2, 6, 4
        );
        double expected3x3Determinant = -196;
        assertEquals(expected3x3Determinant, B.det());
    }

    @Test
    void determinant4x4() {
        ISquareMatrix C = new FourMatrix(
            -2, -8, 3, 5,
            -3, 1, 7, 3,
            1, 2, -9, 6,
            -6, 7, 7, -9
        );
        double expected4x4Determinant = -4071;
        assertEquals(expected4x4Determinant, C.det());
    }

    @Test
    void submatrix3x3() {
        ThreeMatrix A = new ThreeMatrix(
            1, 5, 0,
            -3, 2, 7,
            0, 6, -3
        );
        CofactorDetMatrix subA = A.submatrix(0, 2);
        CofactorDetMatrix expectedSubmatrix = new TwoMatrix(-3, 2, 0, 6);

        assertEquals(expectedSubmatrix, subA);
    }

    @Test
    void submatrix4x4() {
        FourMatrix B = new FourMatrix(
            -6, 1, 1, 6,
            -8, 5, 8, 6,
            -1, 0, 8, 2,
            -7, 1, -1, 1
        );
        ThreeMatrix subB = B.submatrix(2, 1);
        ThreeMatrix expectedSubmatrix = new ThreeMatrix(
            -6, 1, 6,
            -8, 8, 6,
            -7, -1, 1
        );

        assertEquals(expectedSubmatrix, subB);
    }

    @Test
    void cofactor() {
        ThreeMatrix A = new ThreeMatrix(
            3, 5, 0,
            2, -1, -7,
            6, -1, 5
        );
        double expectedCofactor = -25;
        double cofactor = A.cofactor(1, 0);

        assertEquals(expectedCofactor, cofactor);
    }

    @Test
    void isInvertible() {
        ISquareMatrix notInvertible = new FourMatrix(
            -4, 2, -2, -3,
            9, 6, 2, 6,
            0, -5, 1, -5,
            0, 0, 0, 0
        );

        assertFalse(notInvertible.isInvertible());
    }

    @Test
    void inverse() {
        ISquareMatrix invertibleA = new FourMatrix(
                -5, 2, 6, -8,
                1, -5, 1, 8,
                7, 7, -6, -7,
                1, -3, 7, 4
        );
        ISquareMatrix expectedAInverse = new FourMatrix(
                0.21805, 0.45113, 0.24060, -0.04511,
                -0.80827, -1.45677, -0.44361, 0.52068,
                -0.07895, -0.22368, -0.05263, 0.19737,
                -0.52256, -0.81391, -0.30075, 0.30639
        );

        assertEquals(expectedAInverse, invertibleA.inverse());

        ISquareMatrix invertibleB = new FourMatrix(
                8, -5, 9, 2,
                7, 5, 6, 1,
                -6, 0, 9, 6,
                -3, 0, -9, -4
        );
        ISquareMatrix expectedBInverse = new FourMatrix(
                -0.15385, -0.15385, -0.28205, -0.53846,
                -0.07692, 0.12308, 0.02564, 0.03077,
                0.35897, 0.35897, 0.43590, 0.92308,
                -0.69231, -0.69231, -0.76923, -1.92308
        );
        assertEquals(expectedBInverse, invertibleB.inverse());
    }

    @Test
    void multiplyingInversesCancelsOut() {
        ISquareMatrix firstFactor = new FourMatrix(
            9, 3, 0, 9,
            -5, -2, -6, -3,
            -4, 9, 6, 4,
            -7, 6, 6, 2
        );
        ISquareMatrix secondFactor = new FourMatrix(
            8, 2, 2, 2,
            3, -1, 7, 0,
            7, 0, 5, 4,
            6, -2, 0, 5
        );
        assertEquals(firstFactor.multiply(secondFactor).multiply(secondFactor.inverse()), firstFactor);
    }
}
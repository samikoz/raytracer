package io.raytracer.mathsy;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TupleComparator {
    static void assertTuplesEqual(Tuple expected, Tuple actual, String message) {
        assertEquals(expected.dim(), actual.dim(), "Should be of the same dimension");

        assertEquals(
            expected, actual,
            () -> message + " " + messageComparingCoordinates(expected, actual)
        );
    }

    static String messageComparingCoordinates(Tuple expected, Tuple actual) {
        String[] coordinateComparisonMessages = new String[expected.dim()];

        for (int coordinateIndex = 0; coordinateIndex < expected.dim(); coordinateIndex++) {
            coordinateComparisonMessages[coordinateIndex] =
                expected.get(coordinateIndex) + " should be " + actual.get(coordinateIndex);
        }
        return String.join(", ", Arrays.asList(coordinateComparisonMessages));
    }
}

class RealTupleTest {

    @Test
    void equalityOfTuplesUpToADelta() {
        Tuple first = new TupleImpl(0.0, -2.0, 1e-4);
        Tuple second = new TupleImpl(0.0, -2.0, 0.0);

        assertEquals(first, second, "Equality of tuples should be up to a small delta");
    }

}

class RealVectorTest {

    @Test
    void additionOfTwoVectors() {
        Vector first = new VectorImpl(1.0, 1.0, 1.0);
        Vector second = new VectorImpl(1.0, 0.0, -1.0);
        Vector expectedSum = new VectorImpl(2.0, 1.0, 0.0);
        Vector actualSum = first.add(second);

        TupleComparator.assertTuplesEqual(expectedSum, actualSum, "Sum of two vectors should be a vector.");
    }

    @Test
    void multiplicationByScalar() {
        Vector vector = new VectorImpl(2.5, -0.4, 1);
        Vector expectedProduct = new VectorImpl(1.25, -0.2, 0.5);
        Vector actualProduct = vector.multiply(0.5);

        TupleComparator.assertTuplesEqual(expectedProduct, actualProduct, "A vector times a scalar should be a vector.");
    }

    @Test
    void norm() {
        Vector vector = new VectorImpl(1, 2, 3);
        assertEquals(Math.sqrt(14), vector.norm(), 1e-3, "Norm of (1, 2, 3) should be sqrt(14)");
    }


    @Test
    void normalise() {
        Vector vector = new VectorImpl(1, 2, 3);
        Vector expectedNormalised = new VectorImpl(1 / Math.sqrt(14), 2 / Math.sqrt(14), 3 / Math.sqrt(14));
        Vector normalised = vector.normalise();

        assertAll(
            "Should return a correctly normalised vector.",
            () -> assertEquals(
                    normalised.norm(), 1, 1e-3, "Normalised vector should have unit norm"
            ),
            () -> TupleComparator.assertTuplesEqual(expectedNormalised, normalised, "Should have appropriate coordinates")
        );
    }

    @Test
    void dot() {
        Vector first = new VectorImpl(1, 2, 3);
        Vector second = new VectorImpl(2, 3, 4);
        double expectedProduct = 20;

        assertEquals(expectedProduct, first.dot(second), "(1, 2, 3) dot (2, 3, 4) should equal 20.0");
    }

}

class Real3VectorTest {

    @Test
    void cross() {
        ThreeVector first = new Real3Vector(1, 2, 3);
        ThreeVector second = new Real3Vector(2, 3, 4);
        ThreeVector expectedCross = new Real3Vector(-1, 2, -1);
        ThreeVector actualCross = first.cross(second);

        TupleComparator.assertTuplesEqual(expectedCross, actualCross, "Cross of two vectors should be a vector.");
    }
}

class RealPointTest {

    @Test
    void additionOfPointAndVector() {
        Point aPoint = new PointImpl(12.0, 3.7, -0.2);
        Vector aVector = new VectorImpl(-5.0, 0.2, 0.0);
        Point expectedSum = new PointImpl(7.0, 3.9, -0.2);
        Point actualSum = aPoint.add(aVector);

        TupleComparator.assertTuplesEqual(expectedSum, actualSum, "Sum of a point and a vector should be a point.");
    }

    @Test
    void subtractionOfTwoPoints() {
        Point first = new PointImpl(-1.0,-1.0,0.0);
        Point second = new PointImpl(2.0,-2.0,5.0);
        Vector expectedDifference = new VectorImpl(-3.0,1.0,-5.0);
        Vector actualDifference = first.subtract(second);

        TupleComparator.assertTuplesEqual(expectedDifference, actualDifference, "Difference of two points should be a vector.");
    }
}
package io.raytracer.mathsy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TupleComparator {
    private static String compareCoordinates(Real3Tuple expected, Real3Tuple actual) {
        return expected.getX() + " should be " + actual.getX() +
            ", " + expected.getY() + " should be " + actual.getY() +
            ", " + expected.getZ() + " should be " + actual.getZ();
    }

    static String compareCoordinates(Vector expected, Vector actual) {
        return compareCoordinates((Real3Tuple) expected, (Real3Tuple) actual);
    }

    static String compareCoordinates(Point expected, Point actual) {
        return compareCoordinates((Real3Tuple) expected, (Real3Tuple) actual);
    }
}

class Real3TupleTest {

    @Test
    void testEqualityOfTuplesUpToADelta() {
        Vector first = new Real3Tuple(0.0, -2.0, 1e-4);
        Vector second = new Real3Tuple(0.0, -2.0, 0.0);

        assertEquals(first, second, "Equality of tuples should be up to a small delta");
    }

}

class Real3VectorTest {

    @Test
    void add() {
        Vector first = new Real3Tuple(1.0, 1.0, 1.0);
        Vector second = new Real3Tuple(1.0, 0.0,-1.0);
        Vector expectedSum = new Real3Tuple(2.0,1.0, 0.0);
        Vector added = first.add(second);

        assertEquals(
            expectedSum, added,
            () -> "Sum of two vectors should be a vector. " +
                TupleComparator.compareCoordinates(expectedSum, added)
        );
    }

    @Test
    void subtract() {
        Vector first = new Real3Tuple(2.8, -0.001, 12.0);
        Vector second = new Real3Tuple(2.8, 0.001,0.0);
        Vector expectedDifference = new Real3Tuple(0.0,-0.002, 12.0);
        Vector subtracted = first.subtract(second);

        assertEquals(
            expectedDifference, subtracted,
            () -> "Subtraction of two vectors should be a vector. " +
                TupleComparator.compareCoordinates(expectedDifference, subtracted)
        );
    }

    @Test
    void negate() {
        Vector vector = new Real3Tuple(0.0, 15.2, -5.8);
        Vector expectedNegation = new Real3Tuple(0.0, -15.2, 5.8);
        Vector negated = vector.negate();

        assertEquals(
            expectedNegation, negated,
            () -> "Negation of a vector should be a vector. " +
                TupleComparator.compareCoordinates(expectedNegation, negated)
        );
    }

    @Test
    void multiply() {
        Vector vector = new Real3Tuple(2.5, -0.4, 1);
        Vector expectedProduct = new Real3Tuple(1.25, -0.2,0.5);
        Vector product = vector.multiply(0.5);

        assertEquals(
            expectedProduct, product,
            () -> "A vector times a scalar should be a vector. " +
                TupleComparator.compareCoordinates(expectedProduct, product)
        );
    }

    @Test
    void norm() {
        Vector vector = new Real3Tuple(1, 2, 3);
        assertEquals(Math.sqrt(14), vector.norm(), 1e-3,"Norm of (1, 2, 3) should be sqrt(14)");
    }


    @Test
    void normalise() {
        Vector vector = new Real3Tuple(1,2,3);
        Vector expectedNormalised = new Real3Tuple(1/Math.sqrt(14), 2/Math.sqrt(14), 3/Math.sqrt(14));
        Vector normalised = vector.normalise();

        assertAll(
            "Should return a correctly normalised vector.",
            () -> assertEquals(
                normalised.norm(), 1, 1e-3, "Normalised vector should have unit norm"
            ),
            () -> assertEquals(
                expectedNormalised, normalised,
                () -> TupleComparator.compareCoordinates(expectedNormalised, normalised)
            )
        );
    }

    @Test
    void dot() {
        Vector first = new Real3Tuple(1, 2, 3);
        Vector second = new Real3Tuple(2,3,4);
        double expectedProduct = 20;

        assertEquals(expectedProduct, first.dot(second), "(1, 2, 3) dot (2, 3, 4) should equal 20.0");
    }

    @Test
    void cross() {
        Vector first = new Real3Tuple(1, 2, 3);
        Vector second = new Real3Tuple(2, 3, 4);
        Vector expectedCrossProduct = new Real3Tuple(-1, 2, -1);
        Vector crossed = first.cross(second);

        assertEquals(
            expectedCrossProduct, crossed,
                () -> "(1, 2, 3) cross (2, 3, 4) should be a vector. " +
                    TupleComparator.compareCoordinates(expectedCrossProduct, crossed)
        );
    }
}

class Real3PointTest {

    @Test
    void additionOfPointAndVector() {
        Point point = new Real3Tuple(12.0, 3.7, -0.2);
        Vector vector = new Real3Tuple(-5.0, 0.2, 0.0);
        Point expectedSum = new Real3Tuple(7.0, 3.9, -0.2);
        Point summed = point.add(vector);

        assertEquals(
            expectedSum, summed,
            () -> "Sum of a point and a vector should be a point. " +
                TupleComparator.compareCoordinates(expectedSum, summed)
        );
    }

    @Test
    void subtractionOfTwoPoints() {
        Point first = new Real3Tuple(-1.0,-1.0,0.0);
        Point second = new Real3Tuple(2.0,-2.0,5.0);
        Vector expectedDifference = new Real3Tuple(-3.0,1.0,-5.0);
        Vector subtracted = first.subtract(second);

        assertEquals(
            expectedDifference, subtracted,
            () -> "Difference of two points should be a vector. " +
                TupleComparator.compareCoordinates(expectedDifference, subtracted)
        );
    }

    @Test
    void subtractionOfPointAndVector() {
        Point point = new Real3Tuple(3.5,0.0,0.0);
        Vector displacement = new Real3Tuple(0.0,0.0,-2.1);
        Point expectedPosition = new Real3Tuple(3.5,0.0,2.1);
        Point subtracted = point.subtract(displacement);

        assertEquals(
            expectedPosition, subtracted,
            () -> "Difference of a point and a vector should be a point. " +
                TupleComparator.compareCoordinates(expectedPosition, subtracted)
        );
    }
}
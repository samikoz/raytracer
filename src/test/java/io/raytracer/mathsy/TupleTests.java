package io.raytracer.mathsy;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TupleComparator {
    private static String compareCoordinates(RealTuple expected, RealTuple actual) {
        String[] coordComparison = new String[expected.dim()];
        for (int coordIndex = 0; coordIndex < expected.dim(); coordIndex++) {
            coordComparison[coordIndex] = expected.get(coordIndex) + " should be " + actual.get(coordIndex);
        }
        return String.join(", ", Arrays.asList(coordComparison));
    }

    static String compareCoordinates(Vector expected, Vector actual) {
        return compareCoordinates((RealTuple) expected, (RealTuple) actual);
    }

    static String compareCoordinates(Point expected, Point actual) {
        return compareCoordinates((RealTuple) expected, (RealTuple) actual);
    }
}

class RealTupleTest {

    @Test
    void testEqualityOfTuplesUpToADelta() {
        Vector first = new RealTuple(0.0, -2.0, 1e-4);
        Vector second = new RealTuple(0.0, -2.0, 0.0);

        assertEquals(first, second, "Equality of tuples should be up to a small delta");
    }

}

class RealVectorTest {

    @Test
    void add() {
        Vector first = new RealTuple(1.0, 1.0, 1.0);
        Vector second = new RealTuple(1.0, 0.0,-1.0);
        Vector expectedSum = new RealTuple(2.0,1.0, 0.0);
        Vector added = first.add(second);

        assertEquals(
            expectedSum, added,
            () -> "Sum of two vectors should be a vector. " +
                TupleComparator.compareCoordinates(expectedSum, added)
        );
    }

    @Test
    void subtract() {
        Vector first = new RealTuple(2.8, -0.001, 12.0);
        Vector second = new RealTuple(2.8, 0.001,0.0);
        Vector expectedDifference = new RealTuple(0.0,-0.002, 12.0);
        Vector subtracted = first.subtract(second);

        assertEquals(
            expectedDifference, subtracted,
            () -> "Subtraction of two vectors should be a vector. " +
                TupleComparator.compareCoordinates(expectedDifference, subtracted)
        );
    }

    @Test
    void negate() {
        Vector vector = new RealTuple(0.0, 15.2, -5.8);
        Vector expectedNegation = new RealTuple(0.0, -15.2, 5.8);
        Vector negated = vector.negate();

        assertEquals(
            expectedNegation, negated,
            () -> "Negation of a vector should be a vector. " +
                TupleComparator.compareCoordinates(expectedNegation, negated)
        );
    }

    @Test
    void multiply() {
        Vector vector = new RealTuple(2.5, -0.4, 1);
        Vector expectedProduct = new RealTuple(1.25, -0.2,0.5);
        Vector product = vector.multiply(0.5);

        assertEquals(
            expectedProduct, product,
            () -> "A vector times a scalar should be a vector. " +
                TupleComparator.compareCoordinates(expectedProduct, product)
        );
    }

    @Test
    void norm() {
        Vector vector = new RealTuple(1, 2, 3);
        assertEquals(Math.sqrt(14), vector.norm(), 1e-3,"Norm of (1, 2, 3) should be sqrt(14)");
    }


    @Test
    void normalise() {
        Vector vector = new RealTuple(1,2,3);
        Vector expectedNormalised = new RealTuple(1/Math.sqrt(14), 2/Math.sqrt(14), 3/Math.sqrt(14));
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
        Vector first = new RealTuple(1, 2, 3);
        Vector second = new RealTuple(2,3,4);
        double expectedProduct = 20;

        assertEquals(expectedProduct, first.dot(second), "(1, 2, 3) dot (2, 3, 4) should equal 20.0");
    }

    @Test
    void cross() {
        Vector first = new RealTuple(1, 2, 3);
        Vector second = new RealTuple(2, 3, 4);
        Vector expectedCrossProduct = new RealTuple(-1, 2, -1);
        Vector crossed = first.cross(second);

        assertEquals(
            expectedCrossProduct, crossed,
                () -> "(1, 2, 3) cross (2, 3, 4) should be a vector. " +
                    TupleComparator.compareCoordinates(expectedCrossProduct, crossed)
        );
    }
}

class RealPointTest {

    @Test
    void additionOfPointAndVector() {
        Point point = new RealTuple(12.0, 3.7, -0.2);
        Vector vector = new RealTuple(-5.0, 0.2, 0.0);
        Point expectedSum = new RealTuple(7.0, 3.9, -0.2);
        Point summed = point.add(vector);

        assertEquals(
            expectedSum, summed,
            () -> "Sum of a point and a vector should be a point. " +
                TupleComparator.compareCoordinates(expectedSum, summed)
        );
    }

    @Test
    void subtractionOfTwoPoints() {
        Point first = new RealTuple(-1.0,-1.0,0.0);
        Point second = new RealTuple(2.0,-2.0,5.0);
        Vector expectedDifference = new RealTuple(-3.0,1.0,-5.0);
        Vector subtracted = first.subtract(second);

        assertEquals(
            expectedDifference, subtracted,
            () -> "Difference of two points should be a vector. " +
                TupleComparator.compareCoordinates(expectedDifference, subtracted)
        );
    }

    @Test
    void subtractionOfPointAndVector() {
        Point point = new RealTuple(3.5,0.0,0.0);
        Vector displacement = new RealTuple(0.0,0.0,-2.1);
        Point expectedPosition = new RealTuple(3.5,0.0,2.1);
        Point subtracted = point.subtract(displacement);

        assertEquals(
            expectedPosition, subtracted,
            () -> "Difference of a point and a vector should be a point. " +
                TupleComparator.compareCoordinates(expectedPosition, subtracted)
        );
    }
}
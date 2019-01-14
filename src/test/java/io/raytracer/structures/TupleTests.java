package io.raytracer.structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TupleComparator {
    static String compareCoordinates(Tuple expected, Tuple actual) {
        return expected.getX() + " should be " + actual.getX() +
            ", " + expected.getY() + " should be " + actual.getY() +
            ", " + expected.getZ() + " should be " + actual.getZ();
    }
}

class Real3TupleTest {

    @Test
    void testInequalityOfDifferentTuplesWithSameCoords() {
        Point point = new Real3Point(1.0, 0.5, 0.0);
        Vector vector = new Real3Vector(1.0, 0.5, 0.0);

        assertFalse(point.equalTo(vector), "Point and vector of the same coordinates should be distinct");
    }

    @Test
    void testEqualityOfTuplesUpToADelta() {
        Tuple first = new Real3Tuple(0.0, -2.0, 1e-4);
        Tuple second = new Real3Tuple(0.0, -2.0, 0.0);

        assertTrue(first.equalTo(second), "Equality of tuples should be up to a small delta");
    }

}

class Real3VectorTest {

    @Test
    void testAdditionOfTwoVectors() {
        Vector first = new Real3Vector(1.0, 1.0, 1.0);
        Vector second = new Real3Vector(1.0, 0.0,-1.0);
        Vector expectedSum = new Real3Vector(2.0,1.0, 0.0);
        Vector added = first.add(second);

        assertTrue(
            expectedSum.equalTo(added),
            () -> "Sum of two vectors should be a vector. " +
                TupleComparator.compareCoordinates(expectedSum, added)
        );
    }

    @Test
    void testSubtractionOfTwoVectors() {
        Vector first = new Real3Vector(2.8, -0.001, 12.0);
        Vector second = new Real3Vector(2.8, 0.001,0.0);
        Vector expectedDifference = new Real3Vector(0.0,-0.002, 12.0);
        Vector subtracted = first.subtract(second);

        assertTrue(
            expectedDifference.equalTo(subtracted),
            () -> "Subtraction of two vectors should be a vector. " +
                TupleComparator.compareCoordinates(expectedDifference, subtracted)
        );
    }

    @Test
    void testNegation() {
        Vector vector = new Real3Vector(0.0, 15.2, -5.8);
        Vector expectedNegation = new Real3Vector(0.0, -15.2, 5.8);
        Vector negated = vector.negate();

        assertTrue(
            expectedNegation.equalTo(negated),
            () -> "Negation of a vector should be a vector. " +
                TupleComparator.compareCoordinates(expectedNegation, negated)
        );
    }

    @Test
    void testScalarMultiplication() {
        Vector vector = new Real3Vector(2.5, -0.4, 1);
        Vector expectedProduct = new Real3Vector(1.25, -0.2,0.5);
        Vector product = vector.multiply(0.5);

        assertTrue(
            expectedProduct.equalTo(product),
            () -> "A vector times a scalar should be a vector. " +
                TupleComparator.compareCoordinates(expectedProduct, product)
        );
    }

    @Test
    void testNorm() {
        Vector vector = new Real3Vector(1, 2, 3);
        assertEquals(Math.sqrt(14), vector.norm(), 1e-1,"Norm of (1, 2, 3) should be sqrt(14).");
    }
}

class Real3PointTest {

    @Test
    void testAdditionOfPointAndVector() {
        Point point = new Real3Point(12.0, 3.7, -0.2);
        Vector vector = new Real3Vector(-5.0, 0.2, 0.0);
        Point expectedSum = new Real3Point(7.0, 3.9, -0.2);
        Point summed = point.add(vector);

        assertTrue(
            expectedSum.equalTo(summed),
            () -> "Sum of a point and a vector should be a point. " +
                TupleComparator.compareCoordinates(expectedSum, summed)
        );
    }

    @Test
    void testSubtractionOfTwoPoints() {
        Point first = new Real3Point(-1.0,-1.0,0.0);
        Point second = new Real3Point(2.0,-2.0,5.0);
        Vector expectedDifference = new Real3Vector(-3.0,1.0,-5.0);
        Vector subtracted = first.subtract(second);

        assertTrue(
            expectedDifference.equalTo(subtracted),
            () -> "Difference of two points should be a vector. " +
                TupleComparator.compareCoordinates(expectedDifference, subtracted)
        );
    }

    @Test
    void testSubtractionOfPointAndVector() {
        Point point = new Real3Point(3.5,0.0,0.0);
        Vector displacement = new Real3Vector(0.0,0.0,-2.1);
        Point expectedPosition = new Real3Point(3.5,0.0,2.1);
        Point subtracted = point.subtract(displacement);

        assertTrue(
            expectedPosition.equalTo(subtracted),
            () -> "Difference of a point and a vector should be a point. " +
                TupleComparator.compareCoordinates(expectedPosition, subtracted)
        );
    }
}
package io.raytracer.structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Real3TupleTest {

    @Test
    void testInequalityOfDifferentTuplesWithSameCoords() {
        Point point = new Real3Point(1.0, 0.5, 0.0);
        Vector vector = new Real3Vector(1.0, 0.5, 0.0);

        assertFalse(point.equalTo(vector), "Point should be distinct from a vector");
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
    void testAdditionOfTwoVectorsGivesVector() {
        Vector first = new Real3Vector(1.0, 1.0, 1.0);
        Vector second = new Real3Vector(1.0, 0.0,-1.0);
        Vector expectedSum = new Real3Vector(2.0,1.0, 0.0);

        assertTrue(expectedSum.equalTo(first.add(second)), "Sum of two vectors should be a vector");
    }

    @Test
    void testSubtractionOfTwoVectorsGivesVector() {
        Vector first = new Real3Vector(2.8, -0.001, 12.0);
        Vector second = new Real3Vector(2.8, 0.001,0.0);
        Vector expectedDifference = new Real3Vector(0.0,-0.002, 12.0);
        Vector subtracted = first.subtract(second);

        assertTrue(
            expectedDifference.equalTo(subtracted),
            () -> "Subtraction result should be a vector. " +
                Double.toString(subtracted.getX()) + " should be " + Double.toString(expectedDifference.getX()) +
                ", " + Double.toString(subtracted.getY()) + " should be " + Double.toString(expectedDifference.getY()) +
                ", " + Double.toString(subtracted.getZ()) + " should be " + Double.toString(expectedDifference.getZ())
        );
    }

    @Test
    void testNegation() {
        Vector vector = new Real3Vector(0.0, 15.2, -5.8);
        Vector negated = new Real3Vector(0.0, -15.2, 5.8);

        assertTrue(negated.equalTo(vector.negate()), "A vector should negate by negating all coordinaes");
    }
}

class Real3PointTest {

    @Test
    void testAdditionOfPointAndVectorGivesPoint() {
        Point point = new Real3Point(12.0, 3.7, -0.2);
        Vector vector = new Real3Vector(-5.0, 0.2, 0.0);
        Point expectedSum = new Real3Point(7.0, 3.9, -0.2);

        assertTrue(expectedSum.equalTo(point.add(vector)), "Sum of a point and a vector should be a point");
    }

    @Test
    void testSubtractionOfTwoPointsGivesVector() {
        Point first = new Real3Point(-1.0,-1.0,0.0);
        Point second = new Real3Point(2.0,-2.0,5.0);
        Vector expectedDifference = new Real3Vector(-3.0,1.0,-5.0);

        assertTrue(
                expectedDifference.equalTo(first.subtract(second)),
                "Difference of two points should be a vector"
        );
    }

    @Test
    void testSubtractionOfPointAndVectorGivesPoint() {
        Point point = new Real3Point(3.5,0.0,0.0);
        Vector displacement = new Real3Vector(0.0,0.0,-2.1);
        Point expectedPosition = new Real3Point(3.5,0.0,2.1);

        assertTrue(
                expectedPosition.equalTo(point.subtract(displacement)),
                "Difference of a point and a vector should be a point"
        );
    }
}
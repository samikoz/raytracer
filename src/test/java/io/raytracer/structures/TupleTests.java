package io.raytracer.structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TupleTest {

    @Test
    void testDifferentTuplesWithSameCoords() {
        Point point = new Real3Point(1.0, 0.5, 0.0);
        Vector vector = new Real3Vector(1.0, 0.5, 0.0);

        assertFalse(point.equalTo(vector), "Point should be distinct from a vector");
    }

    @Test
    void testEqualityOfTuples() {
        Tuple first = new Real3Tuple(0.0, -2.0, 1e-4);
        Tuple second = new Real3Tuple(0.0, -2.0, 0.0);

        assertTrue(first.equalTo(second), "Equality of tuples should be up to a small delta");
    }

    @Test
    void testAdditionOfPointAndVectorGivesPoint() {
        Point point = new Real3Point(12.0, 3.7, -0.2);
        Vector vector = new Real3Vector(-5.0, 0.2, 0.0);
        Point expectedSum = new Real3Point(7.0, 3.9, -0.2);

        assertTrue(expectedSum.equalTo(point.add(vector)), "Sum of a point and a vector should be a point");
    }

}

class VectorTest {

    @Test
    void testAdditionOfTwoVectorsGivesVector() {
        Vector first = new Real3Vector(1.0, 1.0, 1.0);
        Vector second = new Real3Vector(1.0, 0.0,-1.0);
        Vector expectedSum = new Real3Vector(2.0,1.0, 0.0);

        assertTrue(expectedSum.equalTo(first.add(second)), "Sum of two vectors should be a vector");
    }
}
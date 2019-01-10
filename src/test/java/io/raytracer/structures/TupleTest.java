package io.raytracer.structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Real3TupleTest {

    @Test
    void testDifferentTuplesWithSameCoords() {
        Point point = new Real3Point(1.0, 0.5, 0.0);
        Vector vector = new Real3Vector(1.0, 0.5, 0.0);

        assertFalse(point.equalTo(vector), "Point should be distinct from a vector");
    }

    @Test
    void testEqualityOfTuples() {
        Tuple first = new Real3Tuple(0.0, -2.0, 0.0001);
        Tuple second = new Real3Tuple(0.0, -2.0, 0.0);

        assertTrue(first.equalTo(second), "Equality of tuples should be up to a small delta");
    }

}
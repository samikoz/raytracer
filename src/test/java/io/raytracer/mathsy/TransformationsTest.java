package io.raytracer.mathsy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransformationsTest {

    @Test
    void translatingReal4Immersed3Point() {
        TransformablePoint p = new Real4Immersed3Point(-3, 4, 5);
        Point expectedPosition = new Real4Immersed3Point(2, 1, 7);
        TransformablePoint translated = p.translate(new RealVector(5, -3, 2));

        assertEquals(expectedPosition, translated, TupleComparator.compareCoordinates(expectedPosition, translated));
    }

    @Test
    void translatingReal4Immersed3Vector() {
        TransformableVector v = new Real4Immersed3Vector(-3, 4, 5);
        Vector translated = v.translate(new RealVector(5, -3, 2));

        assertEquals(v, translated, TupleComparator.compareCoordinates(v, translated));
    }
}
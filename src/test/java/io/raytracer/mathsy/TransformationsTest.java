package io.raytracer.mathsy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransformationsTest {

    @Test
    void translatingPoint() {
        Point p = new RealTuple(-3, 4, 5);
        Transformation t = new Translation(5, -3, 2);
        Point expectedPosition = new RealTuple(2, 1, 7);
        Point translated = p.transform(t);

        assertEquals(expectedPosition, translated, TupleComparator.compareCoordinates(expectedPosition, translated));
        assertEquals(translated, t.act(p), "Point transformed should be equivalent to transformation acting on it");
    }

    @Test
    void inverseTranslatingPoint() {
        Point p = new RealTuple(-3, 4, 5);
        Transformation t = new Translation(5, -3, 2).inverse();
        Point expectedPosition = new RealTuple(-8, 7, 3);
        Point translated = p.transform(t);

        assertEquals(expectedPosition, translated, TupleComparator.compareCoordinates(expectedPosition, translated));
    }

    @Test
    void translatingVector() {
        Vector v = new RealTuple(-3, 4, 5);
        Transformation t = new Translation(5, -3, 2);
        Vector translated = v.transform(t);

        assertEquals(v, translated, TupleComparator.compareCoordinates(v, translated));
        assertEquals(translated, t.act(v), "Vector transformed should be equivalent to transformation acting on it");
    }
}
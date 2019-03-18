package io.raytracer.mathsy;

import org.junit.jupiter.api.Test;

class Immersing3TransformationTest {
    @Test
    void pointTranslation() {
        Transformation aTranslation = Immersing3Transformation.translation(5, -3, 2);
        Point aPoint = new RealPoint(3, 4, 5);
        Point expectedTranslatedPoint = new RealPoint(8, 1, 7);
        Point actualTranslatedPoint = aTranslation.act(aPoint);

        TupleComparator.assertTuplesEqual(expectedTranslatedPoint, actualTranslatedPoint, "A translated point should be a point.");
    }

    @Test
    void vectorTranslation() {
        Transformation aTranslation = Immersing3Transformation.translation(5, -3, 2);
        Vector aVector = new Real3Vector(3, 4, 5);
        Vector actualTranslatedPoint = aTranslation.act(aVector);

        TupleComparator.assertTuplesEqual(aVector, actualTranslatedPoint, "A translated vector should be a vector.");
    }
}
package io.raytracer.geometry;

import org.junit.jupiter.api.Test;
import io.raytracer.geometry.helpers.TupleComparator;

class ThreeTransformationTest {
    @Test
    void pointTranslation() {
        Transformation aTranslation = ThreeTransformation.translation(5, -3, 2);
        Point aPoint = new PointImpl(3, 4, 5);
        Point expectedTranslatedPoint = new PointImpl(8, 1, 7);
        Point actualTranslatedPoint = aPoint.transform(aTranslation);

        TupleComparator.assertTuplesEqual(expectedTranslatedPoint, actualTranslatedPoint,
                "A translated point should be a point.");
    }

    @Test
    void pointTranslationByInverse() {
        Transformation aTranslation = ThreeTransformation.translation(5, -3, 2).inverse();
        Point aPoint = new PointImpl(3, 4, 5);
        Point expectedTranslatedPoint = new PointImpl(-2, 7, 3);
        Point actualTranslatedPoint = aPoint.transform(aTranslation);

        TupleComparator.assertTuplesEqual(expectedTranslatedPoint, actualTranslatedPoint,
                "Translation by inverse translates in the opposite direction.");
    }

    @Test
    void vectorTranslation() {
        Transformation aTranslation = ThreeTransformation.translation(5, -3, 2);
        Vector aVector = new VectorImpl(3, 4, 5);
        Vector actualTranslatedPoint = aVector.transform(aTranslation);

        TupleComparator.assertTuplesEqual(aVector, actualTranslatedPoint,
                "A translated vector should be a vector.");
    }
}
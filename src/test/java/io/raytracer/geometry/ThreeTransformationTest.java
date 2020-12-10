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
                "Should correctly translate a point.");
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
                "Should correctly translate a vector.");
    }

    @Test
    void pointScaling() {
        Transformation aScaling = ThreeTransformation.scaling(2, 3, 4);
        Point aPoint = new PointImpl(-4, 6, 8);
        Point expectedScaledPoint = new PointImpl(-8, 18, 32);
        Point actualScaledPoint = aPoint.transform(aScaling);

        TupleComparator.assertTuplesEqual(expectedScaledPoint, actualScaledPoint,
                "Should correctly scale a point.");
    }

    @Test
    void vectorScaling() {
        Transformation aScaling = ThreeTransformation.scaling(2, 3, 4);
        Vector aVector = new VectorImpl(-4, 6, 8);
        Vector expectedScaledVector = new VectorImpl(-8, 18, 32);
        Vector actualScaledVector = aVector.transform(aScaling);

        TupleComparator.assertTuplesEqual(expectedScaledVector, actualScaledVector,
                "Should correctly scale a vector.");
    }

    @Test
    void vectorScalingByInverse() {
        Transformation aScaling = ThreeTransformation.scaling(2, 3, 4).inverse();
        Vector aVector = new VectorImpl(-4, 6, 8);
        Vector expectedScaledVector = new VectorImpl(-2, 2, 2);
        Vector actualScaledVector = aVector.transform(aScaling);

        TupleComparator.assertTuplesEqual(expectedScaledVector, actualScaledVector,
                "Scaling by an inverse scales in the opposite direction.");
    }

    @Test
    void pointXRotation() {
        Transformation halfQuarterRotation = ThreeTransformation.rotation_x(Math.PI / 4);
        Point aPoint = new PointImpl(0, 1, 0);
        Point expectedRotatedPoint = new PointImpl(0, Math.sqrt(2)/2, Math.sqrt(2)/2);
        Point actualRotatedPoint = aPoint.transform(halfQuarterRotation);

        TupleComparator.assertTuplesEqual(expectedRotatedPoint, actualRotatedPoint,
                "Should correctly rotate a point around the x axis.");
    }

    @Test
    void pointInverseXRotation() {
        Transformation halfQuarterRotation = ThreeTransformation.rotation_x(Math.PI / 4).inverse();
        Point aPoint = new PointImpl(0, 1, 0);
        Point expectedRotatedPoint = new PointImpl(0, Math.sqrt(2)/2, -Math.sqrt(2)/2);
        Point actualRotatedPoint = aPoint.transform(halfQuarterRotation);

        TupleComparator.assertTuplesEqual(expectedRotatedPoint, actualRotatedPoint,
                "Inverse rotation around x axis should rotate counterclockwise.");
    }
}
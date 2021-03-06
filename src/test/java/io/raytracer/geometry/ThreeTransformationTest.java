package io.raytracer.geometry;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.ParameterizedTest;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ThreeTransformationTest {
    @Test
    void equalityTest() {
        Transformation t1 = ThreeTransformation.translation(1, 2, 3);
        ThreeTransformation t2 = ThreeTransformation.translation(1, 2, 3);

        assertEquals(t1, t2);
    }

    @Test
    void inequalityTest() {
        Transformation t1 = ThreeTransformation.translation(1, 2, 3);
        Transformation t2 = ThreeTransformation.translation(0, 2, 3);

        assertNotEquals(t1, t2);
    }

    @Test
    void pointTranslation() {
        Transformation aTranslation = ThreeTransformation.translation(5, -3, 2);
        Point aPoint = new PointImpl(3, 4, 5);
        Point expectedTranslatedPoint = new PointImpl(8, 1, 7);
        Point actualTranslatedPoint = aPoint.transform(aTranslation);

        assertEquals(expectedTranslatedPoint, actualTranslatedPoint);
    }

    @Test
    void pointTranslationByInverse() {
        Transformation aTranslation = ThreeTransformation.translation(5, -3, 2).inverse();
        Point aPoint = new PointImpl(3, 4, 5);
        Point expectedTranslatedPoint = new PointImpl(-2, 7, 3);
        Point actualTranslatedPoint = aPoint.transform(aTranslation);

        assertEquals(expectedTranslatedPoint, actualTranslatedPoint,
                "Translation by inverse should translate in the opposite direction.");
    }

    @Test
    void vectorTranslation() {
        Transformation aTranslation = ThreeTransformation.translation(5, -3, 2);
        Vector aVector = new VectorImpl(3, 4, 5);
        Vector actualTranslatedPoint = aVector.transform(aTranslation);

        assertEquals(aVector, actualTranslatedPoint);
    }

    @Test
    void pointScaling() {
        Transformation aScaling = ThreeTransformation.scaling(2, 3, 4);
        Point aPoint = new PointImpl(-4, 6, 8);
        Point expectedScaledPoint = new PointImpl(-8, 18, 32);
        Point actualScaledPoint = aPoint.transform(aScaling);

        assertEquals(expectedScaledPoint, actualScaledPoint);
    }

    @Test
    void vectorScaling() {
        Transformation aScaling = ThreeTransformation.scaling(2, 3, 4);
        Vector aVector = new VectorImpl(-4, 6, 8);
        Vector expectedScaledVector = new VectorImpl(-8, 18, 32);
        Vector actualScaledVector = aVector.transform(aScaling);

        assertEquals(expectedScaledVector, actualScaledVector);
    }

    @Test
    void vectorScalingByInverse() {
        Transformation aScaling = ThreeTransformation.scaling(2, 3, 4).inverse();
        Vector aVector = new VectorImpl(-4, 6, 8);
        Vector expectedScaledVector = new VectorImpl(-2, 2, 2);
        Vector actualScaledVector = aVector.transform(aScaling);

        assertEquals(expectedScaledVector, actualScaledVector,
                "Scaling by an inverse scales in the opposite direction.");
    }

    @Test
    void pointXRotation() {
        Transformation halfQuarterRotation = ThreeTransformation.rotation_x(Math.PI / 4);
        Point aPoint = new PointImpl(0, 1, 0);
        Point expectedRotatedPoint = new PointImpl(0, Math.sqrt(2) / 2, Math.sqrt(2) / 2);
        Point actualRotatedPoint = aPoint.transform(halfQuarterRotation);

        assertEquals(expectedRotatedPoint, actualRotatedPoint);
    }

    @Test
    void pointInverseXRotation() {
        Transformation halfQuarterRotation = ThreeTransformation.rotation_x(Math.PI / 4).inverse();
        Point aPoint = new PointImpl(0, 1, 0);
        Point expectedRotatedPoint = new PointImpl(0, Math.sqrt(2) / 2, -Math.sqrt(2) / 2);
        Point actualRotatedPoint = aPoint.transform(halfQuarterRotation);

        assertEquals(expectedRotatedPoint, actualRotatedPoint,
                "Inverse rotation around x axis should rotate counterclockwise.");
    }

    @Test
    void pointYRotation() {
        Transformation halfQuarterRotation = ThreeTransformation.rotation_y(Math.PI / 4);
        Point aPoint = new PointImpl(0, 0, 1);
        Point expectedRotatedPoint = new PointImpl(Math.sqrt(2) / 2, 0, Math.sqrt(2) / 2);
        Point actualRotatedPoint = aPoint.transform(halfQuarterRotation);

        assertEquals(expectedRotatedPoint, actualRotatedPoint,
                "Should correctly rotate a point around the y axis.");
    }

    @Test
    void pointZRotation() {
        Transformation halfQuarterRotation = ThreeTransformation.rotation_z(Math.PI / 4);
        Point aPoint = new PointImpl(0, 1, 0);
        Point expectedRotatedPoint = new PointImpl(-Math.sqrt(2) / 2, Math.sqrt(2) / 2, 0);
        Point actualRotatedPoint = aPoint.transform(halfQuarterRotation);

        assertEquals(expectedRotatedPoint, actualRotatedPoint,
                "Should correctly rotate a point around the z axis.");
    }

    private static Stream<Arguments> provideShearsAndExpectedPoints() {
        return Stream.of(
                Arguments.of(new int[] {1, 0, 0, 0, 0, 0}, new int[] {5, 3, 4}),
                Arguments.of(new int[] {0, 1, 0, 0, 0, 0}, new int[] {6, 3, 4}),
                Arguments.of(new int[] {0, 0, 1, 0, 0, 0}, new int[] {2, 5, 4}),
                Arguments.of(new int[] {0, 0, 0, 1, 0, 0}, new int[] {2, 7, 4}),
                Arguments.of(new int[] {0, 0, 0, 0, 1, 0}, new int[] {2, 3, 6}),
                Arguments.of(new int[] {0, 0, 0, 0, 0, 1}, new int[] {2, 3, 7})
        );
    }

    @ParameterizedTest
    @MethodSource("provideShearsAndExpectedPoints")
    void pointShear(int[] shearMatrix, int[] expectedShearedCoords) {
        Transformation aShear = ThreeTransformation.shear(
                shearMatrix[0], shearMatrix[1], shearMatrix[2], shearMatrix[3], shearMatrix[4], shearMatrix[5]);
        Point aPoint = new PointImpl(2, 3, 4);
        Point expectedShearedPoint = new PointImpl(
                expectedShearedCoords[0], expectedShearedCoords[1], expectedShearedCoords[2]);
        Point actualShearedPoint = aPoint.transform(aShear);

        assertEquals(expectedShearedPoint, actualShearedPoint);
    }

    @Test
    void chainingTransformations() {
        Transformation t = ThreeTransformation.rotation_x(Math.PI / 2)
                .scale(5, 5, 5)
                .translate(10, 5, 7);
        Point aPoint = new PointImpl(1, 0, 1);
        Point expectedTransformedPoint = new PointImpl(15, 0, 7);
        Point actualTransformedPoint = aPoint.transform(t);

        assertEquals(expectedTransformedPoint, actualTransformedPoint);
    }
}
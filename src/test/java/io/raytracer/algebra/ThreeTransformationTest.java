package io.raytracer.algebra;

import io.raytracer.algebra.ITransform;
import io.raytracer.algebra.ThreeTransform;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ThreeTransformationTest {
    @Test
    void equalityTest() {
        ITransform t1 = ThreeTransform.translation(1, 2, 3);
        ThreeTransform t2 = ThreeTransform.translation(1, 2, 3);

        assertEquals(t1, t2);
    }

    @Test
    void inequalityTest() {
        ITransform t1 = ThreeTransform.translation(1, 2, 3);
        ITransform t2 = ThreeTransform.translation(0, 2, 3);

        assertNotEquals(t1, t2);
    }

    @Test
    void pointTranslation() {
        ITransform aTranslation = ThreeTransform.translation(5, -3, 2);
        IPoint aPoint = new Point(3, 4, 5);
        IPoint expectedTranslatedPoint = new Point(8, 1, 7);
        IPoint actualTranslatedPoint = aPoint.transform(aTranslation);

        assertEquals(expectedTranslatedPoint, actualTranslatedPoint);
    }

    @Test
    void pointTranslationByInverse() {
        ITransform aTranslation = ThreeTransform.translation(5, -3, 2).inverse();
        IPoint aPoint = new Point(3, 4, 5);
        IPoint expectedTranslatedPoint = new Point(-2, 7, 3);
        IPoint actualTranslatedPoint = aPoint.transform(aTranslation);

        assertEquals(expectedTranslatedPoint, actualTranslatedPoint,
                "Translation by inverse should translate in the opposite direction.");
    }

    @Test
    void vectorTranslation() {
        ITransform aTranslation = ThreeTransform.translation(5, -3, 2);
        IVector aVector = new Vector(3, 4, 5);
        IVector actualTranslatedPoint = aVector.transform(aTranslation);

        assertEquals(aVector, actualTranslatedPoint);
    }

    @Test
    void pointScaling() {
        ITransform aScaling = ThreeTransform.scaling(2, 3, 4);
        IPoint aPoint = new Point(-4, 6, 8);
        IPoint expectedScaledPoint = new Point(-8, 18, 32);
        IPoint actualScaledPoint = aPoint.transform(aScaling);

        assertEquals(expectedScaledPoint, actualScaledPoint);
    }

    @Test
    void vectorScaling() {
        ITransform aScaling = ThreeTransform.scaling(2, 3, 4);
        IVector aVector = new Vector(-4, 6, 8);
        IVector expectedScaledVector = new Vector(-8, 18, 32);
        IVector actualScaledVector = aVector.transform(aScaling);

        assertEquals(expectedScaledVector, actualScaledVector);
    }

    @Test
    void vectorScalingByInverse() {
        ITransform aScaling = ThreeTransform.scaling(2, 3, 4).inverse();
        IVector aVector = new Vector(-4, 6, 8);
        IVector expectedScaledVector = new Vector(-2, 2, 2);
        IVector actualScaledVector = aVector.transform(aScaling);

        assertEquals(expectedScaledVector, actualScaledVector,
                "Scaling by an inverse scales in the opposite direction.");
    }

    @Test
    void pointXRotation() {
        ITransform halfQuarterRotation = ThreeTransform.rotation_x(Math.PI / 4);
        IPoint aPoint = new Point(0, 1, 0);
        IPoint expectedRotatedPoint = new Point(0, Math.sqrt(2) / 2, Math.sqrt(2) / 2);
        IPoint actualRotatedPoint = aPoint.transform(halfQuarterRotation);

        assertEquals(expectedRotatedPoint, actualRotatedPoint);
    }

    @Test
    void rotationXAroundNonZero() {
        ITransform halfQuarter = ThreeTransform.rotation_x(Math.PI / 4, new Point(1, 2, 3));
        IPoint aPoint = new Point(1, 3, 3);
        IPoint expectedRotatedPoint = new Point(1, 2 + Math.sqrt(2)/2, 3 + Math.sqrt(2) / 2);
        IPoint actualRotatedPoint = aPoint.transform(halfQuarter);

        assertEquals(expectedRotatedPoint, actualRotatedPoint);
    }

    @Test
    void pointInverseXRotation() {
        ITransform halfQuarterRotation = ThreeTransform.rotation_x(Math.PI / 4).inverse();
        IPoint aPoint = new Point(0, 1, 0);
        IPoint expectedRotatedPoint = new Point(0, Math.sqrt(2) / 2, -Math.sqrt(2) / 2);
        IPoint actualRotatedPoint = aPoint.transform(halfQuarterRotation);

        assertEquals(expectedRotatedPoint, actualRotatedPoint,
                "Inverse rotation around x axis should rotate counterclockwise.");
    }

    @Test
    void pointYRotation() {
        ITransform halfQuarterRotation = ThreeTransform.rotation_y(Math.PI / 4);
        IPoint aPoint = new Point(0, 0, 1);
        IPoint expectedRotatedPoint = new Point(Math.sqrt(2) / 2, 0, Math.sqrt(2) / 2);
        IPoint actualRotatedPoint = aPoint.transform(halfQuarterRotation);

        assertEquals(expectedRotatedPoint, actualRotatedPoint,
                "Should correctly rotate a point around the y axis.");
    }

    @Test
    void pointZRotation() {
        ITransform halfQuarterRotation = ThreeTransform.rotation_z(Math.PI / 4);
        IPoint aPoint = new Point(0, 1, 0);
        IPoint expectedRotatedPoint = new Point(-Math.sqrt(2) / 2, Math.sqrt(2) / 2, 0);
        IPoint actualRotatedPoint = aPoint.transform(halfQuarterRotation);

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
        ITransform aShear = ThreeTransform.shear(
                shearMatrix[0], shearMatrix[1], shearMatrix[2], shearMatrix[3], shearMatrix[4], shearMatrix[5]);
        IPoint aPoint = new Point(2, 3, 4);
        IPoint expectedShearedPoint = new Point(
                expectedShearedCoords[0], expectedShearedCoords[1], expectedShearedCoords[2]);
        IPoint actualShearedPoint = aPoint.transform(aShear);

        assertEquals(expectedShearedPoint, actualShearedPoint);
    }

    @Test
    void chainingTransformations() {
        ITransform t = ThreeTransform.rotation_x(Math.PI / 2)
                .scale(5, 5, 5)
                .translate(10, 5, 7);
        IPoint aPoint = new Point(1, 0, 1);
        IPoint expectedTransformedPoint = new Point(15, 0, 7);
        IPoint actualTransformedPoint = aPoint.transform(t);

        assertEquals(expectedTransformedPoint, actualTransformedPoint);
    }
}
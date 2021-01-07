package io.raytracer.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class VectorTest {
    @Test
    void additionOfTwoVectors() {
        Vector first = new VectorImpl(1.0, 1.0, 1.0);
        Vector second = new VectorImpl(1.0, 0.0, -1.0);
        Vector expectedSum = new VectorImpl(2.0, 1.0, 0.0);
        Vector actualSum = first.add(second);

        assertEquals(expectedSum, actualSum);
    }

    @Test
    void multiplicationByScalar() {
        Vector vector = new VectorImpl(2.5, -0.4, 1);
        Vector expectedProduct = new VectorImpl(1.25, -0.2, 0.5);
        Vector actualProduct = vector.multiply(0.5);

        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    void norm() {
        Vector vector = new VectorImpl(1, 2, 3);
        assertEquals(Math.sqrt(14), vector.norm(), 1e-3);
    }

    @Test
    void normalisedVectorHasUnitNorm() {
        Vector vector = new VectorImpl(1, 2, 3);
        Vector normalised = vector.normalise();

        assertEquals(normalised.norm(), 1, 1e-3);
    }

    @Test
    void normalisedVectorHasCorrectCoordinates() {
        Vector vector = new VectorImpl(1, 2, 3);
        Vector expectedNormalised = new VectorImpl(1 / Math.sqrt(14), 2 / Math.sqrt(14), 3 / Math.sqrt(14));
        Vector normalised = vector.normalise();

        assertEquals(expectedNormalised, normalised);
    }

    @Test
    void dot() {
        Vector first = new VectorImpl(1, 2, 3);
        Vector second = new VectorImpl(2, 3, 4);
        double expectedProduct = 20;

        assertEquals(expectedProduct, first.dot(second));
    }

    @Test
    void reflect() {
        Vector reflector = new VectorImpl(Math.sqrt(2) / 2, Math.sqrt(2) / 2, 0);
        Vector reflectee = new VectorImpl(0, -1, 0);
        Vector expectedReflected = new VectorImpl(1, 0, 0);
        Vector actualReflected = reflectee.reflect(reflector);

        assertEquals(expectedReflected, actualReflected);
    }
}
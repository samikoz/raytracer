package io.raytracer.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class VectorTest {
    @Test
    void additionOfTwoVectors() {
        IVector first = new Vector(1.0, 1.0, 1.0);
        IVector second = new Vector(1.0, 0.0, -1.0);
        IVector expectedSum = new Vector(2.0, 1.0, 0.0);
        IVector actualSum = first.add(second);

        assertEquals(expectedSum, actualSum);
    }

    @Test
    void multiplicationByScalar() {
        IVector vector = new Vector(2.5, -0.4, 1);
        IVector expectedProduct = new Vector(1.25, -0.2, 0.5);
        IVector actualProduct = vector.multiply(0.5);

        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    void norm() {
        IVector vector = new Vector(1, 2, 3);
        assertEquals(Math.sqrt(14), vector.norm(), 1e-3);
    }

    @Test
    void normalisedVectorHasUnitNorm() {
        IVector vector = new Vector(1, 2, 3);
        IVector normalised = vector.normalise();

        assertEquals(normalised.norm(), 1, 1e-3);
    }

    @Test
    void normalisedVectorHasCorrectCoordinates() {
        IVector vector = new Vector(1, 2, 3);
        IVector expectedNormalised = new Vector(1 / Math.sqrt(14), 2 / Math.sqrt(14), 3 / Math.sqrt(14));
        IVector normalised = vector.normalise();

        assertEquals(expectedNormalised, normalised);
    }

    @Test
    void dot() {
        IVector first = new Vector(1, 2, 3);
        IVector second = new Vector(2, 3, 4);
        double expectedProduct = 20;

        assertEquals(expectedProduct, first.dot(second));
    }

    @Test
    void cross() {
        IVector first = new Vector(1, 2, 3);
        IVector second = new Vector(2, 3, 4);
        IVector expectedCross = new Vector(-1, 2, -1);
        IVector actualCross = first.cross(second);

        assertEquals(expectedCross, actualCross);
    }

    @Test
    void reflect() {
        IVector reflector = new Vector(Math.sqrt(2) / 2, Math.sqrt(2) / 2, 0);
        IVector reflectee = new Vector(0, -1, 0);
        IVector expectedReflected = new Vector(1, 0, 0);
        IVector actualReflected = reflectee.reflect(reflector);

        assertEquals(expectedReflected, actualReflected);
    }
}
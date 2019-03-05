package io.raytracer.mathsy;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TupleComparator {
    static String compareCoordinates(Tuple expected, Tuple actual) {
        String[] coordComparison = new String[expected.dim()];
        for (int coordIndex = 0; coordIndex < expected.dim(); coordIndex++) {
            coordComparison[coordIndex] = expected.get(coordIndex) + " should be " + actual.get(coordIndex);
        }
        return String.join(", ", Arrays.asList(coordComparison));
    }
}

class RealTupleTest {

    @Test
    void testEqualityOfTuplesUpToADelta() {
        Tuple first = new RealTuple(0.0, -2.0, 1e-4);
        Tuple second = new RealTuple(0.0, -2.0, 0.0);

        assertEquals(first, second, "Equality of tuples should be up to a small delta");
    }

}

class RealVectorTest {

    @Test
    void add() {
        Vector first = new RealVector(1.0, 1.0, 1.0);
        Vector second = new RealVector(1.0, 0.0, -1.0);
        Vector expectedSum = new RealVector(2.0, 1.0, 0.0);
        Vector added = first.add(second);

        assertEquals(
                expectedSum, added,
                () -> "Sum of two vectors should be a vector. " +
                        TupleComparator.compareCoordinates(expectedSum, added)
        );
    }

    @Test
    void multiply() {
        Vector vector = new RealVector(2.5, -0.4, 1);
        Vector expectedProduct = new RealVector(1.25, -0.2, 0.5);
        Vector product = vector.multiply(0.5);

        assertEquals(
                expectedProduct, product,
                () -> "A vector times a scalar should be a vector. " +
                        TupleComparator.compareCoordinates(expectedProduct, product)
        );
    }

    @Test
    void norm() {
        Vector vector = new RealVector(1, 2, 3);
        assertEquals(Math.sqrt(14), vector.norm(), 1e-3, "Norm of (1, 2, 3) should be sqrt(14)");
    }


    @Test
    void normalise() {
        Vector vector = new RealVector(1, 2, 3);
        Vector expectedNormalised = new RealVector(1 / Math.sqrt(14), 2 / Math.sqrt(14), 3 / Math.sqrt(14));
        Vector normalised = vector.normalise();

        assertAll(
                "Should return a correctly normalised vector.",
                () -> assertEquals(
                        normalised.norm(), 1, 1e-3, "Normalised vector should have unit norm"
                ),
                () -> assertEquals(
                        expectedNormalised, normalised,
                        () -> TupleComparator.compareCoordinates(expectedNormalised, normalised)
                )
        );
    }

    @Test
    void dot() {
        Vector first = new RealVector(1, 2, 3);
        Vector second = new RealVector(2, 3, 4);
        double expectedProduct = 20;

        assertEquals(expectedProduct, first.dot(second), "(1, 2, 3) dot (2, 3, 4) should equal 20.0");
    }

}

class Real4Immersed3VectorTest {

    @Test
    void cross() {
        ThreeVector first = new Real4Immersed3Vector(1, 2, 3);
        ThreeVector second = new Real4Immersed3Vector(2, 3, 4);
        ThreeVector expectedCrossProduct = new Real4Immersed3Vector(-1, 2, -1);
        ThreeVector crossed = first.cross(second);

        assertEquals(
            expectedCrossProduct, crossed,
                () -> "(1, 2, 3) cross (2, 3, 4) should be a vector. " +
                    TupleComparator.compareCoordinates(expectedCrossProduct, crossed)
        );
    }
}

class RealPointTest {

    @Test
    void additionOfPointAndVector() {
        Point point = new RealPoint(12.0, 3.7, -0.2);
        Vector vector = new RealVector(-5.0, 0.2, 0.0);
        Point expectedSum = new RealPoint(7.0, 3.9, -0.2);
        Point summed = point.add(vector);

        assertEquals(
            expectedSum, summed,
            () -> "Sum of a point and a vector should be a point. " +
                TupleComparator.compareCoordinates(expectedSum, summed)
        );
    }

    @Test
    void subtractionOfTwoPoints() {
        Point first = new RealPoint(-1.0,-1.0,0.0);
        Point second = new RealPoint(2.0,-2.0,5.0);
        Vector expectedDifference = new RealVector(-3.0,1.0,-5.0);
        Vector subtracted = first.subtract(second);

        assertEquals(
            expectedDifference, subtracted,
            () -> "Difference of two points should be a vector. " +
                TupleComparator.compareCoordinates(expectedDifference, subtracted)
        );
    }
}
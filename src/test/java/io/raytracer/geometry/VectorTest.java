package io.raytracer.geometry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class VectorTest {
    @Test
    void additionOfTwoVectors() {
        Vector first = new VectorImpl(1.0, 1.0, 1.0);
        Vector second = new VectorImpl(1.0, 0.0, -1.0);
        Vector expectedSum = new VectorImpl(2.0, 1.0, 0.0);
        Vector actualSum = first.add(second);

        TupleComparator.assertTuplesEqual(expectedSum, actualSum, "Sum of two vectors should be a vector.");
    }

    @Test
    void multiplicationByScalar() {
        Vector vector = new VectorImpl(2.5, -0.4, 1);
        Vector expectedProduct = new VectorImpl(1.25, -0.2, 0.5);
        Vector actualProduct = vector.multiply(0.5);

        TupleComparator.assertTuplesEqual(expectedProduct, actualProduct, "A vector times a scalar should be a vector.");
    }

    @Test
    void norm() {
        Vector vector = new VectorImpl(1, 2, 3);
        assertEquals(Math.sqrt(14), vector.norm(), 1e-3, "Norm of (1, 2, 3) should be sqrt(14)");
    }

    @Test
    void normalise() {
        Vector vector = new VectorImpl(1, 2, 3);
        Vector expectedNormalised = new VectorImpl(1 / Math.sqrt(14), 2 / Math.sqrt(14), 3 / Math.sqrt(14));
        Vector normalised = vector.normalise();

        assertAll(
                "Should return a correctly normalised vector.",
                () -> assertEquals(
                        normalised.norm(), 1, 1e-3, "Normalised vector should have unit norm"
                ),
                () -> TupleComparator.assertTuplesEqual(expectedNormalised, normalised, "Should have appropriate coordinates")
        );
    }

    @Test
    void dot() {
        Vector first = new VectorImpl(1, 2, 3);
        Vector second = new VectorImpl(2, 3, 4);
        double expectedProduct = 20;

        assertEquals(expectedProduct, first.dot(second), "(1, 2, 3) dot (2, 3, 4) should equal 20.0");
    }
}
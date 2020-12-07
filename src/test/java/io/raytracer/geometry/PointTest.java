package io.raytracer.geometry;

import org.junit.jupiter.api.Test;
import io.raytracer.geometry.helpers.TupleComparator;

class PointTest {
    @Test
    void additionOfPointAndVector() {
        Point aPoint = new PointImpl(12.0, 3.7, -0.2);
        Vector aVector = new VectorImpl(-5.0, 0.2, 0.0);
        Point expectedSum = new PointImpl(7.0, 3.9, -0.2);
        Point actualSum = aPoint.add(aVector);

        TupleComparator.assertTuplesEqual(expectedSum, actualSum, "Sum of a point and a vector should be a point.");
    }

    @Test
    void subtractionOfTwoPoints() {
        Point first = new PointImpl(-1.0,-1.0,0.0);
        Point second = new PointImpl(2.0,-2.0,5.0);
        Vector expectedDifference = new VectorImpl(-3.0,1.0,-5.0);
        Vector actualDifference = first.subtract(second);

        TupleComparator.assertTuplesEqual(expectedDifference, actualDifference, "Difference of two points should be a vector.");
    }
}
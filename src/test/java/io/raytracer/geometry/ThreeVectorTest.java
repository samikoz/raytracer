package io.raytracer.geometry;

import org.junit.jupiter.api.Test;


class ThreeVectorTest {
    @Test
    void cross() {
        ThreeVector first = new ThreeVectorImpl(1, 2, 3);
        ThreeVector second = new ThreeVectorImpl(2, 3, 4);
        ThreeVector expectedCross = new ThreeVectorImpl(-1, 2, -1);
        ThreeVector actualCross = first.cross(second);

        TupleComparator.assertTuplesEqual(expectedCross, actualCross, "Cross of two vectors should be a vector.");
    }
}

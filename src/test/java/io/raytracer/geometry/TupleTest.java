package io.raytracer.geometry;

import org.junit.jupiter.api.Test;


class TupleTest {
    @Test
    void equalityOfTuplesUpToADelta() {
        Tuple first = new TupleImpl(0.0, -2.0, 1e-4);
        Tuple second = new TupleImpl(0.0, -2.0, 0.0);

        TupleComparator.assertTuplesEqual(first, second, "Equality of tuples should be up to a small delta");
    }
}

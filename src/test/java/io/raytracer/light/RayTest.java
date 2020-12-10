package io.raytracer.light;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.VectorImpl;
import io.raytracer.geometry.helpers.TupleComparator;
import org.junit.jupiter.api.Test;

class RayTest {
    @Test
    void rayPosition() {
        Ray ray = new RayImpl(new PointImpl(2, 3, 4), new VectorImpl(1, 0, 0));
        Point expectedPosition = new PointImpl(3, 3, 4);
        Point actualPosition = ray.position(1);

        TupleComparator.assertTuplesEqual(expectedPosition, actualPosition,
                "Should correctly compute position of a ray.");
    }
}

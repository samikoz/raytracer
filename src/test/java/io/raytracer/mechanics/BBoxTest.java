package io.raytracer.mechanics;

import io.raytracer.geometry.Interval;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BBoxTest {

    @Test
    void isHit() {
        BBox box = new BBox(new Point(0, 0, 0), new Point(1, 1, 1));

        assertTrue(box.isHit(new Ray(new Point(-1, 0, 0), new Vector(1.5, 0.5, 0.2)), new Interval(0, 5)));
        assertFalse(box.isHit(new Ray(new Point(-2, 0, 0), new Vector(0, 1, 0)), new Interval(-5, 5)));
        assertFalse(box.isHit(new Ray(new Point(-0.5, -0.5, -0.5), new Vector(1, 1, 1)), new Interval(2, 5)));
    }
}
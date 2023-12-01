package io.raytracer.mechanics;

import io.raytracer.geometry.Interval;
import io.raytracer.geometry.Point;
import io.raytracer.algebra.ThreeTransform;
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

    @Test
    void merging() {
        BBox box1 = new BBox(new Point(0, 0, 0), new Point(1, 1, 1));
        BBox box2 = new BBox(new Point(0, -1, 2), new Point(1, 2, 3));
        BBox mergedBox = new BBox(box1, box2);

        assertEquals(0, mergedBox.x.min, 1e-6);
        assertEquals(1, mergedBox.x.max, 1e-6);
        assertEquals(-1, mergedBox.y.min, 1e-6);
        assertEquals(2, mergedBox.y.max, 1e-6);
        assertEquals(0, mergedBox.z.min, 1e-6);
        assertEquals(3, mergedBox.z.max, 1e-6);
    }

    @Test
    void translating() {
        BBox box = new BBox(new Point(0, 0, 0), new Point(1, 1, 1));
        BBox translated = box.transform(ThreeTransform.translation(1, 0, 0));

        assertEquals(1, translated.x.min, 1e-6);
        assertEquals(2, translated.x.max, 1e-6);
        assertEquals(0, translated.y.min, 1e-6);
        assertEquals(1, translated.z.max, 1e-6);
    }

    @Test
    void rotating() {
        BBox box = new BBox(new Point(-1, -1, -1), new Point(1, 1, 1));
        BBox rotated = box.transform(ThreeTransform.rotation_z(Math.PI / 4));

        assertEquals(-1, rotated.z.min, 1e-6);
        assertEquals(1, rotated.z.max, 1e-6);
        assertEquals(Math.sqrt(2), rotated.x.max, 1e-6);
        assertEquals(-Math.sqrt(2), rotated.x.min, 1e-6);
        assertEquals(Math.sqrt(2), rotated.y.max, 1e-6);
        assertEquals(-Math.sqrt(2), rotated.y.min, 1e-6);
    }
}
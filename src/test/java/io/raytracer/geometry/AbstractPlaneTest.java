package io.raytracer.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractPlaneTest {
    @Test
    void doesContain() {
        IPlane aPlane = new AbstractPlane(new Vector(1, 0, 0), new Point(8, 5, 6));

        assertTrue(aPlane.doesContain(new Point(8, -12, 1245)));
        assertFalse(aPlane.doesContain(new Point(7.9, 0 ,0)));
    }

    @Test
    void intersect() {
        IPlane aPlane = new AbstractPlane(new Vector(0, 1, 0), new Point(17, 5, 0));
        ILine aLine = new Line(new Point(-1, -1, 0), new Vector(1, 1, 0));
        IPoint intersection = new Point(5, 5, 0);

        assertEquals(intersection, aPlane.intersect(aLine));
    }

    @Test
    void translate() {
        IVector normal = new Vector(0, 1, 0);
        AbstractPlane aPlane = new AbstractPlane(normal, new Point(0, 0, 0));
        aPlane.setTransform(ThreeTransform.translation(5, 5, 0));

        ILine testLine = new Line(new Point(0, 10, 0), new Vector(0, -1, 0));

        assertEquals(normal, aPlane.getNormal());
        assertEquals(5, testLine.intersect(aPlane), 1e-3);
    }

    @Test
    void rotaterddx_x() {
        IPoint aPoint = new Point(5, 2, 0);
        AbstractPlane aPlane = new AbstractPlane(new Vector(0, 1, 0), aPoint);
        aPlane.setTransform(ThreeTransform.rotation_x(Math.PI / 2));

        ILine testLine = new Line(new Point(0, 0, 0), new Vector(1, 1, 1));

        assertEquals(new Vector(0, 0, 1), aPlane.getNormal());
        assertEquals(new Point(5, 0, 2), aPlane.getPoint());
        assertEquals(2, testLine.intersect(aPlane), 1e-3);
    }

    @Test
    void rotate_y() {
        IPoint aPoint = new Point(5, 0 ,-2);
        AbstractPlane aPlane = new AbstractPlane(new Vector(0, 0, 1), aPoint);
        aPlane.setTransform(ThreeTransform.rotation_y(Math.PI / 2));

        ILine testLine = new Line(new Point(0, 0, 0), new Vector(1, 1, 1));

        assertEquals(new Vector(1, 0, 0), aPlane.getNormal());
        assertEquals(new Point(-2, 0, -5), aPlane.getPoint());
        assertEquals(-2, testLine.intersect(aPlane), 1e-3);
    }
}
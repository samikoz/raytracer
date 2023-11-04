package io.raytracer.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        IPlane aPlane = new AbstractPlane(normal, new Point(0, 0, 0));
        aPlane.translate(new Vector(5, 5, 0));

        ILine testLine = new Line(new Point(0, 10, 0), new Vector(0, -1, 0));

        assertEquals(normal, aPlane.getNormal());
        assertEquals(5, testLine.intersect(aPlane), 1e-3);
    }

    @Test
    void rotate_x() {
        IPoint aPoint = new Point(5, 2, 0);
        IPlane aPlane = new AbstractPlane(new Vector(0, 1, 0), aPoint);
        aPlane.rotate_x(Math.PI / 2);

        ILine testLine = new Line(new Point(0, 0, 0), new Vector(1, 1, 1));

        assertEquals(new Vector(0, 0, 1), aPlane.getNormal());
        assertEquals(aPoint, aPlane.getPoint());
        assertEquals(0, testLine.intersect(aPlane), 1e-3);
    }

    @Test
    void rotate_y() {
        IPoint aPoint = new Point(5, 0 ,-2);
        IPlane aPlane = new AbstractPlane(new Vector(0, 0, 1), aPoint);
        aPlane.rotate_y(Math.PI / 2);

        ILine testLine = new Line(new Point(0, 0, 0), new Vector(1, 1, 1));

        assertEquals(new Vector(1, 0, 0), aPlane.getNormal());
        assertEquals(aPoint, aPlane.getPoint());
        assertEquals(5, testLine.intersect(aPlane), 1e-3);
    }
}
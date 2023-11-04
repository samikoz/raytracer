package io.raytracer.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineTest {

    @Test
    void pointAt() {
        ILine aLine = new Line(new Point(0, 1, 3), new Point(0, 1, 0));
        ILine bLine = new Line(new Point(0, 1,3), new Vector(0, 0, -1));
        IPoint expectedPoint = new Point(0, 1, -3);

        assertEquals(expectedPoint, aLine.pointAt(2));
        assertEquals(expectedPoint, bLine.pointAt(6));
    }

    @Test
    void closestTo() {
        ILine aLine = new Line(new Point(5, 0, -3), new Vector(0, 0, 1));
        IPoint aPoint = new Point(15, 16, 0);
        IPoint closestPoint = new Point(5, 0, 0);

        assertEquals(closestPoint, aLine.closestTo(aPoint));
    }
}
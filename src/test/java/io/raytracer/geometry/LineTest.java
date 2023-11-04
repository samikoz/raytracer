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
}
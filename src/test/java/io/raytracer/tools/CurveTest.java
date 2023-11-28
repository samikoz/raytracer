package io.raytracer.tools;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CurveTest {
    @Test
    void pointAt() {
        Curve c = new Curve(
            Arrays.asList(new Point(0, 0, 0), new Point(1, 1, 1)),
            Arrays.asList(new Vector(0, 0, 0), new Vector(0, 2, -1)));
        IPoint expectedPoint = new Point(0.2, 0.2, 0.2);

        assertEquals(expectedPoint, c.pointAt(0.2));
    }

    @Test
    void tangentAt() {
        Curve c = new Curve(
            Arrays.asList(new Point(0, 0, 0), new Point(1, 1, 1), new Point(2, 3, 4)),
            Arrays.asList(new Vector(0, 0, 0), new Vector(0, 2, -1), new Vector(5, 5, 5))
        );
        IVector expectedVector = new Vector(0, 1, -0.5);
        assertEquals(expectedVector, c.normalAt(0.25));
    }
}
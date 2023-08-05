package io.raytracer.shapes;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;

import static org.junit.jupiter.api.Assertions.*;


class SmoothTriangleTest {
    private final SmoothTriangle testTriangle = new SmoothTriangle(
        new Point(0, 1, 0),
        new Point(-1, 0, 0),
        new Point(1, 0, 0),
        new Vector(0, 1, 0),
        new Vector(-1, 0, 0),
        new Vector(1, 0 ,0)
    );

}
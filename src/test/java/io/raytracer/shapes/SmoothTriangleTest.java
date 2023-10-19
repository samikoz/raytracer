package io.raytracer.shapes;

import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.Ray;
import io.raytracer.mechanics.TextureParameters;
import org.junit.jupiter.api.Test;

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

    @Test
    public void smoothTriangleInterpolatesTheNormal() {
        Intersection testIntersection = new Intersection(
            testTriangle,
            new Ray(new Point(0, 0, 0), new Vector(0, 1, 0)),
            0, new TextureParameters(0.45, 0.25)
        );
        IVector expectedNormal = new Vector(-0.5547, 0.83205, 0);

        assertEquals(expectedNormal, this.testTriangle.normal(testIntersection));
    }
}
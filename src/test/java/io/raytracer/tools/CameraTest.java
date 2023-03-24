package io.raytracer.tools;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.IRay;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CameraTest {
    @Test
    void getRayThroughTheCentreOfCanvas() {
        ICamera camera = new Camera(201, 101, Math.PI / 2);
        IRay ray = camera.getRayThroughPixel(100, 50);

        assertEquals(new Point(0, 0, 0), ray.getOrigin());
        assertEquals(new Vector(0, 0, -1), ray.getDirection());
    }

    @Test
    void getRayThroughCornerOfCanvas() {
        ICamera camera = new Camera(201, 101, Math.PI / 2);
        IRay ray = camera.getRayThroughPixel(0, 0);

        assertEquals(new Point(0, 0, 0), ray.getOrigin());
        assertEquals(new Vector(0.66519, 0.33259, -0.66851), ray.getDirection());
    }

    @Test
    void getRayForATransformedCamera() {
        IPoint eyePosition = new Point(0, 2, -5);
        IVector lookDirection = ThreeTransform.rotation_y(-Math.PI / 4).act(new Vector(0, 0, -1));
        IVector upDirection = new Vector(0, 1, 0);
        ICamera camera = new Camera(201, 101, Math.PI / 2, eyePosition, lookDirection, upDirection);
        IRay ray = camera.getRayThroughPixel(100, 50);

        assertEquals(new Point(0, 2, -5), ray.getOrigin(),
                "Ray's origin should be transformed accordingly to the camera's transformation");
        assertEquals(new Vector(Math.sqrt(2) / 2, 0, -Math.sqrt(2) / 2), ray.getDirection());
    }
}

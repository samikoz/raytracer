package io.raytracer.tools;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Ray;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CameraTest {
    @Test
    void getRayThroughTheCentreOfCanvas() {
        ICamera camera = new Camera(201, 101, Math.PI / 2);
        Collection<IRay> rays = camera.getRaysThroughPixel(100, 50);
        IRay uniqueRay = rays.toArray(new IRay[] {})[0];

        assertEquals(new Point(0, 0, 0), uniqueRay.getOrigin());
        assertEquals(new Vector(0, 0, -1), uniqueRay.getDirection());
    }

    @Test
    void getRayThroughCornerOfCanvas() {
        ICamera camera = new Camera(201, 101, Math.PI / 2);
        Collection<IRay> rays = camera.getRaysThroughPixel(0, 0);
        IRay uniqueRay = rays.toArray(new IRay[] {})[0];

        assertEquals(new Point(0, 0, 0), uniqueRay.getOrigin());
        assertEquals(new Vector(0.66519, 0.33259, -0.66851), uniqueRay.getDirection());
    }

    @Test
    void getRayForATransformedCamera() {
        IPoint eyePosition = new Point(0, 2, -5);
        IVector lookDirection = ThreeTransform.rotation_y(-Math.PI / 4).act(new Vector(0, 0, -1));
        IVector upDirection = new Vector(0, 1, 0);
        ICamera camera = new Camera(201, 101, Math.PI / 2, eyePosition, lookDirection, upDirection);
        Collection<IRay> rays = camera.getRaysThroughPixel(100, 50);
        IRay uniqueRay = rays.toArray(new IRay[] {})[0];

        assertEquals(new Point(0, 2, -5), uniqueRay.getOrigin(),
                "Ray's origin should be transformed accordingly to the camera's transformation");
        assertEquals(new Vector(Math.sqrt(2) / 2, 0, -Math.sqrt(2) / 2), uniqueRay.getDirection());
    }

    @Test
    void transformingCamera() {
        IPoint eyePosition = new Point(1, 0, 0);
        IVector lookDirection = new Vector(-1, 0, 0);
        IVector upDirection = new Vector(0, 0, 1);
        ICamera camera = new Camera(1, 1, Math.PI, eyePosition, lookDirection, upDirection);

        camera.transform(ThreeTransform.rotation_z(-Math.PI / 2));
        Ray expectedRay = new Ray(new Point(0, 1, 0), new Vector(0, -1, 0));
        Collection<IRay> rays = camera.getRaysThroughPixel(0, 0);
        IRay uniqueRay = rays.toArray(new IRay[] {})[0];

        assertEquals(expectedRay, uniqueRay);
    }
}

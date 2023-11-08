package io.raytracer.tools;

import io.raytracer.geometry.IPlane;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Ray;
import io.raytracer.shapes.Plane;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SingleRayCameraTest {
    @Test
    void getRayThroughTheCentreOfCanvas() {
        Camera camera = new SingleRayCamera(201, 101, Math.PI / 2);
        Collection<IRay> rays = camera.getRaysThroughPixel(new Pixel(100, 50));
        IRay uniqueRay = rays.toArray(new IRay[] {})[0];

        assertEquals(new Point(0, 0, 0), uniqueRay.getOrigin());
        assertEquals(new Vector(0, 0, -1), uniqueRay.getDirection());
    }

    @Test
    void getRayThroughCornerOfCanvas() {
        Camera camera = new SingleRayCamera(201, 101, Math.PI / 2);
        Collection<IRay> rays = camera.getRaysThroughPixel(new Pixel(0, 0));
        IRay uniqueRay = rays.toArray(new IRay[] {})[0];

        assertEquals(new Point(0, 0, 0), uniqueRay.getOrigin());
        assertEquals(new Vector(0.66519, 0.33259, -0.66851), uniqueRay.getDirection());
    }

    @Test
    void getRayForATransformedCamera() {
        IPoint eyePosition = new Point(0, 2, -5);
        IVector lookDirection = ThreeTransform.rotation_y(-Math.PI / 4).act(new Vector(0, 0, -1));
        IVector upDirection = new Vector(0, 1, 0);
        Camera camera = new SingleRayCamera(201, 101, Math.PI / 2, eyePosition, lookDirection, upDirection);
        Collection<IRay> rays = camera.getRaysThroughPixel(new Pixel(100, 50));
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
        Camera camera = new SingleRayCamera(1, 1, Math.PI, eyePosition, lookDirection, upDirection);

        camera.transform(ThreeTransform.rotation_z(-Math.PI / 2));
        Ray expectedRay = new Ray(new Point(0, 1, 0), new Vector(0, -1, 0));
        Collection<IRay> rays = camera.getRaysThroughPixel(new Pixel(0, 0));
        IRay uniqueRay = rays.toArray(new IRay[] {})[0];

        assertEquals(expectedRay, uniqueRay);
    }

    @Test
    void projectOntoSensorInvertsRayThroughPixel() {
        IPoint eyePosition = new Point(1, -2, 0);
        IVector lookDirection = new Vector(1, 0, 1.0/3);
        IVector upDirection = new Vector(0, 1, 0);
        Camera camera = new SingleRayCamera(100, 100, Math.PI/3, eyePosition, lookDirection, upDirection);

        IRay aRay = camera.getRayThroughPixel(11.7, 27.8);
        IRay bRay = camera.getRayThroughPixel(70.7, 28.6);
        IPlane aPlane = new Plane(new Vector(1, 2, 3), new Point(-30, 40, -50));
        IPlane bPlane = new Plane(new Vector(-1, 5, 0), new Point(10, 10, 10));
        Optional<IPoint> aIntersection = aPlane.intersect(aRay);
        Optional<Pixel> aPixel = aIntersection.flatMap(camera::projectOnSensorPlane);
        Optional<IPoint> bIntersection = bPlane.intersect(bRay);
        Optional<Pixel> bPixel = bIntersection.flatMap(camera::projectOnSensorPlane);

        assertTrue(aPixel.isPresent());
        assertTrue(bPixel.isPresent());
        assertEquals(new Pixel(12, 28), aPixel.get());
        assertEquals(new Pixel(71, 29), bPixel.get());
    }

    @Test
    void projectingPointsBeyondSight() {
        IPoint eyePosition = new Point(0, 0, 0);
        IVector lookDirection = new Vector(1, 0, 0);
        IVector upDirection = new Vector(0, 1, 0);
        Camera camera = new SingleRayCamera(100, 100, Math.PI/2, eyePosition, lookDirection, upDirection);
        IPoint invisiblePoint = new Point(10, 12, 2);

        Optional<Pixel> aPixel = camera.projectOnSensorPlane(invisiblePoint);

        assertTrue(aPixel.isPresent());
        assertEquals(new Pixel(40, -10), aPixel.get());
    }
}

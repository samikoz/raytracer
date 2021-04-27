package io.raytracer.drawing;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.ThreeTransformation;
import io.raytracer.geometry.Vector;
import io.raytracer.geometry.VectorImpl;
import io.raytracer.light.Ray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CameraTest {
    @Test
    void getRayThroughTheCentreOfCanvas() {
        Camera camera = new CameraImpl(201, 101, Math.PI / 2);
        Ray ray = camera.rayThroughPixel(100, 50);

        assertEquals(new PointImpl(0, 0, 0), ray.getOrigin());
        assertEquals(new VectorImpl(0, 0, -1), ray.getDirection());
    }

    @Test
    void getRayThroughCornerOfCanvas() {
        Camera camera = new CameraImpl(201, 101, Math.PI / 2);
        Ray ray = camera.rayThroughPixel(0, 0);

        assertEquals(new PointImpl(0, 0, 0), ray.getOrigin());
        assertEquals(new VectorImpl(0.66519, 0.33259, -0.66851), ray.getDirection());
    }

    @Test
    void getRayForATransformedCamera() {
        Point eyePosition = new PointImpl(0, 2, -5);
        Vector lookDirection = ThreeTransformation.rotation_y(-Math.PI / 4).act(new VectorImpl(0, 0, -1));
        Vector upDirection = new VectorImpl(0, 1, 0);
        Camera camera = new CameraImpl(201, 101, Math.PI / 2, eyePosition, lookDirection, upDirection);
        Ray ray = camera.rayThroughPixel(100, 50);

        assertEquals(new PointImpl(0, 2, -5), ray.getOrigin(),
                "Ray's origin should be transformed accordingly to the camera's transformation");
        assertEquals(new VectorImpl(Math.sqrt(2) / 2, 0, -Math.sqrt(2) / 2), ray.getDirection());
    }
}

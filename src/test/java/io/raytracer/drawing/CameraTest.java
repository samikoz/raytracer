package io.raytracer.drawing;

import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.ThreeTransformation;
import io.raytracer.geometry.Transformation;
import io.raytracer.geometry.VectorImpl;
import io.raytracer.light.Ray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CameraTest {
    @Test
    void defaultCameraTransformationIsIdentity() {
        Camera camera = new CameraImpl(160, 120, Math.PI / 2);

        assertEquals(new ThreeTransformation(), camera.getTransformation(),
                "Default transformation should be the identity");
    }

    @Test
    void getRayThroughTheCentreOfCanvas() {
        Camera camera = new CameraImpl(201, 101, Math.PI / 2);
        Ray ray = camera.rayThrough(100, 50);

        assertEquals(new PointImpl(0, 0, 0), ray.getOrigin());
        assertEquals(new VectorImpl(0, 0, -1), ray.getDirection());
    }

    @Test
    void getRayThroughCornerOfCanvas() {
        Camera camera = new CameraImpl(201, 101, Math.PI / 2);
        Ray ray = camera.rayThrough(0, 0);

        assertEquals(new PointImpl(0, 0, 0), ray.getOrigin());
        assertEquals(new VectorImpl(0.66519, 0.33259, -0.66851), ray.getDirection());
    }

    @Test
    void getRayForATransformedCamera() {
        Transformation transform = ThreeTransformation.translation(0, -2, 5).rotate_y(Math.PI / 4);
        Camera camera = new CameraImpl(201, 101, Math.PI / 2, transform);
        Ray ray = camera.rayThrough(100, 50);

        assertEquals(new PointImpl(0, 2, -5), ray.getOrigin(),
                "Ray's origin should be transformed accordingly to the camera's transformation");
        assertEquals(new VectorImpl(Math.sqrt(2) / 2, 0, -Math.sqrt(2) / 2), ray.getDirection());
    }
}

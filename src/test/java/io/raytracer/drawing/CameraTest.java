package io.raytracer.drawing;

import io.raytracer.geometry.ThreeTransformation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CameraTest {
    @Test
    void defaultCameraTransformationIsIdentity() {
        Camera camera = new CameraImpl(160, 120, Math.PI / 2);

        assertEquals(new ThreeTransformation(), camera.getTransformation(),
                "Default transformation should be the identity");
    }
}

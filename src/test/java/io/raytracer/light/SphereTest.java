package io.raytracer.light;

import io.raytracer.geometry.ThreeTransformation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SphereTest {
    @Test
    void getDefaultTransform() {
        Sphere sphere = new SphereImpl();

        assertEquals(new ThreeTransformation(), sphere.getTransform(),
                "Default transformation should be the identity.");
    }
}

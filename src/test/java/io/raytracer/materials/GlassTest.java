package io.raytracer.materials;


import io.raytracer.materials.Glass;
import io.raytracer.materials.Material;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlassTest {
    @Test
    void creatingGlass() {
        Material aGlass = Glass.glassBuilder().ambient(1.0).build();

        assertEquals(1, aGlass.transparency);
        assertEquals(1.5, aGlass.refractiveIndex);
        assertEquals(1, aGlass.ambient);
    }
}
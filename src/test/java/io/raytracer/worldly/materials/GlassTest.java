package io.raytracer.worldly.materials;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlassTest {
    @Test
    void creatingGlass() {
        Glass aGlass = Glass.builder().ambient(1).build();

        assertEquals(1, aGlass.transparency);
        assertEquals(1.5, aGlass.refractive_index);
        assertEquals(1, aGlass.ambient);
    }
}
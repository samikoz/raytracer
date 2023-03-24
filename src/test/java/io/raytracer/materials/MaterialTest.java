package io.raytracer.materials;

import io.raytracer.materials.Material;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MaterialTest {
    @Test
    void materialEquality() {
        Material material1 = Material.builder().diffuse(2.5).build();
        Material material2 = Material.builder().diffuse(2.5005).build();

        assertEquals(material1, material2);
    }

    @Test
    void materialInequality() {
        Material material1 = Material.builder().shininess(0.5).build();
        Material material2 = Material.builder().ambient(0.2).build();

        assertNotEquals(material1, material2);
    }
}

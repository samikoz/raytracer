package io.raytracer.light;

import io.raytracer.drawing.ColourImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MaterialTest {
    @Test
    void materialEquality() {
        Material material1 = new Material();
        material1.diffuse(2.5);
        Material material2 = new Material();
        material2.diffuse(2.5005);

        assertEquals(material1, material2, "Should correctly determine material equality.");
    }

    @Test
    void materialInequality() {
        Material material1 = new Material();
        material1.shininess(0.5);
        Material material2 = new Material();
        material2.ambient(0.2);

        assertNotEquals(material1, material2, "Should correctly determine material inequality.");
    }

    @Test
    void materialDefaultValues() {
        Material material = new Material();

        assertEquals(new ColourImpl(1, 1, 1), material.colour, "Default colour should be black.");
        assertEquals(0.1, material.ambient, "Ambient default value should be -1.");
        assertEquals(0.9, material.diffuse, "Default diffuse value should be 0.9.");
        assertEquals(0.9, material.specular, "Default specular value should be 0.9.");
        assertEquals(200.0, material.shininess, "Default shininess value should be 200.0.");
    }
}

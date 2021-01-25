package io.raytracer.light;


import io.raytracer.drawing.ColourImpl;
import io.raytracer.drawing.Material;
import io.raytracer.drawing.Sphere;
import io.raytracer.drawing.SphereImpl;
import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.ThreeTransformation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorldTest {
    static Sphere firstSphere;
    static Sphere secondSphere;
    static World defaultWorld;

    @BeforeAll
    static void setupMaterialAndPosition() {
        Material defaultMaterial = new Material();
        defaultMaterial.colour = new ColourImpl(0.8, 1.0, 0.6);
        defaultMaterial.diffuse = 0.7;
        defaultMaterial.specular = 0.2;
        firstSphere = new SphereImpl(defaultMaterial);
        secondSphere = new SphereImpl(defaultMaterial);
        secondSphere.setTransform(ThreeTransformation.scaling(0.5, 0.5, 0.5));

        defaultWorld = new WorldImpl(
                new LightSource(new ColourImpl(1, 1, 1), new PointImpl(-10, 10, -10)));
        defaultWorld.put(firstSphere);
        defaultWorld.put(secondSphere);
    }

    @Test
    void containsObjectsThatWerePut() {
        assertTrue(defaultWorld.contains(firstSphere) && defaultWorld.contains(secondSphere));
    }
}

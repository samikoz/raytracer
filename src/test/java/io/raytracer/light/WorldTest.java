package io.raytracer.light;


import io.raytracer.drawing.ColourImpl;
import io.raytracer.drawing.Material;
import io.raytracer.drawing.Sphere;
import io.raytracer.drawing.SphereImpl;
import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.ThreeTransformation;
import io.raytracer.geometry.VectorImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
                new LightSourceImpl(new ColourImpl(1, 1, 1), new PointImpl(-10, 10, -10)));
        defaultWorld.put(firstSphere);
        defaultWorld.put(secondSphere);
    }

    @Test
    void containsObjectsThatWerePut() {
        assertTrue(defaultWorld.contains(firstSphere) && defaultWorld.contains(secondSphere));
    }

    @Test
    void intersectingEmptyWorld() {
        Ray ray = new RayImpl(new PointImpl(0, 0, -5), new VectorImpl(0, 0, 1));
        World world = new WorldImpl(
                new LightSourceImpl(new ColourImpl(1, 1, 1), new PointImpl(-10, 10, -10)));

        assertEquals(0, world.intersect(ray).count(), "Empty world should have no intersections.");
    }

    @Test
    void intersecting() {
        Ray ray = new RayImpl(new PointImpl(0, 0, -5), new VectorImpl(0, 0, 1));
        IntersectionList intersections = defaultWorld.intersect(ray);

        assertEquals(intersections.get(0).time, 4);
        assertEquals(intersections.get(1).time, 4.5);
        assertEquals(intersections.get(2).time, 5.5);
        assertEquals(intersections.get(3).time, 6);
    }
}

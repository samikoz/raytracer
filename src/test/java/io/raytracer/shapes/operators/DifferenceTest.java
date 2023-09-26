package io.raytracer.shapes.operators;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.Ray;
import io.raytracer.mechanics.RayHit;
import io.raytracer.shapes.Shape;
import io.raytracer.shapes.Sphere;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.textures.Textures;
import io.raytracer.tools.IColour;
import io.raytracer.tools.LinearColour;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class DifferenceTest {
    @Test
    void rayHittingDifferenceOfTwoSpheres() {
        Shape leftSphere = new Sphere();
        Shape rightSphere = new Sphere();
        rightSphere.setTransform(ThreeTransform.translation(0.5, 0, 0));
        Shape diff = new Difference(rightSphere, leftSphere);

        IRay ray = new Ray(new Point(-2, 0, 0), new Vector(1, 0, 0));
        Intersection[] inters = diff.intersect(ray);

        assertEquals(2, inters.length);
        assertEquals(3, inters[0].rayParameter);
        assertEquals(3.5, inters[1].rayParameter);
    }

    @Test
    void coloringDifferenceOfTwoSpheres() {
        Shape leftSphere = new Sphere(Material.builder().texture(new MonocolourTexture(new LinearColour(1, 0, 0))).build());
        Shape rightSphere = new Sphere(Material.builder().texture(new MonocolourTexture(new LinearColour(0, 1, 0))).build());
        rightSphere.setTransform(ThreeTransform.translation(0.5, 0, 0));
        Shape diff = new Difference(rightSphere, leftSphere);

        IRay ray = new Ray(new Point(-2, 0, 0), new Vector(1, 0, 0));
        Intersection[] inters = diff.intersect(ray);
        Optional<RayHit> hitpoint = RayHit.fromIntersections(Arrays.stream(inters).collect(Collectors.toList()));

        assertTrue(hitpoint.isPresent());
        assertEquals(new LinearColour(1, 0, 0), diff.getIntrinsicColour(hitpoint.get()));
    }
}
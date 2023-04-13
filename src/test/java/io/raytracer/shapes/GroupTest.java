package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.LightSource;
import io.raytracer.mechanics.Ray;
import io.raytracer.textures.StripedTexture;
import io.raytracer.tools.Colour;
import io.raytracer.tools.IColour;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {

    @Test
    void addShapeSetsItsParent() {
        Shape sphere = new Sphere();
        Group group = new Group();

        group.add(sphere);
        Optional<Group> parent = sphere.getParent();

        assertTrue(parent.isPresent());
        assertEquals(group, parent.get());
    }

    @Test
    void intersectForEmptyGroup() {
        Group group = new Group();
        IRay ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));

        assertEquals(0, group.intersect(ray).length);
    }

    @Test
    void intersectAggregatesAndSortsIntersections() {
        Group group = new Group();
        Shape firstSphere = new Sphere();
        Shape secondSphere = new Sphere();
        secondSphere.setTransform(ThreeTransform.translation(0, 0, -3));
        Shape thirdSphere = new Sphere();
        thirdSphere.setTransform(ThreeTransform.translation(5, 0, 0));
        group.add(firstSphere).add(secondSphere).add(thirdSphere);
        IRay ray = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));

        Intersection[] intersections = group.intersect(ray);

        assertEquals(4, intersections.length);
        assertEquals(secondSphere, intersections[0].object);
        assertEquals(secondSphere, intersections[1].object);
        assertEquals(firstSphere, intersections[2].object);
        assertEquals(firstSphere, intersections[3].object);
    }

    @Test
    void intersectTransformedGroup() {
        Group group = new Group();
        group.setTransform(ThreeTransform.scaling(2, 2, 2));
        Shape sphere = new Sphere();
        sphere.setTransform(ThreeTransform.translation(5, 0, 0));
        group.add(sphere);
        IRay ray = new Ray(new Point(10, 0, -10), new Vector(0, 0, 1));

        Intersection[] intersections = group.intersect(ray);

        assertEquals(2, intersections.length);
    }
}
package io.raytracer.shapes;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.Ray;
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

    @Test
    void boundingBoxMergedOverChildrens() {
        Group g = new Group();
        Shape sphere = new Sphere();
        sphere.setTransform(ThreeTransform.scaling(2, 2, 2));
        Shape cube = new Cube();
        cube.setTransform(ThreeTransform.translation(0, 2, 3));
        g.add(sphere).add(cube);
        BBox gbox = g.getBoundingBox().get();

        assertEquals(-2, gbox.x.min, 1e-6);
        assertEquals(2, gbox.x.max, 1e-6);
        assertEquals(-2, gbox.y.min, 1e-6);
        assertEquals(3, gbox.y.max, 1e-6);
        assertEquals(-2, gbox.z.min, 1e-6);
        assertEquals(4, gbox.z.max, 1e-6);
    }
}
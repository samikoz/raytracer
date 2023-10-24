package io.raytracer.shapes;

import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Ray;
import io.raytracer.mechanics.RayHit;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GroupTest {

    @Test
    void setTransformSetsChildrensParent() {
        Shape sphere1 = new Sphere();
        Shape sphere2 = new Sphere();
        Group group = new Group(new Hittable[]{sphere1, sphere2});

        assertFalse(sphere1.getParent().isPresent());
        assertFalse(sphere2.getParent().isPresent());

        group.setTransform(ThreeTransform.scaling(1, 2, 1));

        assertTrue(sphere1.getParent().isPresent());
        assertEquals(group, sphere1.getParent().get());
        assertTrue(sphere2.getParent().isPresent());
        assertEquals(group, sphere2.getParent().get());
    }

    @Test
    void intersectTransformedGroup() {
        Shape sphere = new Sphere();
        sphere.setTransform(ThreeTransform.translation(5, 0, 0));
        Group group = new Group(new Hittable[]{sphere});
        group.setTransform(ThreeTransform.scaling(2, 2, 2));
        IRay ray = new Ray(new Point(10, 0, -10), new Vector(0, 0, 1));

        Optional<RayHit> hit = group.intersect(ray);

        assertTrue(hit.isPresent());
    }

    @Test
    void intersectTransformedGroupWithinUntransformed() {
        Shape sphereLeft = new Sphere();
        sphereLeft.setTransform(ThreeTransform.translation(-2, 0, 0));
        Shape sphereRight = new Sphere();
        sphereRight.setTransform(ThreeTransform.translation(2, 0, 0));
        Group transformedGroup = new Group(new Hittable[] {sphereRight});
        transformedGroup.setTransform(ThreeTransform.translation(2, 0, 0));
        Group untransformedGroup = new Group(new Hittable[] {sphereLeft, transformedGroup});
        IVector upVector = new Vector(0, 0, 1);

        IRay rightHittingRay = new Ray(new Point(3.5, 0, -2), upVector);
        IRay rightMissingRay = new Ray(new Point(2, 0, -2), upVector);
        IRay centreMissingRay = new Ray(new Point(0, 0, -2), upVector);
        IRay leftHittingRay = new Ray(new Point(-2.5, 0, -2), upVector);

        assertTrue(untransformedGroup.intersect(rightHittingRay).isPresent());
        assertFalse(untransformedGroup.intersect(rightMissingRay).isPresent());
        assertFalse(untransformedGroup.intersect(centreMissingRay).isPresent());
        assertTrue(untransformedGroup.intersect(leftHittingRay).isPresent());
    }

    @Test
    void boundingBoxMergedOverChildren() {
        Shape sphere = new Sphere();
        sphere.setTransform(ThreeTransform.scaling(2, 2, 2));
        Shape cube = new Cube();
        cube.setTransform(ThreeTransform.translation(0, 2, 3));
        Group g = new Group(new Hittable[] {sphere, cube});
        BBox gbox = g.getBoundingBox();

        assertEquals(-2, gbox.x.min, 1e-6);
        assertEquals(2, gbox.x.max, 1e-6);
        assertEquals(-2, gbox.y.min, 1e-6);
        assertEquals(3, gbox.y.max, 1e-6);
        assertEquals(-2, gbox.z.min, 1e-6);
        assertEquals(4, gbox.z.max, 1e-6);
    }
}
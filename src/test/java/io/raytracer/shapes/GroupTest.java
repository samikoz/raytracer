package io.raytracer.shapes;

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
}
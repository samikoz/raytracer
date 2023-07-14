package io.raytracer.mechanics;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.shapes.Plane;
import io.raytracer.shapes.Shape;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.tools.LinearColour;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class LambertianWorldTest {
    static class OscillatoryCaller {
        int counter;

        OscillatoryCaller() {
            counter = 0;
        }

        int call() {
            return counter++ % 2;
        }
    }


    @Test
    void illuminateWithRecasters() {
        Material topMaterial = Material.builder().emit(new LinearColour(1, 0, 0)).build();
        Shape topWall = new Plane(topMaterial);
        topWall.setTransform(ThreeTransform.translation(0, 2, 0));

        Material bottomMaterial = Material.builder().emit(new LinearColour(0, 1, 0)).build();
        Shape bottomWall = new Plane(bottomMaterial);
        bottomWall.setTransform(ThreeTransform.translation(0, -2 ,0));

        Material middleMaterial = Material.builder().texture(new MonocolourTexture(new LinearColour(1, 1, 1))).build();
        middleMaterial.addRecaster(rayHit -> Collections.singletonList(new Ray(new Point(0, 0, 0), new Vector(0, 1, 0))), 0.2);
        middleMaterial.addRecaster(rayHit -> Collections.singletonList(new Ray(new Point(0, 0, 0), new Vector(0, -1, 0))), 0.8);
        Shape middleWall = new Plane(middleMaterial);

        OscillatoryCaller caller = new OscillatoryCaller();
        World world = new LambertianWorld(() -> (new float[] {0.1F, 0.3F })[caller.call()]);

        world.put(bottomWall).put(topWall).put(middleWall);

        IRay ray = new Ray(new Point(0, 1, 0), new Vector(0, -1, 0));
        assertEquals(new LinearColour(1, 0, 0), world.illuminate(ray));
        assertEquals(new LinearColour(0, 1, 0), world.illuminate(ray));
    }
}
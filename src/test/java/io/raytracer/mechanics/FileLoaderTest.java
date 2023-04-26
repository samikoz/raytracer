package io.raytracer.mechanics;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileLoaderTest {

    @Test
    void populate(@TempDir File tempdir) throws IOException {
        File testInput = new File(tempdir, "test.mth");
        List<String> lines = Arrays.asList("Sphere(P(1.0,2.0,3.0),0.1)", "Sphere(P(-1.0,-2.0,-3.0),0.1)");
        Files.write(testInput.toPath(), lines);
        Loader loader = new FileLoader(Material.builder().build());
        World world = new World();
        IRay firstRay = new Ray(new Point(0, 2, 3), new Vector(1, 0, 0));
        IRay secondRay = new Ray(new Point(-1, 0, -3), new Vector(0, -1, 0));

        loader.load(testInput);
        loader.populate(world);

        assertEquals(2, world.intersect(firstRay).size());
        assertEquals(2, world.intersect(secondRay).size());
    }
}
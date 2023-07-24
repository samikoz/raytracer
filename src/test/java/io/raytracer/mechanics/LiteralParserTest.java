package io.raytracer.mechanics;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.shapes.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LiteralParserTest {

    @Test
    void parsing(@TempDir File tempdir) throws IOException {
        File testInput = new File(tempdir, "test.mth");
        List<String> lines = Arrays.asList("Sphere(P(1.0,2.0,3.0),0.1)", "Sphere(P(-1.0,-2.0,-3.0),0.1)");
        Files.write(testInput.toPath(), lines);
        Parser parser = new LiteralParser(Material.builder().build());

        parser.parse(testInput);
        Group group = parser.getParsed();

        assertEquals(2, group.children.size());
    }
}
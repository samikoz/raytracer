package io.raytracer.mechanics;

import io.raytracer.materials.Material;
import io.raytracer.shapes.Hittable;
import io.raytracer.tools.parsers.LiteralParser;
import io.raytracer.tools.parsers.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LiteralParserTest {

    @Test
    void parsing(@TempDir File tempdir) throws IOException {
        File testInput = new File(tempdir, "test.mth");
        List<String> lines = Arrays.asList("Sphere(P(1.0,2.0,3.0),0.1)", "Sphere(P(-1.0,-2.0,-3.0),0.1)");
        Files.write(testInput.toPath(), lines);
        Parser parser = new LiteralParser(Material.builder().build());

        parser.parse(testInput);
        List<Hittable> group = parser.getParsed();

        assertEquals(2, group.size());
    }
}
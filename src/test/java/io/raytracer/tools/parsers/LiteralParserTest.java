package io.raytracer.tools.parsers;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
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
    void parsingSpheres(@TempDir File tempdir) throws IOException {
        File testInput = new File(tempdir, "test.mth");
        List<String> lines = Arrays.asList("Sphere(P(1.0,2.0,3.0),0.1)", "Sphere(P(-1.0,-2.0,-3.0),0.1)");
        Files.write(testInput.toPath(), lines);
        Parser parser = new LiteralParser(Material.builder().build());

        parser.parse(testInput);
        List<Hittable> group = parser.getParsed();

        assertEquals(2, group.size());
    }

    @Test
    void parsingCurvepoints(@TempDir File tempdir) throws IOException {
        File testInput = new File(tempdir, "test.mth");
        List<String> lines = Arrays.asList(
            "CurvePoint(P(160.346,449.204),V(0.852,0.523))",
            "CurvePoint(P(161.880,446.648),V(0.862,0.507))"
        );
        Files.write(testInput.toPath(), lines);
        LiteralParser parser = new LiteralParser();

        parser.parse(testInput);
        List<IPoint> points = parser.getParsedPoints();
        List<IVector> vectors = parser.getParsedVectors();

        assertEquals(new Point(160.346, 449.204, 0), points.get(0));
        assertEquals(new Vector(0.862, 0.507, 0), vectors.get(1));
    }
}
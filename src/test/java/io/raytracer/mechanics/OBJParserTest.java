package io.raytracer.mechanics;

import io.raytracer.geometry.Point;
import io.raytracer.shapes.Group;
import io.raytracer.shapes.Shape;
import io.raytracer.shapes.Triangle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OBJParserTest {
    @Test
    void parseGibberishFile(@TempDir File tempdir) throws IOException {
        File testInput = new File(tempdir, "test.mth");
        List<String> lines = Arrays.asList("what a", "nonsense");
        Files.write(testInput.toPath(), lines);
        OBJParser parser = new OBJParser();
        parser.parse(testInput);

        assertEquals(1, parser.vertices.size(), "Parser ignores gibberish input");
        assertNull(parser.vertices.get(0));
    }

    @Test
    void parseVertexData(@TempDir File tempdir) throws IOException {
        File testInput = new File(tempdir, "test.mth");
        List<String> lines = Arrays.asList(
                "v -1 1 0",
                "v -1.0000 0.5000 0.0000",
                "v 1 0 0",
                "v 1 1 0"
        );
        Files.write(testInput.toPath(), lines);
        OBJParser parser = new OBJParser();
        parser.parse(testInput);

        assertEquals(5, parser.vertices.size());
        assertEquals(new Point(-1, 1, 0), parser.vertices.get(1));
        assertEquals(new Point(-1, 0.5, 0), parser.vertices.get(2));
        assertEquals(new Point(1, 0, 0), parser.vertices.get(3));
        assertEquals(new Point(1, 1, 0), parser.vertices.get(4));
    }

    @Test
    void parseTriangleFaces(@TempDir File tempdir) throws IOException {
        File testInput = new File(tempdir, "test.mth");
        List<String> lines = Arrays.asList(
                "v -1 1 0",
                "v -1 0 0",
                "v 1 0 0",
                "v 1 1 0",
                "",
                "f 1 2 3",
                "f 1 3 4"
        );
        Files.write(testInput.toPath(), lines);
        OBJParser parser = new OBJParser();
        parser.parse(testInput);
        List<Shape> parsed = parser.getParsed().children;

        assertEquals(2, parsed.size());
        Triangle firstParsed = (Triangle) parsed.get(0);
        Triangle secondParsed = (Triangle) parsed.get(1);

        assertEquals(parser.vertices.get(1), firstParsed.v1);
        assertEquals(parser.vertices.get(2), firstParsed.v2);
        assertEquals(parser.vertices.get(3), firstParsed.v3);
        assertEquals(parser.vertices.get(1), secondParsed.v1);
        assertEquals(parser.vertices.get(3), secondParsed.v2);
        assertEquals(parser.vertices.get(4), secondParsed.v3);
    }

    @Test
    void parsePolygonData(@TempDir File tempdir) throws IOException {
        File testInput = new File(tempdir, "test.mth");
        List<String> lines = Arrays.asList(
                "v -1 1 0",
                "v -1 0 0",
                "v 1 0 0",
                "v 1 1 0",
                "v 0 2 0",
                "",
                "f 1 2 3 4 5"
        );
        Files.write(testInput.toPath(), lines);
        OBJParser parser = new OBJParser();
        parser.parse(testInput);
        List<Shape> parsed = parser.getParsed().children;

        assertEquals(3, parsed.size());
        Triangle firstParsed = (Triangle) parsed.get(0);
        Triangle secondParsed = (Triangle) parsed.get(1);
        Triangle thirdParsed = (Triangle) parsed.get(2);

        assertEquals(parser.vertices.get(1), firstParsed.v1);
        assertEquals(parser.vertices.get(2), firstParsed.v2);
        assertEquals(parser.vertices.get(3), firstParsed.v3);
        assertEquals(parser.vertices.get(1), secondParsed.v1);
        assertEquals(parser.vertices.get(3), secondParsed.v2);
        assertEquals(parser.vertices.get(4), secondParsed.v3);
        assertEquals(parser.vertices.get(1), thirdParsed.v1);
        assertEquals(parser.vertices.get(4), thirdParsed.v2);
        assertEquals(parser.vertices.get(5), thirdParsed.v3);
    }

    @Test
    void parseTrianglesInGroups(@TempDir File tempdir) throws IOException {
        File testInput = new File(tempdir, "test.mth");
        List<String> lines = Arrays.asList(
            "v -1 1 0",
            "v -1 0 0",
            "v 1 0 0",
            "v 1 1 0",
            "",
            "g FirstGroup",
            "f 1 2 3",
            "g SecondGroup",
            "f 1 3 4"
        );
        Files.write(testInput.toPath(), lines);
        OBJParser parser = new OBJParser();
        parser.parse(testInput);
        List<Shape> parsed = parser.getParsed().children;

        assertEquals(2, parsed.size());
        Group firstGroup = (Group) parsed.get(0);
        Group secondGroup = (Group) parsed.get(1);
        assertEquals(1, firstGroup.children.size());
        assertEquals(1, firstGroup.children.size());
        Triangle firstTriangle = (Triangle) firstGroup.children.get(0);
        Triangle secondTriangle = (Triangle) secondGroup.children.get(0);

        assertEquals(parser.vertices.get(1), firstTriangle.v1);
        assertEquals(parser.vertices.get(2), firstTriangle.v2);
        assertEquals(parser.vertices.get(3), firstTriangle.v3);
        assertEquals(parser.vertices.get(1), secondTriangle.v1);
        assertEquals(parser.vertices.get(3), secondTriangle.v2);
        assertEquals(parser.vertices.get(4), secondTriangle.v3);
    }
}
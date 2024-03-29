package io.raytracer.mechanics;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.shapes.Group;
import io.raytracer.shapes.Hittable;
import io.raytracer.shapes.SmoothTriangle;
import io.raytracer.shapes.Triangle;
import io.raytracer.tools.parsers.OBJParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
        List<Hittable> parsed = parser.getParsed();

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
        List<Hittable> parsed = parser.getParsed();

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
        List<Hittable> parsed = parser.getParsed();

        assertEquals(2, parsed.size());
        Group firstGroup = (Group) parsed.get(0);
        Group secondGroup = (Group) parsed.get(1);
        assertEquals(1, firstGroup.getChildren().size());
        assertEquals(1, firstGroup.getChildren().size());
        Triangle firstTriangle = (Triangle) firstGroup.getChildren().get(0);
        Triangle secondTriangle = (Triangle) secondGroup.getChildren().get(0);

        assertEquals(parser.vertices.get(1), firstTriangle.v1);
        assertEquals(parser.vertices.get(2), firstTriangle.v2);
        assertEquals(parser.vertices.get(3), firstTriangle.v3);
        assertEquals(parser.vertices.get(1), secondTriangle.v1);
        assertEquals(parser.vertices.get(3), secondTriangle.v2);
        assertEquals(parser.vertices.get(4), secondTriangle.v3);
    }

    @Test
    void parseVertexNormals(@TempDir File tempdir) throws IOException {
        File testInput = new File(tempdir, "test.mth");
        List<String> lines = Arrays.asList(
            "vn 0 0 1",
            "vn 0.707 0 -0.707",
            "vn 1 2 3"
        );
        Files.write(testInput.toPath(), lines);
        OBJParser parser = new OBJParser();
        parser.parse(testInput);

        assertEquals(4, parser.normals.size());
        assertEquals(new Vector(0, 0, 1), parser.normals.get(1));
        assertEquals(new Vector(0.707, 0, -0.707), parser.normals.get(2));
        assertEquals(new Vector(1, 2, 3), parser.normals.get(3));
    }

    @Test
    void parseFacesWithNormals(@TempDir File tempdir) throws IOException {
        File testInput = new File(tempdir, "test.mth");
        List<String> lines = Arrays.asList(
            "v 0 1 0",
            "v -1 0 0",
            "v 1 0 0",
            "",
            "vn -1 0 0",
            "vn 1 0 0",
            "vn 0 1 0",
            "",
            "f 1//3 2//1 3//2",
            "f 1/0/3 2/102/1 3/14/2"
        );
        Files.write(testInput.toPath(), lines);
        OBJParser parser = new OBJParser();
        parser.parse(testInput);

        List<Hittable> parsed = parser.getParsed();
        assertEquals(2, parsed.size());
        SmoothTriangle firstChild = (SmoothTriangle) parsed.get(0);
        SmoothTriangle secondChild = (SmoothTriangle) parsed.get(1);

        assertEquals(parser.vertices.get(1), firstChild.v1);
        assertEquals(parser.vertices.get(2), firstChild.v2);
        assertEquals(parser.vertices.get(3), firstChild.v3);
        assertEquals(parser.normals.get(3), firstChild.n1);
        assertEquals(parser.normals.get(1), firstChild.n2);
        assertEquals(parser.normals.get(2), firstChild.n3);
        assertEquals(secondChild, firstChild);
    }
}
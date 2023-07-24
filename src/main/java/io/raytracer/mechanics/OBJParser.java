package io.raytracer.mechanics;

import io.raytracer.geometry.Point;
import io.raytracer.shapes.Group;
import io.raytracer.shapes.Triangle;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OBJParser implements Parser {
    public List<Point> vertices;
    @Getter private final Group parsed;
    private Group currentlyParsed;

    private final String floatPoint = "(-?\\d+(?:\\.\\d+)*)";
    private final Pattern vertexPattern = Pattern.compile("v " + floatPoint + " " + floatPoint + " " + floatPoint);
    private final Pattern facePattern = Pattern.compile("f((?: \\d){3,})");
    private final Pattern groupPattern = Pattern.compile("g \\w+");

    public OBJParser() {
        this.vertices = new ArrayList<>();
        this.vertices.add(null);
        this.parsed = new Group();

        this.currentlyParsed = this.parsed;
    }

    @Override
    public void parseLine(String line) {
        Matcher vertexMatcher = vertexPattern.matcher(line);
        if (vertexMatcher.find()) {
            this.vertices.add(this.parsePoint(vertexMatcher));
        }
        Matcher faceMatcher = facePattern.matcher(line);
        if (faceMatcher.find()) {
            this.parseFace(faceMatcher).forEach(this.currentlyParsed::add);
        }
        Matcher groupMatcher = groupPattern.matcher(line);
        if (groupMatcher.find()) {
            Group newGroup = new Group();
            this.parsed.add(newGroup);
            this.currentlyParsed = newGroup;
        }
    }

    private Point parsePoint(Matcher vertexMatcher) {
        return new Point(
            Double.parseDouble(vertexMatcher.group(1)),
            Double.parseDouble(vertexMatcher.group(2)),
            Double.parseDouble(vertexMatcher.group(3))
        );
    }

    private List<Triangle> parseFace(Matcher faceMatcher) {
        int[] vertexIndices = Arrays.stream(faceMatcher.group().substring(2).split(" ")).mapToInt(Integer::parseInt).toArray();
        return IntStream.range(1, vertexIndices.length-1).mapToObj(index -> new Triangle(
                this.vertices.get(vertexIndices[0]),
                this.vertices.get(vertexIndices[index]),
                this.vertices.get(vertexIndices[index+1])
        )).collect(Collectors.toList());
    }
}

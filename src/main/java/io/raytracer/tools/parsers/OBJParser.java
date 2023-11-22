package io.raytracer.tools.parsers;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.shapes.Group;
import io.raytracer.shapes.Hittable;
import io.raytracer.shapes.SmoothTriangle;
import io.raytracer.shapes.Triangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OBJParser implements Parser {
    public List<IPoint> vertices;
    public List<IVector> normals;
    private final List<List<Hittable>> parsed;
    private List<Hittable> currentlyParsed;
    private final Material material;

    private final String floatPoint = "(-?\\d+(?:\\.\\d+)*)";
    private final Pattern vertexPattern = Pattern.compile("v " + floatPoint + " " + floatPoint + " " + floatPoint);
    private final Pattern facePattern = Pattern.compile("f(?: (\\S*)){3,}");
    private final Pattern groupPattern = Pattern.compile("g \\w+");
    private final Pattern normalPattern = Pattern.compile("vn " + floatPoint + " " + floatPoint + " " + floatPoint);

    public OBJParser() {
        this(Material.builder().build());
    }

    public OBJParser(Material material) {
        this.vertices = new ArrayList<>();
        this.normals = new ArrayList<>();
        this.vertices.add(null);
        this.normals.add(null);
        this.parsed = new ArrayList<>();
        this.material = material;

        this.currentlyParsed = new ArrayList<>();
        this.parsed.add(currentlyParsed);
    }

    @Override
    public List<Hittable> getParsed() {
        if (this.parsed.size() == 1) {
            return this.parsed.get(0);
        }
        else {
            List<Hittable> parsedList = this.parsed.get(0);
            Stream<Hittable> parsedGroups = this.parsed.stream()
                    .skip(1).map(hitlist -> new Group(hitlist.toArray(new Hittable[] {})));
            return Stream.concat(parsedList.stream(), parsedGroups).collect(Collectors.toList());
        }
    }

    @Override
    public void parseLine(String line) {
        Matcher vertexMatcher = vertexPattern.matcher(line);
        if (vertexMatcher.find()) {
            this.vertices.add(this.parsePoint(vertexMatcher));
        }
        Matcher faceMatcher = facePattern.matcher(line);
        if (faceMatcher.find()) {
            this.currentlyParsed.addAll(this.parseFace(faceMatcher));
        }
        Matcher groupMatcher = groupPattern.matcher(line);
        if (groupMatcher.find()) {
            List<Hittable> newList = new ArrayList<>();
            this.parsed.add(newList);
            this.currentlyParsed = newList;
        }
        Matcher normalMatcher = normalPattern.matcher(line);
        if (normalMatcher.find()) {
            this.normals.add(this.parseNormal(normalMatcher));
        }
    }

    private IPoint parsePoint(Matcher vertexMatcher) {
        return new Point(
            Double.parseDouble(vertexMatcher.group(1)),
            Double.parseDouble(vertexMatcher.group(2)),
            Double.parseDouble(vertexMatcher.group(3))
        );
    }

    private IVector parseNormal(Matcher normalMatcher) {
        return new Vector(
            Double.parseDouble(normalMatcher.group(1)),
            Double.parseDouble(normalMatcher.group(2)),
            Double.parseDouble(normalMatcher.group(3))
        );
    }

    private List<Triangle> parseFace(Matcher faceMatcher) {
        List<Triangle> parsedTriangles = new ArrayList<>();
        String[] vertexStrings = Arrays.stream(faceMatcher.group().substring(2).split(" ")).toArray(String[]::new);
        String[][] vertexInfos = Arrays.stream(vertexStrings).map(vertexString -> vertexString.split("/")).toArray(String[][]::new);
        for (int i = 1; i < vertexStrings.length - 1; i++) {
            if (vertexInfos[0].length == 3 && vertexInfos[i].length == 3 && vertexInfos[i+1].length == 3) {
                parsedTriangles.add(new SmoothTriangle(
                    this.vertices.get(Integer.parseInt(vertexInfos[0][0])),
                    this.vertices.get(Integer.parseInt(vertexInfos[i][0])),
                    this.vertices.get(Integer.parseInt(vertexInfos[i + 1][0])),
                    this.normals.get(Integer.parseInt(vertexInfos[0][2])),
                    this.normals.get(Integer.parseInt(vertexInfos[i][2])),
                    this.normals.get(Integer.parseInt(vertexInfos[i + 1][2])),
                    this.material
                ));
            }
            else {
                parsedTriangles.add(new Triangle(
                    this.vertices.get(Integer.parseInt(vertexInfos[0][0])),
                    this.vertices.get(Integer.parseInt(vertexInfos[i][0])),
                    this.vertices.get(Integer.parseInt(vertexInfos[i + 1][0])),
                    this.material
                ));
            }
        }
        return parsedTriangles;
    }
}

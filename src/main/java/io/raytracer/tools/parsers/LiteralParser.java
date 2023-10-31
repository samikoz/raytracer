package io.raytracer.tools.parsers;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.materials.Material;
import io.raytracer.shapes.Hittable;
import io.raytracer.shapes.Sphere;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LiteralParser implements Parser {
    private final List<Hittable> loaded;
    private final Material material;

    private final String floatPoint = "(-?\\d+\\.\\d+)";
    private final String threePoint = "P\\(" + floatPoint + "," + floatPoint + "," + floatPoint + "\\)";

    public LiteralParser(Material material) {
        this.loaded = new ArrayList<>();
        this.material = material;
    }

    @Override
    public List<Hittable> getParsed() {
        return this.loaded;
    }

    public void parseLine(String line) {
        Pattern spherePattern = Pattern.compile("Sphere\\(" + this.threePoint + "," + this.floatPoint + "\\)");
        Matcher sphereMatcher = spherePattern.matcher(line);
        if (sphereMatcher.find()) {
            this.loaded.add(this.parseSphere(sphereMatcher));
        } else {
            throw new RuntimeException(String.format("Could not parse line: %s", line));
        }
    }

    private Sphere parseSphere(Matcher sphereMatcher) {
        Sphere sphere = new Sphere(this.material);
        double parsedRadius = Double.parseDouble(sphereMatcher.group(4));
        IPoint parsedCentre = new Point(
            Double.parseDouble(sphereMatcher.group(1)),
            Double.parseDouble(sphereMatcher.group(2)),
            Double.parseDouble(sphereMatcher.group(3))
        );
        sphere.setTransform(
            ThreeTransform.scaling(parsedRadius, parsedRadius, parsedRadius)
            .translate(parsedCentre.x(), parsedCentre.y(), parsedCentre.z())
        );
        return sphere;
    }
}

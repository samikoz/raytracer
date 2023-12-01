package io.raytracer.tools.parsers;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.algebra.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.shapes.Hittable;
import io.raytracer.shapes.Sphere;
import io.raytracer.tools.Pixel;
import lombok.Getter;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LiteralParser implements Parser {
    @Getter private final List<Hittable> parsed;
    @Getter private final List<IPoint> parsedPoints;
    @Getter private final List<IVector> parsedVectors;
    @Getter private final List<Pixel> parsedPixels;
    private final Material material;

    private final String floatPoint = "(-?\\d+\\.\\d+)";
    private final String threeFloatPoint = "P\\(" + floatPoint + "," + floatPoint + "," + floatPoint + "\\)";
    private final Pattern threeIntPoint = Pattern.compile("Point\\((\\d+),(\\d+),(\\d+)\\)");
    private final Pattern pixel = Pattern.compile("Pixel\\(x=(\\d+), y=(\\d+)\\)");
    private final Pattern spherePattern = Pattern.compile("Sphere\\(" + this.threeFloatPoint + "," + this.floatPoint + "\\)");
    private final Pattern curvepoint = Pattern.compile("CurvePoint\\(P\\(" + floatPoint + "," + floatPoint + "\\),V\\(" + floatPoint + "," + floatPoint + "\\)\\)");

    public LiteralParser() {
        this.parsed = new ArrayList<>();
        this.parsedPoints = new ArrayList<>();
        this.parsedPixels = new ArrayList<>();
        this.parsedVectors = new ArrayList<>();
        this.material = Material.builder().build();
    }

    public LiteralParser(Material material) {
        this.parsed = new ArrayList<>();
        this.parsedPoints = new ArrayList<>();
        this.parsedPixels = new ArrayList<>();
        this.parsedVectors = new ArrayList<>();
        this.material = material;
    }

    public void parseLine(String line) {
        Matcher sphereMatcher = this.spherePattern.matcher(line);
        if (sphereMatcher.find()) {
            this.parsed.add(this.parseSphere(sphereMatcher));
            return;
        }
        Matcher pointMatcher = this.threeIntPoint.matcher(line);
        if (pointMatcher.find()) {
            this.parsedPoints.add(this.parsePoint(pointMatcher));
            return;
        }
        Matcher pixelMatcher = this.pixel.matcher(line);
        if (pixelMatcher.find()) {
            this.parsedPixels.add(this.parsePixel(pixelMatcher));
            return;
        }
        Matcher curvepointMatcher = this.curvepoint.matcher(line);
        if (curvepointMatcher.find()) {
            Pair<IPoint, IVector> parsed = this.parseCurvepoint(curvepointMatcher);
            this.parsedPoints.add(parsed.getValue0());
            this.parsedVectors.add(parsed.getValue1());
            return;
        }

        throw new RuntimeException(String.format("Could not parse line: %s", line));

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

    private IPoint parsePoint(Matcher pointMatcher) {
        return new Point(
            Integer.parseInt(pointMatcher.group(1)),
            Integer.parseInt(pointMatcher.group(2)),
            Integer.parseInt(pointMatcher.group(3))
        );
    }

    private Pixel parsePixel(Matcher pixelMatcher) {
        return new Pixel(
                Integer.parseInt(pixelMatcher.group(1)),
                Integer.parseInt(pixelMatcher.group(2))
        );
    }

    private Pair<IPoint, IVector> parseCurvepoint(Matcher matcher) {
        IPoint p = new Point(
            Double.parseDouble(matcher.group(1)),
            Double.parseDouble(matcher.group(2)),
            0
        );
        IVector v = new Vector(
            Double.parseDouble(matcher.group(3)),
            Double.parseDouble(matcher.group(4)),
            0
        );
        return new Pair<>(p, v);
    }
}

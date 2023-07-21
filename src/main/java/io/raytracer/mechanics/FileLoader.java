package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.materials.Material;
import io.raytracer.shapes.Shape;
import io.raytracer.shapes.Sphere;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileLoader implements Loader {
    private final ArrayList<Shape> loaded;
    private final Material material;

    private final String floatPoint = "(-?\\d+\\.\\d+)";
    private final String threePoint = "P\\(" + floatPoint + "," + floatPoint + "," + floatPoint + "\\)";

    public FileLoader(Material material) {
        this.loaded = new ArrayList<>();
        this.material = material;
    }

    @Override
    public void populate(World world) {
        this.loaded.forEach(world::put);
    }

    @Override
    public void load(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            this.parseLine(line);
        }
        scanner.close();
    }

    private void parseLine(String line) {
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
            .translate(parsedCentre.get(0), parsedCentre.get(1), parsedCentre.get(2))
        );
        return sphere;
    }
}

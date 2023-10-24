package io.raytracer.tools.parsers;

import io.raytracer.shapes.Hittable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public interface Parser {
    List<Hittable> getParsed();
    void parseLine(String line);

    default void parse(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            this.parseLine(line);
        }
        scanner.close();
    }
}

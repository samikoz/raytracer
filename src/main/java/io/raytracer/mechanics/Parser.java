package io.raytracer.mechanics;

import io.raytracer.shapes.Group;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public interface Parser {
    Group getParsed();
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

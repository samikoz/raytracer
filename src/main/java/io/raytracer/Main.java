package io.raytracer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) throws IOException {
        String filename = "sphere.ppt";
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
    }
}

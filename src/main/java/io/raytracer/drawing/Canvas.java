package io.raytracer.drawing;

import java.io.FileWriter;
import java.io.IOException;

public interface Canvas {
    void write(int x, int y, Colour colour);
    Colour read(int x, int y);

    String export();
    void export(FileWriter writer) throws IOException;
}

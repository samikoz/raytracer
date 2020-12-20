package io.raytracer.drawing;

import java.io.IOException;
import java.io.PrintWriter;

public interface Canvas {
    void write(int x, int y, Colour colour);
    Colour read(int x, int y);

    String export();
    void export(PrintWriter writer) throws IOException;
}

package io.raytracer.drawing;

import java.io.IOException;
import java.io.PrintWriter;

public interface IPicture {
    void write(int x, int y, IColour colour);
    IColour read(int x, int y);
    String export();
    void export(PrintWriter writer) throws IOException;
}

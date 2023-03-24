package io.raytracer.tools;

import java.io.IOException;
import java.io.PrintWriter;

public interface IPicture {
    int getWidth();
    int getHeight();
    void write(int x, int y, IColour colour);
    IColour read(int x, int y);
    String export();
    default void embed(IPicture picture, int xPosition, int yPosition) {
        for (int y = 0; y < picture.getHeight(); y++) {
            for (int x = 0; x < picture.getWidth(); x++) {
                this.write(xPosition + x, yPosition + y, picture.read(x, y));
            }
        }
    }
    void export(PrintWriter writer) throws IOException;
}

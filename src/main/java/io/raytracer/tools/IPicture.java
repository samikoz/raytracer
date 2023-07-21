package io.raytracer.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

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
    default void average(Collection<IPicture> pictures) {
        for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < this.getWidth(); x++) {
                int xCopy = x;
                int yCopy = y;
                IColour averaged = pictures.stream()
                    .map(pic -> pic.read(xCopy, yCopy))
                    .reduce(IColour::add)
                    .map(c -> c.multiply(1.0/ pictures.size()))
                    .orElseThrow(RuntimeException::new);
                this.write(xCopy, yCopy, averaged);
            }
        }
    }
    void export(PrintWriter writer) throws IOException;
}

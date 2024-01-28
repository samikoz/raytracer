package io.raytracer.tools;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Stream;

public interface IPicture {
    int getWidth();
    int getHeight();
    Stream<Pixel> getBlankPixels();
    void write(Pixel p, IColour colour);

    IColour read(int x, int y);
    void export(Path path) throws IOException;

    default void add(IPicture pic) {
        for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < this.getWidth(); x++) {
                IColour sum = this.read(x, y).add(pic.read(x, y));
                this.write(new Pixel(x, y), sum);
            }
        }
    }

    default void patch(IPicture pic) {
        for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < this.getWidth(); x++) {
                IColour colour = pic.read(x, y);
                if (!colour.equals(new LinearColour(0, 0, 0))) {
                    this.write(new Pixel(x, y), colour);
                }
            }
        }
    }
}

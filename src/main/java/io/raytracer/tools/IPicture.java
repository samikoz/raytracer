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
    default void fill(IColour colour) {
        for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < this.getWidth(); x++) {
                this.write(new Pixel(x, y), colour);
            }
        }
    }

    IColour read(int x, int y);
    void export(Path path) throws IOException;

    default void add(Collection<IPicture> pictures) {
        for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < this.getWidth(); x++) {
                IColour sum = new LinearColour(0);
                for (IPicture pic : pictures) {
                    sum = sum.add(pic.read(x, y));
                }
                this.write(new Pixel(x, y), sum);
            }
        }
    }
}

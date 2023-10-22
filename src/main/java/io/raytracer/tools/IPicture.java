package io.raytracer.tools;

import io.raytracer.geometry.IPoint;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.BiPredicate;

public interface IPicture {
    int getWidth();
    int getHeight();
    void write(int x, int y, IColour colour);
    default void fill(IColour colour) {
        for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < this.getWidth(); x++) {
                this.write(x, y, colour);
            }
        }
    }

    IColour read(int x, int y);
    void export(Path path) throws IOException;
    default void embed(IPicture picture, IPoint embedPosition) {
        for (int y = 0; y < picture.getHeight(); y++) {
            for (int x = 0; x < picture.getWidth(); x++) {
                this.write((int)embedPosition.get(0) + x, (int)embedPosition.get(1) + y, picture.read(x, y));
            }
        }
    }

    default void embed(IPicture picture, BiPredicate<Integer, Integer> pixelCondition) {
        for (int y = 0; y < picture.getHeight(); y++) {
            for (int x = 0; x < picture.getWidth(); x++) {
                if (pixelCondition.test(x, y)) {
                    this.write(x, y, picture.read(x, y));
                }
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
}

package io.raytracer.drawing;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class PPMCanvas implements Canvas {
    private Colour[][] pixelGrid;
    static private final Colour initialColour = new Unit3TupleColour(0 ,0,0);
    private final String exportHeader;

    private class PPMCanvasIterator implements Iterator<Colour> {
        private int horizontalIndex;
        private int verticalIndex;

        PPMCanvasIterator() {
            verticalIndex = 0;
            horizontalIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return verticalIndex < pixelGrid.length - 1 || horizontalIndex < pixelGrid[verticalIndex].length;
        }

        @Override
        public Colour next() throws NoSuchElementException {
            if (hasNext()) {
                if (horizontalIndex < pixelGrid[verticalIndex].length) {
                    return pixelGrid[verticalIndex][horizontalIndex++];
                } else {
                    horizontalIndex = 0;
                    return pixelGrid[++verticalIndex][horizontalIndex];
                }
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    @Override
    public Iterator<Colour> iterator() {
        return new PPMCanvasIterator();
    }
    public PPMCanvas(int x, int y) {
        exportHeader = "P3\n" + x + " " + y + "\n255";
        pixelGrid = new Colour[y][x];
        for (int i = 0; i < y; i++) {
            Arrays.fill(pixelGrid[i], initialColour);
        }
    }

    @Override
    public void write(int x, int y, Colour colour) {
        pixelGrid[y][x] = colour;
    }

    @Override
    public Colour read(int x, int y) {
        return pixelGrid[y][x];
    }

    @Override
    public String exportToPPM() {
        return exportHeader;
    }
}

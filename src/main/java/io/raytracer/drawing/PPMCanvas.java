package io.raytracer.drawing;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PPMCanvas implements Canvas {
    private List<List<Colour>> pixelGrid;

    private static Colour initialColour = new Unit3TupleColour(0 ,0,0);

    public PPMCanvas(int x, int y) {
        pixelGrid = Collections.nCopies(y, Collections.nCopies(x, initialColour));
    }

    @Override
    public void write(int x, int y, Colour colour) {

    }

    @Override
    public Iterator<Colour> iterator() {
        return null;
    }

    @Override
    public Colour read(int x, int y) {
        return null;
    }
}

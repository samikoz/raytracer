package io.raytracer.drawing;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PPMCanvas implements Canvas {
    private List<List<Colour>> pixelGrid;
    static private final Colour initialColour = new Unit3TupleColour(0 ,0,0);

    private class PPMCanvasIterator implements Iterator<Colour> {
        private Iterator<Colour> horizontalIterator;
        private Iterator<List<Colour>> verticalIterator;

        PPMCanvasIterator() {
            verticalIterator = pixelGrid.iterator();
            horizontalIterator = verticalIterator.next().iterator();
        }

        @Override
        public boolean hasNext() {
            return horizontalIterator.hasNext() || verticalIterator.hasNext();
        }

        @Override
        public Colour next() throws NoSuchElementException {
            if (horizontalIterator.hasNext()) {
                return horizontalIterator.next();
            } else if (verticalIterator.hasNext()) {
                horizontalIterator = verticalIterator.next().iterator();
                return horizontalIterator.next();
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
        pixelGrid = Collections.nCopies(y, Collections.nCopies(x, initialColour));
    }

    @Override
    public void write(int x, int y, Colour colour) {

    }

    @Override
    public Colour read(int x, int y) {
        return null;
    }
}

package io.raytracer.drawing;

import java.util.*;

public class PPMCanvas implements Canvas {
    private List<List<Colour>> pixelGrid;
    static private final Colour initialColour = new Unit3TupleColour(0 ,0,0);
    private final String exportHeader;

    @Override
    public Iterator<Colour> iterator() {
        return new PPMCanvasIterator();
    }

    private Iterator<Iterator<Colour>> rowIterator() {
        return new PPMCanvasRowIterator();
    }

    public PPMCanvas(int x, int y) {
        exportHeader = "P3\n" + x + " " + y + "\n255";

        Colour[] rowPrototype = new Colour[x];
        Arrays.fill(rowPrototype, initialColour);

        pixelGrid = new ArrayList<>(y);
        for (int i = 0; i < y; i++) {
            pixelGrid.add(i, Arrays.asList(Arrays.copyOf(rowPrototype, x)));
        }
    }

    @Override
    public void write(int x, int y, Colour colour) {
        pixelGrid.get(y).set(x, colour);
    }

    @Override
    public Colour read(int x, int y) {
        return pixelGrid.get(y).get(x);
    }

    @Override
    public String exportToPPM() {
        return exportHeader;
    }

    private class PPMCanvasIterator implements Iterator<Colour> {
        private Iterator<Iterator<Colour>> aRowIterator;
        private Iterator<Colour> aRow;

        PPMCanvasIterator() {
            aRowIterator = rowIterator();
            aRow = rowIterator().next();
        }

        @Override
        public boolean hasNext() {
            return aRow.hasNext() || aRowIterator.hasNext();
        }

        @Override
        public Colour next() {
            if (aRow.hasNext()) {
                return aRow.next();
            } else if (aRowIterator.hasNext()) {
                aRow = aRowIterator.next();
                return aRow.next();
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    private class PPMCanvasRowIterator implements Iterator<Iterator<Colour>> {
        private int verticalIndex;

        PPMCanvasRowIterator() {
            verticalIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return verticalIndex < pixelGrid.size();
        }

        @Override
        public Iterator<Colour> next() {
            if (hasNext()) {
                return pixelGrid.get(verticalIndex++).iterator();
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
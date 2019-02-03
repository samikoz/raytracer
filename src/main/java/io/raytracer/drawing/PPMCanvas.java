package io.raytracer.drawing;

import java.util.*;

public class PPMCanvas implements Canvas {
    private PPMCanvasRow[] pixelGrid;
    static private final Colour initialColour = new Unit3TupleColour(0 ,0,0);
    private final String exportHeader;

    public PPMCanvas(int x, int y) {
        exportHeader = "P3\n" + x + " " + y + "\n255\n";

        pixelGrid = new PPMCanvasRow[y];
        for (int i = 0; i < y; i++) {
            pixelGrid[i] = new PPMCanvasRow(x);
        }
    }

    @Override
    public void write(int x, int y, Colour colour) {
        pixelGrid[y].set(x, colour);
    }

    @Override
    public Colour read(int x, int y) {
        return pixelGrid[y].get(x);
    }

    @Override
    public String exportToPPM() {
        StringBuilder exported = new StringBuilder(exportHeader);

        for (PPMCanvasRow row : rows()) {
            exported.append(String.join(" ", row.export()));
            exported.append("\n");
        }
        return exported.toString();
    }

    private Iterable<PPMCanvasRow> rows() {
        return () -> new Iterator<PPMCanvasRow>() {
            private int rowIndex;

            { rowIndex = 0; }

            @Override
            public boolean hasNext() {
                return rowIndex < pixelGrid.length;
            }

            @Override
            public PPMCanvasRow next() {
                if (hasNext()) {
                    return pixelGrid[rowIndex++];
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    private class PPMCanvasRow implements Iterable<Colour> {
        private Colour[] rowColours;
        private int size;

        PPMCanvasRow(int size) {
            this.size = size;

            rowColours = new Unit3TupleColour[size];
            Arrays.fill(rowColours, initialColour);
        }

        Colour get(int x) {
            return rowColours[x];
        }

        void set(int x, Colour colour) {
            rowColours[x] = colour;
        }

        List<String> export() {
            List<String> exported = new ArrayList<>(size);
            for (Colour rowElement : this) {
                exported.add(rowElement.exportNormalised());
            }
            return exported;
        }

        @Override
        public Iterator<Colour> iterator() {
            return new Iterator<Colour>() {
                private int rowElementIndex;

                { rowElementIndex = 0; }

                @Override
                public boolean hasNext() {
                    return rowElementIndex < rowColours.length;
                }

                @Override
                public Colour next() {
                    if (hasNext()) {
                        return rowColours[rowElementIndex++];
                    } else {
                        throw new NoSuchElementException();
                    }
                }
            };
        }
    }
}
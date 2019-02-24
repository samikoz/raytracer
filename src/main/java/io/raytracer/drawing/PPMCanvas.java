package io.raytracer.drawing;

import java.util.*;
import java.util.stream.Stream;

public class PPMCanvas implements Canvas {
    private PPMCanvasRow[] pixelGrid;
    static private final Colour initialColour = new Unit3TupleColour(0 ,0,0);
    static private final int exportedLineMaxLength = 70;
    private final String exportHeader;

    public PPMCanvas(int x, int y) {
        exportHeader = "P3\n" + x + " " + y + "\n255\n";

        pixelGrid = new PPMCanvasRow[y];
        Arrays.setAll(pixelGrid, i -> new PPMCanvasRow(x));
    }

    @Override
    public void write(int x, int y, Colour colour) {
        pixelGrid[y].set(x, colour);
    }

    @Override
    public Colour read(int x, int y) {
        return pixelGrid[y].get(x);
    }

    private String putLineBreaks(String exportRow) {
        StringBuilder rowToBreak = new StringBuilder(exportRow);
        int numberOfBreaks = exportRow.length() / exportedLineMaxLength;
        for (int breakNumber = 1; breakNumber <= numberOfBreaks; breakNumber++) {
            int whitespacePosition = breakNumber*exportedLineMaxLength;
            while (rowToBreak.charAt(whitespacePosition) != ' ') whitespacePosition--;
            rowToBreak.setCharAt(whitespacePosition, '\n');
        }
        return rowToBreak.toString();
    }

    @Override
    public String exportToPPM() {
        StringBuilder exported = new StringBuilder(exportHeader);

        for (PPMCanvasRow row : rows()) {
            exported.append(putLineBreaks(String.join(" ", row.export()))).append("\n");
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
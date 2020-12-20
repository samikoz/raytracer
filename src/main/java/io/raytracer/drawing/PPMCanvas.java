package io.raytracer.drawing;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PPMCanvas implements Canvas {
    private final PPMCanvasRow[] pixelGrid;
    static private final Colour initialColour = new ColourImpl(0, 0, 0);
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

    @Override
    public void export(FileWriter writer) throws IOException {
        writer.write(this.export());
        writer.close();
    }

    @Override
    public String export() {
        StringBuilder exported = new StringBuilder(exportHeader);

        for (PPMCanvasRow row : rows()) {
            exported.append(putLineBreaks(String.join(" ", row.export()))).append("\n");
        }
        return exported.toString();
    }

    private Iterable<PPMCanvasRow> rows() {
        return () -> new Iterator<PPMCanvasRow>() {
            private int rowIndex;

            {
                rowIndex = 0;
            }

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

    private String putLineBreaks(String exportRow) {
        StringBuilder rowToBreak = new StringBuilder(exportRow);
        int numberOfBreaks = exportRow.length() / exportedLineMaxLength;
        for (int breakNumber = 1; breakNumber <= numberOfBreaks; breakNumber++) {
            int whitespacePosition = breakNumber * exportedLineMaxLength;
            while (rowToBreak.charAt(whitespacePosition) != ' ') whitespacePosition--;
            rowToBreak.setCharAt(whitespacePosition, '\n');
        }
        return rowToBreak.toString();
    }

    private static class PPMCanvasRow implements Iterable<Colour> {
        private final Colour[] rowColours;
        private final int size;

        PPMCanvasRow(int size) {
            this.size = size;

            rowColours = new ColourImpl[size];
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

                {
                    rowElementIndex = 0;
                }

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
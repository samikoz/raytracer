package io.raytracer.tools;

import lombok.Getter;
import lombok.NonNull;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PPMPicture implements IPicture {
    @Getter private final int width;
    @Getter private final int height;
    private final List<ArrayList<IColour>> pixelGrid;
    private final String exportHeader;

    static private final IColour initialColour = new LinearColour(0, 0, 0);
    static private final int exportedLineMaxLength = 70;

    public PPMPicture(int x, int y) {
        this.width = x;
        this.height = y;
        exportHeader = "P3\n" + x + " " + y + "\n255\n";

        pixelGrid = Stream.generate(() -> new ArrayList<>(Collections.nCopies(x, PPMPicture.initialColour))).limit(y).collect(Collectors.toList());
    }

    @Override
    public void write(int x, int y, @NonNull IColour colour) {
        pixelGrid.get(y).set(x, colour);
    }

    @Override
    public IColour read(int x, int y) {
        return pixelGrid.get(y).get(x);
    }

    @Override
    public void export(PrintWriter writer) {
        writer.write(this.export());
        writer.close();
    }

    @Override
    public String export() {
        StringBuilder exported = new StringBuilder(exportHeader);

        for (ArrayList<IColour> row : this.pixelGrid) {
            exported.append(putLineBreaks(String.join(" ", this.export(row)))).append("\n");
        }
        return exported.toString();
    }

    private List<String> export(ArrayList<IColour> ppmRow) {
        List<String> exported = new ArrayList<>();
        ppmRow.forEach(rowElement -> exported.add(rowElement.export()));
        return exported;
    }

    private String putLineBreaks(String exportRow) {
        StringBuilder rowToBreak = new StringBuilder(exportRow);
        int numberOfBreaks = (exportRow.length() - 1) / exportedLineMaxLength;
        for (int breakNumber = 1; breakNumber <= numberOfBreaks; breakNumber++) {
            int whitespacePosition = breakNumber * exportedLineMaxLength;
            while (rowToBreak.charAt(whitespacePosition) != ' ') whitespacePosition--;
            rowToBreak.setCharAt(whitespacePosition, '\n');
        }
        return rowToBreak.toString();
    }
}
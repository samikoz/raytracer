package io.raytracer.tools;

import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PPMPicture implements IPicture {
    @Getter private final int width;
    @Getter private final int height;
    private final IColour[][] pixelGrid;
    private final String exportHeader;
    private final int averagingFactor;
    private int loadingPointer;
    private final List<Integer> parsingResidue;

    static private final IColour initialColour = new LinearColour(0, 0, 0);
    static private final int exportedLineMaxLength = 70;

    public PPMPicture(int x, int y) {
        this(x, y, 1);
    }

    public PPMPicture(int x, int y, int averagingFactor) {
        this.width = x;
        this.height = y;
        exportHeader = "P3\n" + x + " " + y + "\n255\n";
        this.averagingFactor = averagingFactor;

        pixelGrid = new IColour[y][x];

        this.loadingPointer = 0;
        this.parsingResidue = new ArrayList<>(3);
    }

    @Override
    public void write(Pixel p, @NonNull IColour colour) {
        this.pixelGrid[p.y][p.x] = colour;
    }

    @Override
    public IColour read(int x, int y) {
        try {
            IColour read = this.pixelGrid[y][x];
            if (read == null) {
                throw new IndexOutOfBoundsException(String.format("%d,%d", x, y));
            }
            return read;
        } catch (IndexOutOfBoundsException e) {
            return PPMPicture.initialColour;
        }
    }

    @Override
    public Stream<Pixel> getBlankPixels() {
        return IntStream.range(0, this.height).mapToObj(y -> IntStream.range(0, this.width).mapToObj(x -> new Pixel(x, y))).flatMap(y -> y);
    }

    @Override
    public void export(Path path) throws IOException {
        System.out.printf("exporting to %s\n", path.toString());
        FileOutputStream writeStream = FileUtils.openOutputStream(path.toFile());
        writeStream.write(this.export().getBytes());
        writeStream.close();
    }

    private String export() {
        StringBuilder exported = new StringBuilder(exportHeader);

        for (int y = 0; y < this.height; y++) {
            exported.append(putLineBreaks(this.exportRow(y))).append("\n");
        }
        return exported.toString();
    }

    protected String exportRow(int y) {
        List<String> exported = new ArrayList<>();
        for (int x = 0; x < this.width; x++) {
            exported.add(this.read(x, y).multiply(1.0/this.averagingFactor).export());
        }
        return String.join(" ", exported);
    }

    private String putLineBreaks(String exportedRow) {
        StringBuilder rowToBreak = new StringBuilder(exportedRow);
        int numberOfBreaks = (exportedRow.length() - 1) / exportedLineMaxLength;
        for (int breakNumber = 1; breakNumber <= numberOfBreaks; breakNumber++) {
            int whitespacePosition = breakNumber * exportedLineMaxLength;
            while (rowToBreak.charAt(whitespacePosition) != ' ') whitespacePosition--;
            rowToBreak.setCharAt(whitespacePosition, '\n');
        }
        return rowToBreak.toString();
    }

    public static PPMPicture load(Path file) throws IOException {
        System.out.printf("loading from %s\n", file.toString());
        BufferedReader reader = Files.newBufferedReader(file);
        PPMPicture loadedPicture = PPMPicture.parseHeader(reader);
        reader.lines().forEach(loadedPicture::parseLoadedLine);
        reader.close();
        return loadedPicture;
    }

    private static PPMPicture parseHeader(BufferedReader reader) throws IOException {
        String firstLine = reader.readLine();
        assert firstLine.startsWith("P3");
        String secondLine = reader.readLine();
        String thirdLine = reader.readLine();
        assert thirdLine.startsWith("255");
        Matcher sizeMatcher = Pattern.compile("(\\d+) (\\d+)").matcher(secondLine);
        boolean isFound = sizeMatcher.find();
        assert isFound;
        int x = Integer.parseInt(sizeMatcher.group(1));
        int y = Integer.parseInt(sizeMatcher.group(2));
        return new PPMPicture(x, y);
    }

    private void parseLoadedLine(String line) {
        Arrays.stream(line.split(" ")).map(Integer::parseInt).forEach(colorPart -> {
            this.parsingResidue.add(colorPart);
            if (this.parsingResidue.size() == 3) {
                IColour parsedColour = new LinearColour(this.parsingResidue.get(0)/255.0, this.parsingResidue.get(1)/255.0, this.parsingResidue.get(2)/255.0);
                this.write(new Pixel(this.loadingPointer % this.getWidth(), this.loadingPointer / this.getWidth()), parsedColour);
                this.loadingPointer++;
                this.parsingResidue.clear();
            }
        });
    }
}
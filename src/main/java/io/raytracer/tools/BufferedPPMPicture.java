package io.raytracer.tools;

import lombok.Getter;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class BufferedPPMPicture implements IPicture {
    @Getter private final int width;
    @Getter private final int height;
    private final Path buffDir;
    private final int bufferSize;
    private List<Triplet<Integer, Integer, IColour>> buffer;
    private int persistedBufferIndex;
    private PPMPicture loadedBuffer;
    private static final String buffFileExtension = ".buff";

    public BufferedPPMPicture(int x, int y, Path bufferDirectory, int bufferSize) throws IOException {
        this.width = x;
        this.height = y;
        this.buffDir = bufferDirectory;
        this.buffer = new ArrayList<>();
        this.bufferSize = bufferSize;
        this.persistedBufferIndex = this.scanForBufferIndex();
        this.loadedBuffer = new PPMPicture(this.width, this.height);
    }

    public BufferedPPMPicture(int x, int y, Path bufferDirectory) throws IOException {
        this(x, y, bufferDirectory, x * y / 100);
    }

    public synchronized void write(int x, int y, IColour colour) {
        this.buffer.add(new Triplet<>(x, y, colour));
        if (this.buffer.size() >= this.bufferSize) {
            this.persistBuffer();
        }
    }

    @Override
    public IColour read(int x, int y) {
        return this.buffer.stream()
                .filter(triplet -> triplet.getValue0() == x && triplet.getValue1() == y)
                .findFirst()
                .map(Triplet::getValue2)
                .orElse(this.loadedBuffer.read(x, y));
    }

    @Override
    public Stream<Pair<Integer, Integer>> getBlankPixels() {
        Stream<Pair<Integer, Integer>> allPixels = IntStream.range(0, this.height).mapToObj(y -> IntStream.range(0, this.width)
                .mapToObj(x -> new Pair<>(x, y))).flatMap(y -> y);
        Set<Pair<Integer, Integer>> writtenPixels = this.parsePersisted()
                .map(triplet -> new Pair<>(triplet.getValue0(), triplet.getValue1())).collect(Collectors.toSet());
        return allPixels.filter(pixelPair -> !writtenPixels.contains(pixelPair));
    }

    @Override
    public void export(Path path) throws IOException {
        this.persistBuffer();
        this.loadPersisted();
        this.loadedBuffer.export(path);
        this.deletePersisted();
    }

    private Path getBuffer(int index) {
        return this.buffDir.resolve(String.format("%03d%s", index, buffFileExtension));
    }

    private void persistBuffer() {
        persistedBufferIndex++;
        Path buffer = this.getBuffer(this.persistedBufferIndex);
        try {
            ObjectOutputStream outStream = new ObjectOutputStream(Files.newOutputStream(buffer));
            outStream.writeObject(this.buffer);
            outStream.close();
        } catch (IOException e) {
            System.err.format("exception when persisting buffer: %s%n", e);
            System.exit(2);
        }
        this.buffer = new ArrayList<>();
    }

    private Stream<Triplet<Integer, Integer, IColour>> parsePersisted() {
        Stream<Triplet<Integer, Integer, IColour>> parsed = Stream.empty();
        for (int bufferIndex = 1; bufferIndex < this.persistedBufferIndex+1; bufferIndex++) {
            try {
                ObjectInputStream inStream = new ObjectInputStream(Files.newInputStream(this.getBuffer(bufferIndex)));
                @SuppressWarnings("unchecked")
                List<Triplet<Integer, Integer, IColour>> readBuffer = (List<Triplet<Integer, Integer, IColour>>) inStream.readObject();
                inStream.close();
                parsed = Stream.concat(parsed, readBuffer.stream());
            } catch (IOException | ClassNotFoundException e) {
                System.err.format("exception when loading buffer: %s%n", e);
                System.exit(2);
            }
        }
        return parsed;
    }

    public void loadPersisted() {
        PPMPicture loaded = new PPMPicture(this.width, this.height);
        this.parsePersisted().forEach(loadedColour -> loaded.write(loadedColour.getValue0(), loadedColour.getValue1(), loadedColour.getValue2()));
        this.loadedBuffer = loaded;
    }

    private void deletePersisted() throws IOException {
        for (int bufferIndex = 1; bufferIndex < this.persistedBufferIndex+1; bufferIndex++) {
            Files.delete(this.getBuffer(bufferIndex));
        }
    }

    private int scanForBufferIndex() throws IOException {
        try (Stream<Path> stream = Files.list(this.buffDir)) {
            return stream.map(path -> path.getFileName().toString()).map(pathname -> pathname.replace(buffFileExtension, "")).mapToInt(Integer::parseInt).max().orElse(0);
        }
    }
}

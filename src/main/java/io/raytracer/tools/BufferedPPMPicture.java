package io.raytracer.tools;

import lombok.Getter;
import org.javatuples.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class BufferedPPMPicture implements IPicture {
    @Getter private final int width;
    @Getter private final int height;
    private final Path buffDir;
    private final int bufferSize;
    private List<Pair<Pixel, IColour>> buffer;
    private int persistedBufferIndex;
    private IPicture loadedBuffer;
    private final BiFunction<Integer, Integer, IPicture> bufferPictureConstructor;
    private static final String buffFileExtension = ".buff";

    public BufferedPPMPicture(int x, int y, Path bufferDirectory, int bufferSize, BiFunction<Integer, Integer, IPicture> unbufferedConstructor) throws IOException {
        this.width = x;
        this.height = y;
        this.buffDir = bufferDirectory;
        Files.createDirectories(this.buffDir);
        this.buffer = new ArrayList<>();
        this.bufferSize = bufferSize;
        this.persistedBufferIndex = this.scanForBufferIndex();
        this.bufferPictureConstructor = unbufferedConstructor;
        this.loadedBuffer = unbufferedConstructor.apply(this.width, this.height);
    }

    public BufferedPPMPicture(int x, int y, Path bufferDirectory, BiFunction<Integer, Integer, IPicture> unbufferedConstructor) throws IOException {
        this(x, y, bufferDirectory, x * y / 100, unbufferedConstructor);
    }

    public synchronized void write(Pixel p, IColour colour) {
        this.buffer.add(new Pair<>(p, colour));
        if (this.buffer.size() >= this.bufferSize) {
            this.persistBuffer();
        }
    }

    @Override
    public IColour read(int x, int y) {
        return this.buffer.stream()
                .filter(pair -> pair.getValue0().x == x && pair.getValue0().y == y)
                .findFirst()
                .map(Pair::getValue1)
                .orElse(this.loadedBuffer.read(x, y));
    }

    @Override
    public Stream<Pixel> getBlankPixels() {
        Stream<Pixel> allPixels = IntStream.range(0, this.height).mapToObj(y -> IntStream.range(0, this.width)
                .mapToObj(x -> new Pixel(x, y))).flatMap(y -> y);
        Set<Pixel> writtenPixels = this.parsePersisted()
                .map(pair -> new Pixel(pair.getValue0().x, pair.getValue0().y)).collect(Collectors.toSet());
        return allPixels.filter(pixel -> !writtenPixels.contains(pixel));
    }

    @Override
    public void export(Path path) throws IOException {
        this.persistBuffer();
        this.loadPersisted();
        this.loadedBuffer.export(path);
        //this.deletePersisted();
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

    private Stream<Pair<Pixel, IColour>> parsePersisted() {
        Stream<Pair<Pixel, IColour>> parsed = Stream.empty();
        for (int bufferIndex = 1; bufferIndex < this.persistedBufferIndex+1; bufferIndex++) {
            try {
                ObjectInputStream inStream = new ObjectInputStream(Files.newInputStream(this.getBuffer(bufferIndex)));
                @SuppressWarnings("unchecked")
                List<Pair<Pixel, IColour>> readBuffer = (List<Pair<Pixel, IColour>>) inStream.readObject();
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
        IPicture loaded = this.bufferPictureConstructor.apply(this.width, this.height);
        this.parsePersisted().forEach(loadedColour -> loaded.write(loadedColour.getValue0(), loadedColour.getValue1()));
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

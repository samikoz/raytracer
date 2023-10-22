package io.raytracer.tools;

import lombok.Getter;
import org.javatuples.Triplet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class BufferedPPMPicture implements IPicture {
    @Getter private final int width;
    @Getter private final int height;
    private final Path buffDir;
    private final int bufferSize;
    private List<Triplet<Integer, Integer, IColour>> buffer;
    private int persistedBufferIndex;
    private PPMPicture loadedBuffer;

    public BufferedPPMPicture(int x, int y, Path bufferDirectory, int bufferSize) {
        this.width = x;
        this.height = y;
        this.buffDir = bufferDirectory;
        this.buffer = new ArrayList<>();
        this.bufferSize = bufferSize;
        this.persistedBufferIndex = 1;
        this.loadedBuffer = new PPMPicture(this.width, this.height);
    }

    public BufferedPPMPicture(int x, int y, Path bufferDirectory) {
        this(x, y, bufferDirectory, x * y / 100);
    }

    public synchronized void write(int x, int y, IColour colour) {
        this.buffer.add(new Triplet<>(x, y, colour));
        if (this.buffer.size() >= this.bufferSize) {
            this.emptyBuffer();
            persistedBufferIndex++;
        }
    }

    @Override
    public IColour read(int x, int y) {
        return this.loadedBuffer.read(x, y);
    }

    @Override
    public void export(Path path) throws IOException {
        this.emptyBuffer();
        this.loadAllBuffered();
        this.loadedBuffer.export(path);
    }

    private Path getBuffer(int index) {
        return this.buffDir.resolve(String.format("%03d.buff", index));
    }

    private void emptyBuffer() {
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

    public void loadAllBuffered() {
        PPMPicture loaded = new PPMPicture(this.width, this.height);
        for (int bufferIndex = 1; bufferIndex < this.persistedBufferIndex+1; bufferIndex++) {
            try {
                ObjectInputStream inStream = new ObjectInputStream(Files.newInputStream(this.getBuffer(bufferIndex)));
                @SuppressWarnings("unchecked")
                List<Triplet<Integer, Integer, IColour>> readBuffer = (List<Triplet<Integer, Integer, IColour>>) inStream.readObject();
                inStream.close();
                readBuffer.forEach(storedColour -> loaded.write(storedColour.getValue0(), storedColour.getValue1(), storedColour.getValue2()));
            }
            catch (IOException | ClassNotFoundException e) {
                System.err.format("exception when loading buffer: %s%n", e);
                System.exit(2);
            }
        }
        this.loadedBuffer = loaded;
    }
}

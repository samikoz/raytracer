package io.raytracer.mechanics.reporters;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class AggregatingReporter extends DefaultReporter {
    private final Path filepath;

    public AggregatingReporter(RenderData data, Path file) {
        super(data);
        this.filepath = file;
    }

    @Override
    public void report(int renderedPixelCount) {
        super.report(renderedPixelCount);
        if (renderedPixelCount % 1000 == 0) {
            String message = super.formatMessage(renderedPixelCount)
                    .replace(" /", ";")
                    .replaceAll("[^0-9.;%]", "") + "\n";

            try (OutputStream outputStream = Files.newOutputStream(this.filepath)) {
                outputStream.write(message.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

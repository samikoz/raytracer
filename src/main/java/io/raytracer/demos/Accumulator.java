package io.raytracer.demos;

import io.raytracer.algebra.ThreeTransform;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.shapes.Shape;
import io.raytracer.shapes.Torus;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.tools.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Accumulator {
    private final Painter painter;
    private final DemoSetup setup;
    private final Path bufferDir;
    private final IPicture sumPicture;
    private final String pictureTemplate;
    private int renderIndex;

    public Accumulator(Painter painter, Path bufferDir, String pictureTemplate) throws IOException{
        this.painter = painter;
        this.bufferDir = bufferDir;
        this.pictureTemplate = pictureTemplate;

        this.setup = painter.getSetup();
        this.renderIndex = Accumulator.getInitialBufferIndex(this.bufferDir);
        if (renderIndex == 0) {
            this.sumPicture = new PPMPicture(this.setup.xSize, this.setup.ySize);
        } else {
            this.sumPicture = new BufferedPPMPicture(this.setup.xSize, this.setup.ySize, this.getBufferFile(), PPMPicture::new).getUnbuffered();
        }
    }

    private static int getInitialBufferIndex(Path bufferDir) throws IOException {
        Files.createDirectories(bufferDir);
        try (Stream<Path> stream = Files.list(bufferDir)) {
            return stream.map(path -> path.getFileName().toString()).map(s -> s.substring(s.length() - 2)).mapToInt(Integer::parseInt).max().orElse(0);
        }
    }

    private Path getBufferFile() {
        return this.bufferDir.resolve(Paths.get(String.format("buff%02d", renderIndex)));
    }

    private Path getPicturePath() {
        return Paths.get(String.format(this.pictureTemplate, renderIndex));
    }

    public void accumulate() throws IOException {
        renderIndex++;
        IPicture rollingRender = this.painter.render();

        int finalI = renderIndex;
        IPicture accumulatedPicture = new BufferedPPMPicture(this.setup.xSize, this.setup.ySize, this.getBufferFile(), (x, y) -> new PPMPicture(x, y, finalI*setup.rayCount));
        sumPicture.add(rollingRender);
        accumulatedPicture.add(sumPicture);
        accumulatedPicture.export(this.getPicturePath());
    }

    public static void main(String[] args) throws IOException {
        int radius = 12;
        IPoint eyePosition = new Point(new Vector(-radius, 0, 0).transform(ThreeTransform.rotation_z(Math.PI / 10)));
        Material material = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.65)))
                .build();
        Shape leftTorus = new Torus(radius, 2, material);
        ThreeTransform torusRotation = ThreeTransform.rotation_y(-Math.PI/12).rotate_x(-Math.PI/5.5);
        leftTorus.setTransform(torusRotation.conjugateTranslating(eyePosition));
        DemoSetup leftSetup = DemoSetup.builder()
                .rayCount(100)
                .xSize(1080)
                .ySize(1080)
                .brightness(16)
                .injectedObject(leftTorus)
                .build();
        Painter painter = new Nose(leftSetup);
        Accumulator acc = new Accumulator(painter, Paths.get("./buffs/newLeftNostril/"), "./outputs/holes/newLeft%02d.ppm");

        while (true) {
            acc.accumulate();
        }
    }
}

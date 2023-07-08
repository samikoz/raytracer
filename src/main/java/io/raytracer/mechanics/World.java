package io.raytracer.mechanics;

import io.raytracer.tools.LinearColour;
import io.raytracer.tools.Camera;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.PPMPicture;
import io.raytracer.shapes.Shape;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class World {
    private final Function<IRay, IColour> background;
    private final List<Shape> contents;

    public World() {
        this.contents = new ArrayList<>();
        this.background = ray -> new LinearColour(0, 0, 0);
    }

    public World(Function<IRay, IColour> background) {
        this.contents = new ArrayList<>();
        this.background = background;
    }

    public World put(Shape object) {
        this.contents.add(object);
        return this;
    }

    public IColour illuminate(Collection<IRay> rays) {
        return rays.stream().map(this::illuminate).reduce(new LinearColour(0, 0, 0), IColour::add).multiply((double)1/rays.size());
    }

    abstract IColour illuminate(IRay ray);

    Collection<Intersection> intersect(@NonNull IRay ray) {
        return contents.stream().map(object -> object.intersect(ray))
                .flatMap(Arrays::stream).sorted().collect(Collectors.toList());
    }

    public IPicture render(Camera camera) {
        int totalRaysCount = camera.getPictureWidthPixels()*camera.getPictureHeightPixels();
        IPicture picture = new PPMPicture(camera.getPictureWidthPixels(), camera.getPictureHeightPixels());

        long renderStart = System.nanoTime();
        int minutesLeft = 0;
        int rayCount = 1;
        for (int y = 0; y < camera.getPictureHeightPixels() - 1; y++) {
            for (int x = 0; x < camera.getPictureWidthPixels() - 1; x++) {
                float progressPercent = (float)rayCount/totalRaysCount*100;
                if (rayCount % 100 == 0) {
                    long currentTime = System.nanoTime();
                    double timeLeft = (currentTime - renderStart)*(((double)totalRaysCount/rayCount) - 1);
                    minutesLeft = (int)(timeLeft/(60*Math.pow(10,9)));
                }
                System.out.printf("\r%.2f%%\t (%d out of %d); ~%d min left", progressPercent, rayCount, totalRaysCount, minutesLeft);
                Collection<IRay> rays = camera.getRaysThroughPixel(x, y);
                IColour colour = this.illuminate(rays);
                picture.write(x, y, colour);
                rayCount++;
            }
        }
        long renderEnd = System.nanoTime();
        int seconds = (int)((renderEnd - renderStart)/Math.pow(10, 9));
        int minutes = seconds / 60;
        System.out.println();
        System.out.printf("render took %d min, %d sec%n", minutes, seconds % 60);

        return picture;
    }

    public IColour getBackgroundAt(IRay ray) {
        return this.background.apply(ray);
    }
}

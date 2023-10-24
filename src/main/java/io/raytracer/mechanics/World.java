package io.raytracer.mechanics;

import io.raytracer.shapes.Hittable;
import io.raytracer.tools.Camera;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.LinearColour;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public abstract class World {
    private final Function<IRay, IColour> background;
    private final List<Hittable> contents;
    private volatile int renderMinutesLeft = 0;

    public World() {
        this.contents = new ArrayList<>();
        this.background = ray -> new LinearColour(0, 0, 0);
    }

    public World(Function<IRay, IColour> background) {
        this.contents = new ArrayList<>();
        this.background = background;
    }

    public World put(Hittable object) {
        this.contents.add(object);
        return this;
    }

    public World put(List<Hittable> objects) {
        this.contents.addAll(objects);
        return this;
    }

    public IColour illuminate(Collection<IRay> rays) {
        return rays.stream().map(this::illuminate).reduce(new LinearColour(0, 0, 0), IColour::add).multiply((double)1/rays.size());
    }

    abstract IColour illuminate(IRay ray);

    Optional<RayHit> intersect(@NonNull IRay ray) {
        return contents.stream().map(object -> object.intersect(ray))
                .filter(Optional::isPresent)
                .map(Optional::get).min(Comparator.comparingDouble(hit -> hit.rayParameter));
    }

    public void render(IPicture picture, Camera camera) {
        int totalPixelCount = (int)picture.getBlankPixels().count();

        long renderStart = System.nanoTime();
        AtomicInteger rayCount = new AtomicInteger(1);

        picture.getBlankPixels().parallel().forEach(pixelPair -> {
            int countSoFar = rayCount.get();
            float progressPercent = (float)countSoFar/totalPixelCount*100;
            if (countSoFar % 100 == 0) {
                long currentTime = System.nanoTime();
                double timeLeft = (currentTime - renderStart)*(((double)totalPixelCount/countSoFar) - 1);
                renderMinutesLeft = (int)(timeLeft/(60*Math.pow(10,9)));
                System.out.printf("\r%.2f%%  /  %d  / ~%d min left", progressPercent, totalPixelCount, renderMinutesLeft);
            }
            Collection<IRay> rays = camera.getRaysThroughPixel(pixelPair.getValue0(), pixelPair.getValue1());
            IColour colour = this.illuminate(rays);
            picture.write(pixelPair.getValue0(), pixelPair.getValue1(), colour);
            rayCount.getAndIncrement();
        });
        long renderEnd = System.nanoTime();
        int seconds = (int)((renderEnd - renderStart)/Math.pow(10, 9));
        int minutes = seconds / 60;
        System.out.println();
        System.out.printf("render took %d min, %d sec%n", minutes, seconds % 60);
    }

    public IColour getBackgroundAt(IRay ray) {
        return this.background.apply(ray);
    }
}

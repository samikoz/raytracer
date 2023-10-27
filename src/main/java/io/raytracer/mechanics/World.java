package io.raytracer.mechanics;

import io.raytracer.mechanics.reporters.DefaultReporter;
import io.raytracer.mechanics.reporters.RenderData;
import io.raytracer.mechanics.reporters.Reporter;
import io.raytracer.shapes.Hittable;
import io.raytracer.tools.Camera;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.LinearColour;
import io.raytracer.utils.StreamUtils;
import lombok.NonNull;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class World {
    private final Function<IRay, IColour> background;
    private final Function<RenderData, Reporter> reporterFactory;
    private final List<Hittable> contents;

    public World() {
        this.contents = new ArrayList<>();
        this.background = ray -> new LinearColour(0, 0, 0);
        this.reporterFactory = DefaultReporter::new;
    }

    public World(Function<IRay, IColour> background) {
        this.contents = new ArrayList<>();
        this.background = background;
        this.reporterFactory = DefaultReporter::new;
    }

    public World(Function<IRay, IColour> background, Function<RenderData, Reporter> reporterFactory) {
        this.contents = new ArrayList<>();
        this.background = background;
        this.reporterFactory = reporterFactory;
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
        int blankPixelCount = (int)picture.getBlankPixels().count();

        AtomicInteger pixelCount = new AtomicInteger(1);
        Stream<Pair<Integer, Integer>> blankPixels = picture.getBlankPixels().parallel().collect(StreamUtils.shuffledCollector());

        long renderStart = System.nanoTime();
        Reporter reporter = this.reporterFactory.apply(
                new RenderData(picture.getHeight()*picture.getWidth(), blankPixelCount, renderStart));

        blankPixels.forEach(pixelPair -> {
            Collection<IRay> rays = camera.getRaysThroughPixel(pixelPair.getValue0(), pixelPair.getValue1());
            IColour colour = this.illuminate(rays);
            picture.write(pixelPair.getValue0(), pixelPair.getValue1(), colour);
            int renderedPixelCount = pixelCount.getAndIncrement();
            reporter.report(renderedPixelCount);
        });

        reporter.summarise();
    }

    public IColour getBackgroundAt(IRay ray) {
        return this.background.apply(ray);
    }
}

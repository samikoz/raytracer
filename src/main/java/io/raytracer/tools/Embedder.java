package io.raytracer.tools;

import lombok.Builder;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class Embedder {
    private final Predicate<Pixel> readPixelTest;
    private final Function<Pixel, Pixel> pixelTransform;

    @Builder
    public Embedder(Predicate<Pixel> readPixelTest, Function<Pixel, Pixel> pixelTransform) {
        this.readPixelTest = Optional.ofNullable(readPixelTest).orElse(p -> true);
        this.pixelTransform = Optional.ofNullable(pixelTransform).orElse(p -> p);

    }

    public void embed(IPicture from, IPicture to) {
        for (int y = 0; y < from.getHeight(); y++) {
            for (int x = 0; x < from.getWidth(); x++) {
                Pixel p = new Pixel(x, y);
                if (this.readPixelTest.test(p)) {
                    to.write(this.pixelTransform.apply(p), from.read(x, y));
                }
            }
        }
    }
}

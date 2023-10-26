package io.raytracer.mechanics.reporters;

public interface Reporter {
    void report(int renderedPixelCount);

    void summarise();
}

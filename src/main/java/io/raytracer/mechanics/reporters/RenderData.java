package io.raytracer.mechanics.reporters;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RenderData {
    public int totalPixelCount;
    public int blankPixelCount;
    public long renderStart;
}

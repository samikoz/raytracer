package io.raytracer.worldly.materials;

import lombok.Builder;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class Glass extends Material {
    @Builder.Default
    public final double transparency = 1;
    @Builder.Default
    public final double refractive_index = 1.5;
}

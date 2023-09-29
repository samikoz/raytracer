package io.raytracer.mechanics;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TextureParameters {
    public final double u;
    public final double v;

    public TextureParameters() {
        this.u = 0;
        this.v = 0;
    }
}

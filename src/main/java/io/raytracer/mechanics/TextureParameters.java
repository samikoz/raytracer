package io.raytracer.mechanics;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public class TextureParameters {
    public final double u;
    public final double v;

    public TextureParameters() {
        this.u = 0;
        this.v = 0;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new double[] {u, v});
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        TextureParameters themTP = (TextureParameters) them;
        return (int)(this.u*1000) == (int)(themTP.u*1000) && (int)(this.v*1000) == (int)(themTP.v*1000);
    }
}

package io.raytracer.textures;

import io.raytracer.geometry.IPoint;
import io.raytracer.tools.IColour;

import java.util.Arrays;

public class BlendTexture extends Texture {
    private final Texture texture1;
    private final Texture texture2;

    public BlendTexture(Texture t1, Texture t2) {
        this.texture1 = t1;
        this.texture2 = t2;
    }

    @Override
    public IColour ownColourAt(IPoint p) {
        return this.texture1.colourAt(p).add(this.texture2.colourAt(p));
    }

    @Override
    public int getHashCode() {
        return Arrays.hashCode(new Texture[]{texture1, texture2});
    }
}

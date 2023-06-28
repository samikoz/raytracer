package io.raytracer.textures;

import io.raytracer.tools.LinearColour;
import io.raytracer.tools.IColour;
import io.raytracer.geometry.IPoint;

public class TestTexture extends Texture {
    @Override
    public IColour ownColourAt(IPoint p) {
        return new LinearColour(p.get(0), p.get(1), p.get(2));
    }

    @Override
    public int getHashCode() {
        return this.getClass().hashCode();
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;
        return true;
    }
}

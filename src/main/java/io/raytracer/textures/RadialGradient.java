package io.raytracer.textures;

import io.raytracer.geometry.IPoint;
import io.raytracer.tools.IColour;

import java.util.Arrays;

public class RadialGradient extends Texture {
    private final IColour centreColour;
    private final IColour brimColour;

    public RadialGradient(IColour centreColour, IColour brimColour) {
        this.centreColour = centreColour;
        this.brimColour = brimColour;
    }

    @Override
    IColour ownColourAt(IPoint p) {
        double radius = Math.sqrt(Math.pow(p.x(), 2) + Math.pow(p.y(), 2));
        return this.centreColour.interpolate(this.brimColour, radius);
    }

    @Override
    public int getHashCode() {
        return Arrays.hashCode(new int[] { 1, this.centreColour.hashCode(), this.brimColour.hashCode() });
    }
}

package io.raytracer.textures;

import io.raytracer.geometry.IPoint;
import io.raytracer.tools.IColour;
import io.raytracer.tools.LinearColour;

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
        double radius = Math.sqrt(Math.pow(p.get(0), 2) + Math.pow(p.get(1), 2));
        return this.centreColour.interpolate(this.brimColour, radius);
    }

    @Override
    public int getHashCode() {
        return Arrays.hashCode(new int[] { 1, this.centreColour.hashCode(), this.brimColour.hashCode() });
    }
}

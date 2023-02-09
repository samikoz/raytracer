package io.raytracer.drawing.patterns;

import io.raytracer.drawing.IColour;
import io.raytracer.geometry.IPoint;

public abstract class Pattern {
    public abstract IColour colourAt(IPoint p);

    public abstract int getHashCode();

    @Override
    public int hashCode() {
        return this.getHashCode();
    }
}

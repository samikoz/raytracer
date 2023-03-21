package io.raytracer.drawing.patterns;

import io.raytracer.drawing.Colour;
import io.raytracer.drawing.IColour;
import io.raytracer.geometry.IPoint;

public class TestPattern extends Pattern {
    @Override
    public IColour colourAt(IPoint p) {
        return new Colour(p.get(0), p.get(1), p.get(2));
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
package io.raytracer.drawing.patterns;

import io.raytracer.drawing.IColour;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.ThreeTransform;
import lombok.Getter;

public abstract class Pattern {
    @Getter private ITransform inverseTransform;
    public abstract IColour colourAt(IPoint p);

    public void setTransform(ITransform transform) {
        this.inverseTransform = transform.inverse();
    }

    Pattern() {
        this.setTransform(new ThreeTransform());
    }

    public abstract int getHashCode();

    @Override
    public int hashCode() {
        return this.getHashCode();
    }
}

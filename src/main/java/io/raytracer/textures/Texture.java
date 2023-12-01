package io.raytracer.textures;

import io.raytracer.tools.IColour;
import io.raytracer.geometry.IPoint;
import io.raytracer.algebra.ITransform;
import io.raytracer.algebra.ThreeTransform;
import lombok.Getter;

import java.util.Arrays;

public abstract class Texture {
    @Getter private ITransform inverseTransform;

    public IColour colourAt(IPoint p) {
        return this.ownColourAt(this.getInverseTransform().act(p));
    }
    abstract IColour ownColourAt(IPoint p);

    public void setTransform(ITransform transform) {
        this.inverseTransform = transform.inverse();
    }

    Texture() {
        this.setTransform(new ThreeTransform());
    }

    public abstract int getHashCode();

    protected int getTwoColourHashCode(IColour first, IColour second) {
        return Arrays.hashCode(new IColour[]{first, second});
    }

    @Override
    public int hashCode() {
        return this.getHashCode();
    }
}

package io.raytracer.textures;

import io.raytracer.tools.IColour;
import io.raytracer.geometry.IPoint;


public class GradientTexture extends Texture {
    private final IColour firstColour;
    private final IColour secondColour;

    public GradientTexture(IColour firstColour, IColour secondColour) {
        this.firstColour = firstColour;
        this.secondColour = secondColour;
    }

    @Override
    public IColour ownColourAt(IPoint p) {
        return firstColour.interpolate(this.secondColour, p.x() - Math.floor(p.x()));
    }

    @Override
    public int getHashCode() {
        return this.getTwoColourHashCode(this.firstColour, this.secondColour);
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        GradientTexture themGrad = (GradientTexture) them;
        return themGrad.firstColour.equals(this.firstColour) && themGrad.secondColour.equals(this.secondColour);
    }
}

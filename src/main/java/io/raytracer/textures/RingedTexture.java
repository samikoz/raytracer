package io.raytracer.textures;

import io.raytracer.tools.IColour;
import io.raytracer.geometry.IPoint;

public class RingedTexture extends Texture {
    private final IColour firstColour;
    private final IColour secondColour;

    public RingedTexture(IColour firstColour, IColour secondColour) {
        this.firstColour = firstColour;
        this.secondColour = secondColour;
    }

    @Override
    public IColour ownColourAt(IPoint p) {
        return Math.floor(Math.sqrt(Math.pow(p.get(0), 2) + Math.pow(p.get(2), 2))) % 2 == 0 ? this.firstColour : this.secondColour;
    }

    @Override
    public int getHashCode() {
        return this.getTwoColourHashCode(this.firstColour, this.secondColour);
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        RingedTexture themGrad = (RingedTexture) them;
        return themGrad.firstColour.equals(this.firstColour) && themGrad.secondColour.equals(this.secondColour);
    }
}

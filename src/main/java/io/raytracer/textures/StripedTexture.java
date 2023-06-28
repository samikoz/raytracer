package io.raytracer.textures;

import io.raytracer.tools.IColour;
import io.raytracer.geometry.IPoint;
import lombok.NonNull;

public class StripedTexture extends Texture {
    private final IColour firstColour;
    private final IColour secondColour;

    public StripedTexture(@NonNull IColour firstColour, @NonNull IColour secondColour) {
        super();
        this.firstColour = firstColour;
        this.secondColour = secondColour;
    }

    @Override
    public IColour ownColourAt(IPoint p) {
        return (int)Math.floor(p.get(0)) % 2 == 0 ? firstColour : secondColour;
    }

    @Override
    public int getHashCode() {
        return this.getTwoColourHashCode(this.firstColour, this.secondColour);
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        StripedTexture themStriped = (StripedTexture) them;
        return themStriped.firstColour.equals(this.firstColour) && themStriped.secondColour.equals(this.secondColour);
    }
}

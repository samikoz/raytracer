package io.raytracer.patterns;

import io.raytracer.tools.IColour;
import io.raytracer.geometry.IPoint;
import lombok.NonNull;

public class StripedPattern extends Pattern {
    private final IColour firstColour;
    private final IColour secondColour;

    public StripedPattern(@NonNull IColour firstColour, @NonNull IColour secondColour) {
        super();
        this.firstColour = firstColour;
        this.secondColour = secondColour;
    }

    @Override
    public IColour colourAt(IPoint p) {
        return (int)Math.floor(p.get(0)) % 2 == 0 ? firstColour : secondColour;
    }

    @Override
    public int getHashCode() {
        return this.getTwoColourHashCode(this.firstColour, this.secondColour);
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        StripedPattern themStriped = (StripedPattern) them;
        return themStriped.firstColour.equals(this.firstColour) && themStriped.secondColour.equals(this.secondColour);
    }
}

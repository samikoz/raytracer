package io.raytracer.drawing.patterns;

import io.raytracer.drawing.IColour;
import io.raytracer.geometry.IPoint;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Arrays;

@AllArgsConstructor
public class StripedPattern extends Pattern {
    @NonNull private IColour firstColour;
    @NonNull private IColour secondColour;

    @Override
    public IColour colourAt(IPoint p) {
        return (int)Math.floor(p.get(0)) % 2 == 0 ? firstColour : secondColour;
    }

    @Override
    public int getHashCode() {
        return Arrays.hashCode(new IColour[]{this.firstColour, this.secondColour});
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        StripedPattern themStriped = (StripedPattern) them;
        return themStriped.firstColour.equals(this.firstColour) && themStriped.secondColour.equals(this.secondColour);
    }
}

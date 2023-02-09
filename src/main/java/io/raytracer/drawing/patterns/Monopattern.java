package io.raytracer.drawing.patterns;

import io.raytracer.drawing.IColour;
import io.raytracer.geometry.IPoint;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class Monopattern extends Pattern {
    @NonNull private final IColour monocolour;

    public IColour colourAt(IPoint point) {
        return this.monocolour;
    }

    public int getHashCode() {
        return this.monocolour.hashCode();
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Monopattern themMonocolour = (Monopattern) them;
        return themMonocolour.monocolour.equals(this.monocolour);
    }
}

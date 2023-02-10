package io.raytracer.drawing.patterns;

import io.raytracer.drawing.IColour;
import io.raytracer.geometry.IPoint;
import lombok.NonNull;

public class Monopattern extends Pattern {
    @NonNull private final IColour monocolour;

    public Monopattern(IColour colour) {
        super();
        this.monocolour = colour;
    }

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

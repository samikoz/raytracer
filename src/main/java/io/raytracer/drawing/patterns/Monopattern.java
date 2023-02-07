package io.raytracer.drawing.patterns;

import io.raytracer.drawing.Colour;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;

public class Monopattern implements IPattern {
    private final Colour monocolour;

    public Monopattern(Colour monocolour) {
        this.monocolour = monocolour;
    }

    public Colour colourAt(IPoint point) {
        return this.monocolour;
    }

    public int getHashCode() {
        return this.monocolour.hashCode();
    }

    @Override
    public int hashCode() {
        return this.getHashCode();
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Monopattern themMonocolour = (Monopattern) them;
        return themMonocolour.monocolour.equals(this.monocolour);
    }
}

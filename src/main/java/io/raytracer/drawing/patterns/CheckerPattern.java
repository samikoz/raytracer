package io.raytracer.drawing.patterns;

import io.raytracer.drawing.IColour;
import io.raytracer.geometry.IPoint;

public class CheckerPattern extends Pattern {
    private final IColour firstColour;
    private final IColour secondColour;

    public CheckerPattern(IColour firstColour, IColour secondColour) {
        this.firstColour = firstColour;
        this.secondColour = secondColour;
    }

    @Override
    public IColour colourAt(IPoint p) {
        return (Math.floor(p.get(0)) + Math.floor(p.get(1)) + Math.floor(p.get(2))) % 2 == 0 ? this.firstColour : this.secondColour;
    }

    @Override
    public int getHashCode() {
        return this.getTwoColourHashCode(this.firstColour, this.secondColour);
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        CheckerPattern themGrad = (CheckerPattern) them;
        return themGrad.firstColour.equals(this.firstColour) && themGrad.secondColour.equals(this.secondColour);
    }
}

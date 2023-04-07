package io.raytracer.textures;

import io.raytracer.tools.IColour;
import io.raytracer.geometry.IPoint;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CheckerTexture extends Texture {
    private final IColour firstColour;
    private final IColour secondColour;
    private static final int decimalPlacesTolerance = 6;

    public CheckerTexture(IColour firstColour, IColour secondColour) {
        this.firstColour = firstColour;
        this.secondColour = secondColour;
    }

    @Override
    public IColour colourAt(IPoint p) {
        double roundedFirst = this.roundToTolerance(p.get(0));
        double roundedSecond = this.roundToTolerance(p.get(1));
        double roundedThird = this.roundToTolerance(p.get(2));
        return (Math.floor(roundedFirst) + Math.floor(roundedSecond) + Math.floor(roundedThird)) % 2 == 0 ? this.firstColour : this.secondColour;
    }

    private double roundToTolerance(double x) {
        BigDecimal bd = new BigDecimal(Double.toString(x));
        bd = bd.setScale(CheckerTexture.decimalPlacesTolerance, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public int getHashCode() {
        return this.getTwoColourHashCode(this.firstColour, this.secondColour);
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        CheckerTexture themGrad = (CheckerTexture) them;
        return themGrad.firstColour.equals(this.firstColour) && themGrad.secondColour.equals(this.secondColour);
    }
}

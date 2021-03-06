package io.raytracer.drawing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;

@ToString
@RequiredArgsConstructor
public class ColourImpl implements Colour {
    @Getter private final double red;
    @Getter private final double green;
    @Getter private final double blue;

    private final static double equalityTolerance = 1e-3;
    private final static int exportScale = 255;

    @Override
    public int hashCode() {
        return Arrays.hashCode(new double[]{this.getRed(), this.getGreen(), this.getBlue()});
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        ColourImpl themColour = (ColourImpl) them;
        return (this.distance(themColour) < equalityTolerance);
    }

    private double distance(ColourImpl them) {
        return Math.sqrt(
                Math.pow(this.getRed() - them.getRed(), 2) +
                        Math.pow(this.getGreen() - them.getGreen(), 2) +
                        Math.pow(this.getBlue() - them.getBlue(), 2)
        );
    }

    private int normaliseComponent(double componentValue) {
        if (componentValue <= 0) {
            componentValue = 0;
        } else if (componentValue >= 1) {
            componentValue = 1;
        }
        return (int) Math.round(componentValue * exportScale);
    }

    @Override
    public String exportNormalised() {
        return normaliseComponent(getRed()) + " " +
                normaliseComponent(getGreen()) + " " +
                normaliseComponent(getBlue());
    }

    @Override
    public ColourImpl add(Colour them) {
        return new ColourImpl(
                this.getRed() + them.getRed(),
                this.getGreen() + them.getGreen(),
                this.getBlue() + them.getBlue()
        );
    }

    @Override
    public Colour multiply(double scalar) {
        return new ColourImpl(
                scalar * this.getRed(),
                scalar * this.getGreen(),
                scalar * this.getBlue()
        );
    }

    @Override
    public Colour mix(Colour them) {
        return new ColourImpl(
                this.getRed() * them.getRed(),
                this.getGreen() * them.getGreen(),
                this.getBlue() * them.getBlue()
        );
    }
}

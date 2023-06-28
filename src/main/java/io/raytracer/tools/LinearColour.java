package io.raytracer.tools;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.stream.Stream;

@ToString
@RequiredArgsConstructor
public class LinearColour implements IColour {
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

        LinearColour themColour = (LinearColour) them;
        return (this.distance(themColour) < equalityTolerance);
    }

    private double distance(LinearColour them) {
        return Math.sqrt(
            Math.pow(this.getRed() - them.getRed(), 2) +
            Math.pow(this.getGreen() - them.getGreen(), 2) +
            Math.pow(this.getBlue() - them.getBlue(), 2)
        );
    }

    protected double clamp(double componentValue) {
        if (componentValue <= 0) {
            componentValue = 0;
        } else if (componentValue >= 1) {
            componentValue = 1;
        }
        return componentValue;
    }

    protected int scale(double value) {
        return (int) Math.round(value * exportScale);
    }

    @Override
    public String export() {
        return Stream.of(this.getRed(), this.getGreen(), this.getBlue())
            .mapToDouble(this::clamp).mapToInt(this::scale)
            .mapToObj(String::valueOf).reduce((c1, c2) -> c1 + " " + c2).get();
    }

    @Override
    public LinearColour add(IColour them) {
        return new LinearColour(
                this.getRed() + them.getRed(),
                this.getGreen() + them.getGreen(),
                this.getBlue() + them.getBlue()
        );
    }

    @Override
    public IColour multiply(double scalar) {
        return new LinearColour(
                scalar * this.getRed(),
                scalar * this.getGreen(),
                scalar * this.getBlue()
        );
    }

    @Override
    public IColour mix(IColour them) {
        return new LinearColour(
                this.getRed() * them.getRed(),
                this.getGreen() * them.getGreen(),
                this.getBlue() * them.getBlue()
        );
    }

    @Override
    public IColour interpolate(IColour them, double factor) {
        factor = Math.max(Math.min(1, factor), 0);
        return this.add(them.subtract(this).multiply(factor));
    }
}

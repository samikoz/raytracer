package io.raytracer.structures;

public class TupleColour extends Real3Tuple implements Colour  {

    public TupleColour(double red, double green, double blue) {
        super(red, green, blue);
    }

    TupleColour(Tuple them) {
        this(them.getX(), them.getY(), them.getZ());
    }

    @Override
    public double getRed() {
        return super.getX();
    }

    @Override
    public double getGreen() {
        return super.getY();
    }

    @Override
    public double getBlue() {
        return super.getZ();
    }

    @Override
    public Colour add(Colour them) {
        return new TupleColour(super.add(them));
    }

    @Override
    public Colour subtract(Colour them) {
        return new TupleColour(super.subtract(them));
    }

    @Override
    public Colour multiply(double scalar) {
        return new TupleColour(super.multiply(scalar));
    }

    @Override
    public Colour mix(Colour them) {
        return new TupleColour(
            this.getRed()*them.getRed(),
            this.getGreen()*them.getGreen(),
            this.getBlue()*them.getBlue()
        );
    }
}

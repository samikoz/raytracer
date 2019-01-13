package io.raytracer.structures;

public class Real3Tuple implements Tuple {
    private double x;
    private double y;
    private double z;

    public Real3Tuple(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public boolean equalTo(Tuple them) {
        return (this.getClass() == them.getClass() && this.distance(them) < 1e-3);
    }

    double distance(Tuple them) {
        return Math.sqrt(
            Math.pow(this.getX() - them.getX(), 2) +
            Math.pow(this.getY() - them.getY(), 2) +
            Math.pow(this.getZ() - them.getZ(), 2)
        );
    }
}

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
}

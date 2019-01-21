package io.raytracer.mathsy;

import java.util.Arrays;

class Real3Tuple implements Vector, Point {
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
    public int hashCode() {
        double[] coords = {this.getX(), this.getY(), this.getZ()};
        return Arrays.hashCode(coords);
    }

    @Override
    public boolean equals(Object them) {
        if (this.getClass() != them.getClass()) return false;

        Real3Tuple themTuple = (Real3Tuple) them;
        return (this.distance(themTuple) < 1e-3);
    }

    private double distance(Real3Tuple them) {
        return Math.sqrt(
            Math.pow(this.getX() - them.getX(), 2) +
            Math.pow(this.getY() - them.getY(), 2) +
            Math.pow(this.getZ() - them.getZ(), 2)
        );
    }

    @Override
    public double distance(Vector them) {
        return distance((Real3Tuple) them);
    }

    @Override
    public double distance(Point them) {
        return distance((Real3Tuple) them);
    }

    @Override
    public double norm() {
        return distance(new Real3Tuple(0,0,0));
    }

    @Override
    public Real3Tuple normalise() {
        return this.multiply(1/this.norm());
    }

    private Real3Tuple add(Real3Tuple them) {
        return new Real3Tuple(
            this.getX() + them.getX(),
            this.getY() + them.getY(),
            this.getZ() + them.getZ()
        );
    }

    @Override
    public Real3Tuple add(Vector them) {
        return add((Real3Tuple) them);
    }

    private Real3Tuple subtract(Real3Tuple them) {
        return new Real3Tuple(
            this.getX() - them.getX(),
            this.getY() - them.getY(),
            this.getZ() - them.getZ()
        );
    }

    @Override
    public Real3Tuple subtract(Vector them) {
        return subtract((Real3Tuple) them);
    }

    @Override
    public Real3Tuple subtract(Point them) {
        return subtract((Real3Tuple) them);
    }

    @Override
    public Real3Tuple negate() {
        return new Real3Tuple(0, 0, 0).subtract(this);
    }

    public Real3Tuple multiply(double scalar) {
        return new Real3Tuple(
            scalar * this.getX(),
            scalar * this.getY(),
            scalar * this.getZ()
        );
    }

    @Override
    public double dot(Vector them) {
        return this.getX()*them.getX() + this.getY()*them.getY() + this.getZ()*them.getZ();
    }

    @Override
    public Real3Tuple cross(Vector them) {
        return new Real3Tuple(
            this.getY()*them.getZ() - this.getZ()*them.getY(),
            this.getZ()*them.getX() - this.getX()*them.getZ(),
            this.getX()*them.getY() - this.getY()*them.getX()
        );
    }
}

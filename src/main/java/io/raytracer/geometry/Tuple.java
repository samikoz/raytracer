package io.raytracer.geometry;

import lombok.ToString;

import java.util.Arrays;

@ToString
class Tuple implements ITuple {
    private final double x, y, z;

    public Tuple(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new double[] {(int)this.x*1000, (int)this.y*1000, (int)this.z*1000});
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        ITuple themTuple = (ITuple) them;
        return (Math.round(this.x*1000) == Math.round(themTuple.x()*1000) && Math.round(this.y*1000) == Math.round(themTuple.y()*1000) && Math.round(this.z*1000) == Math.round(themTuple.z()*1000));
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }

    public double z() {
        return this.z;
    }

    ITuple add(ITuple them) {
        return new Tuple(this.x + them.x(), this.y + them.y(), this.z + them.z());
    }

    public ITuple multiply(double scalar) {
        return new Tuple(scalar*this.x, scalar*this.y, scalar*this.z);
    }

    double euclideanDistance(ITuple them) {
        return Math.sqrt(Math.pow(this.x - them.x(), 2) + Math.pow(this.y - them.y(), 2) + Math.pow(this.z - them.z(), 2));
    }
}

package io.raytracer.geometry;

import lombok.ToString;

import java.util.Arrays;

@ToString
class Tuple4 {
    public final double x, y, z, w;

    public Tuple4(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new double[]{(int) this.x * 1000, (int) this.y * 1000, (int) this.z * 1000, (int) this.w * 1000});
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Tuple4 themTuple = (Tuple4) them;
        return ((int) this.x * 1000 == (int) themTuple.x * 1000 && (int) this.y * 1000 == themTuple.y * 1000 && (int) this.z * 1000 == (int) themTuple.z * 1000 && (int) themTuple.w * 1000 == (int) this.w * 1000);
    }
}
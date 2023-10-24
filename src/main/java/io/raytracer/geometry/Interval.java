package io.raytracer.geometry;


import java.util.Arrays;

public class Interval {
    public double min, max;
    private static double equalityTolerance = 1e-3;

    public Interval() {
        this.min = 0;
        this.max = 0;
    }

    public Interval(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public Interval(Interval i1, Interval i2) {
        this.min = Math.min(i1.min, i2.min);
        this.max = Math.max(i1.max, i2.max);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.toArray());
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Interval themInterval = (Interval) them;
        return Math.abs(this.min - themInterval.min) < Interval.equalityTolerance && Math.abs(this.max - themInterval.max) < Interval.equalityTolerance;
    }

    public double[] toArray() {
        return new double[] {this.min, this.max};
    }

    public double size() {
        return this.max - this.min;
    }

    public Interval expand(double delta) {
        return new Interval(this.min - delta/2, this.max + delta/2);
    }
}

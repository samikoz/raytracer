package io.raytracer.geometry;


public class Interval {
    public double min, max;

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

    public double size() {
        return this.max - this.min;
    }

    public Interval expand(double delta) {
        return new Interval(this.min - delta/2, this.max + delta/2);
    }
}

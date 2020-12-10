package io.raytracer.light;

public class Intersection {
    public double time;
    public Drawable object;

    public Intersection(double time, Drawable object) {
        this.time = time;
        this.object = object;
    }
}

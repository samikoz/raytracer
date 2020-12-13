package io.raytracer.light;

public interface Drawable {
    public IntersectionList intersect(Ray ray);
}

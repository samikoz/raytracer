package io.raytracer.light;

import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.Vector;

public class Sphere implements Drawable {
    @Override
    public IntersectionList intersect(Ray ray) {
        Vector rayOrigin = ray.getOrigin().subtract(new PointImpl(0, 0, 0));
        double a = ray.getDirection().dot(ray.getDirection());
        double b = 2 * ray.getDirection().dot(rayOrigin);
        double c = rayOrigin.dot(rayOrigin) - 1;
        double delta = Math.pow(b, 2) - 4 * a * c;

        if (delta < 0) {
            return new IntersectionListImpl();
        }
        return new IntersectionListImpl(
                new Intersection((-b - Math.sqrt(delta))/(2*a), this),
                new Intersection((-b + Math.sqrt(delta))/(2*a), this));
    }
}

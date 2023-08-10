package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.IVector;

import io.raytracer.materials.Material;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import lombok.NonNull;

import java.util.Arrays;

public class Sphere extends Shape {
    public Sphere() {
        super();
    }

    public Sphere(@NonNull Material material) {
        super(material);
    }

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, double tmin, double tmax) {
        IVector rayOrigin = ray.getOrigin().subtract(new Point(0, 0, 0));
        double a = ray.getDirection().dot(ray.getDirection());
        double b = 2 * ray.getDirection().dot(rayOrigin);
        double c = rayOrigin.dot(rayOrigin) - 1;
        double delta = Math.pow(b, 2) - 4 * a * c;

        if (delta < 0) {
            return new Intersection[] {};
        }
        double[] roots = new double[] { (-b - Math.sqrt(delta)) / (2 * a), (-b + Math.sqrt(delta)) / (2 * a) };
        return Arrays.stream(roots)
            .filter(root -> root > tmin && root < tmax)
            .mapToObj(position -> new Intersection(this, ray, position, 0, 0))
            .toArray(Intersection[]::new);
    }

    @Override
    public IVector localNormalAt(@NonNull IPoint p) {
        return (p.subtract(new Point(0, 0, 0))).normalise();
    }
}

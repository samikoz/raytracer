package io.raytracer.shapes;

import io.raytracer.algebra.Equation;
import io.raytracer.algebra.IEquation;
import io.raytracer.geometry.*;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.TextureParameters;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Sphere extends Shape {
    public Sphere() {
        super();
    }

    public Sphere(@NonNull Material material) {
        super(material);
    }

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, Interval rayDomain) {
        IVector rayOrigin = ray.getOrigin().subtract(new Point(0, 0, 0));
        double a = ray.getDirection().dot(ray.getDirection());
        double b = 2 * ray.getDirection().dot(rayOrigin);
        double c = rayOrigin.dot(rayOrigin) - 1;

        IEquation eqn = new Equation(c, b, a);
        List<Intersection> intersections = new ArrayList<>();
        for(double root : eqn.solve() ) {
            intersections.add(new Intersection(this, ray, root, new TextureParameters()));
        }
        return intersections.toArray(new Intersection[] {});
    }

    @Override
    public IVector localNormalAt(@NonNull IPoint pt, TextureParameters p) {
        return (pt.subtract(new Point(0, 0, 0))).normalise();
    }

    @Override
    public BBox getLocalBoundingBox() {
        return new BBox(new Point(-1, -1, -1), new Point(1, 1, 1));
    }
}

package io.raytracer.light;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.ThreeTransformation;
import io.raytracer.geometry.Transformation;
import io.raytracer.geometry.Vector;

public class SphereImpl implements Sphere {
    private Transformation transform;
    private final Material material;

    public SphereImpl() {
        this.transform = new ThreeTransformation();
        this.material = new Material();
    }

    public SphereImpl(Material material) {
        this.transform = new ThreeTransformation();
        this.material = material;
    }

    @Override
    public Transformation getTransform() {
        return this.transform;
    }

    @Override
    public void setTransform(Transformation t) {
        this.transform = t;
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public IntersectionList intersect(Ray ray) {
        Ray transformedRay = ray.transform(this.transform.inverse());
        Vector rayOrigin = transformedRay.getOrigin().subtract(new PointImpl(0, 0, 0));
        double a = transformedRay.getDirection().dot(transformedRay.getDirection());
        double b = 2 * transformedRay.getDirection().dot(rayOrigin);
        double c = rayOrigin.dot(rayOrigin) - 1;
        double delta = Math.pow(b, 2) - 4 * a * c;

        if (delta < 0) {
            return new IntersectionListImpl();
        }
        return new IntersectionListImpl(
                new Intersection((-b - Math.sqrt(delta))/(2*a), this),
                new Intersection((-b + Math.sqrt(delta))/(2*a), this));
    }

    @Override
    public Vector normal(Point p) {
        Transformation inverseTransform = this.transform.inverse();
        Point spherePoint = inverseTransform.act(p);
        Vector sphereNormal = (spherePoint.subtract(new PointImpl(0, 0, 0))).normalise();
        return inverseTransform.transpose().act(sphereNormal).normalise();
    }
}

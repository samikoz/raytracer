package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.TextureParameters;
import lombok.NonNull;

import java.util.Optional;

import static java.lang.Math.abs;

public class Plane extends Shape {
    private static final double parallelTolerance = 1e-3;

    public Plane() {
        super();
    }

    public Plane(@NonNull Material material) {
        super(material);
    }

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, double tmin, double tmax) {
        if (abs(ray.getDirection().get(1)) < parallelTolerance) {
            return new Intersection[]{};
        }
        double intersection = -ray.getOrigin().get(1) / ray.getDirection().get(1);
        if (intersection < tmin || intersection > tmax) {
            return new Intersection[] {};
        }
        return new Intersection[] { new Intersection(this, ray, -ray.getOrigin().get(1) / ray.getDirection().get(1),new TextureParameters()) };
    }

    @Override
    protected IVector localNormalAt(IPoint point, double u, double v) {
        return new Vector(0, 1, 0);
    }

    @Override
    protected Optional<BBox> getLocalBoundingBox() {
        return Optional.empty();
    }
}

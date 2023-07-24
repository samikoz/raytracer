package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.IRay;
import lombok.NonNull;

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
    protected double[] getLocalIntersectionPositions(IRay ray, double tmin, double tmax) {
        if (abs(ray.getDirection().get(1)) < parallelTolerance) {
            return new double[]{};
        }
        double intersection = -ray.getOrigin().get(1) / ray.getDirection().get(1);
        if (intersection < tmin || intersection > tmax) {
            return new double[] {};
        }
        return new double[] { -ray.getOrigin().get(1) / ray.getDirection().get(1) };
    }

    @Override
    protected IVector normalLocally(IPoint point) {
        return new Vector(0, 1, 0);
    }
}

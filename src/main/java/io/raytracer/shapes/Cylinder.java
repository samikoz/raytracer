package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Interval;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.TextureParameters;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Cylinder extends Shape {
    @Setter public double upperBound;
    @Setter public double lowerBound;
    @Setter public boolean isTopClosed;
    @Setter public boolean isBottomClosed;

    private static final double tolerance = 1e-7;

    public Cylinder() {
        super();
        this.upperBound = Double.POSITIVE_INFINITY;
        this.lowerBound = Double.NEGATIVE_INFINITY;
        this.isTopClosed = false;
        this.isBottomClosed = false;
    }

    public Cylinder(@NonNull Material material) {
        super(material);
        this.upperBound = Double.POSITIVE_INFINITY;
        this.lowerBound = Double.NEGATIVE_INFINITY;
        this.isTopClosed = false;
        this.isBottomClosed = false;
    }

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, Interval rayDomain) {
        double[] sideIntersections = this.getSideIntersections(ray);
        List<Double> allIntersections = DoubleStream.of(sideIntersections).boxed().collect(Collectors.toCollection(ArrayList::new));
        if (Math.abs(ray.getDirection().get(1)) > Cylinder.tolerance) {
            double lowerEndParameter = (this.lowerBound - ray.getOrigin().get(1)) / ray.getDirection().get(1);
            if (this.isBottomClosed && this.isWithinAtParameter(ray, lowerEndParameter)) {
                allIntersections.add(lowerEndParameter);
            }
            double upperEndParameter = (this.upperBound - ray.getOrigin().get(1)) / ray.getDirection().get(1);
            if (this.isTopClosed && this.isWithinAtParameter(ray, upperEndParameter)) {
                allIntersections.add(upperEndParameter);
            }
        }
        return allIntersections.stream()
            .mapToDouble(d -> d).filter(i -> i > rayDomain.min && i< rayDomain.max)
            .mapToObj(position -> new Intersection(this, ray, position, new TextureParameters()))
            .toArray(Intersection[]::new);
    }

    private double[] getSideIntersections(IRay ray) {
        IVector rayDirection = ray.getDirection();
        IPoint rayOrigin = ray.getOrigin();
        double a = Math.pow(rayDirection.get(0), 2) + Math.pow(rayDirection.get(2), 2);
        if (Math.abs(a) < tolerance) {
            return new double[] {};
        }

        double b = 2*rayOrigin.get(0)*rayDirection.get(0) + 2*rayOrigin.get(2)*rayDirection.get(2);
        double c = Math.pow(rayOrigin.get(0), 2) + Math.pow(rayOrigin.get(2), 2) - 1;
        double delta = Math.pow(b, 2) - 4*a*c;
        if (delta < 0) {
            return new double[] {};
        }

        double[] fullCylinderIntersections = new double[] {
            (-b - Math.sqrt(delta))/(2*a),
            (-b + Math.sqrt(delta))/(2*a)
        };
        return Arrays.stream(fullCylinderIntersections)
            .filter(i -> ray.getPosition(i).get(1) > this.lowerBound && ray.getPosition(i).get(1) < this.upperBound)
            .toArray();
    }

    private boolean isWithinAtParameter(IRay ray, double t) {
        double x = ray.getOrigin().get(0) + t*ray.getDirection().get(0);
        double z = ray.getOrigin().get(2) + t*ray.getDirection().get(2);
        return Math.pow(x, 2) + Math.pow(z, 2) <= 1;
    }

    @Override
    protected IVector localNormalAt(IPoint point, double u, double v) {
        double yDistance = Math.pow(point.get(0), 2) + Math.pow(point.get(2), 2);

        if (yDistance < 1  && point.get(1) >= this.upperBound - Cylinder.tolerance) {
            return new Vector(0, 1, 0);
        }
        if (yDistance < 1 && point.get(1) <= this.lowerBound + Cylinder.tolerance) {
            return new Vector(0, -1, 0);
        }
        return new Vector(point.get(0), 0, point.get(2));
    }

    @Override
    protected BBox getLocalBoundingBox() {
        return new BBox(new Point(-1, this.lowerBound, -1), new Point(1, this.upperBound, 1));
    }
}

package io.raytracer.shapes;

import io.raytracer.geometry.*;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.TextureParameters;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;

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

    protected Intersection[] getLocalIntersections(IRay ray, Interval rayDomain) {
        ArrayList<Double> intersectionPositions = this.getSideIntersections(ray);
        if (Math.abs(ray.getDirection().y()) > Cylinder.tolerance) {
            if (this.isBottomClosed) {
                double lowerEndParameter = (this.lowerBound - ray.getOrigin().y()) / ray.getDirection().y();
                if (this.isWithinAtParameter(ray, lowerEndParameter)) {
                    intersectionPositions.add(lowerEndParameter);
                }
            }
            if (this.isTopClosed) {
                double upperEndParameter = (this.upperBound - ray.getOrigin().y()) / ray.getDirection().y();
                if (this.isWithinAtParameter(ray, upperEndParameter)) {
                    intersectionPositions.add(upperEndParameter);
                }
            }
        }
        Intersection[] intersections = new Intersection[intersectionPositions.size()];
        for (int i = 0; i < intersectionPositions.size(); i++) {
            intersections[i] = new Intersection(this, ray, intersectionPositions.get(i), new TextureParameters());
        }
        return intersections;
    }

    private ArrayList<Double> getSideIntersections(IRay ray) {
        IVector rayDirection = ray.getDirection();
        IPoint rayOrigin = ray.getOrigin();
        double a = Math.pow(rayDirection.x(), 2) + Math.pow(rayDirection.z(), 2);
        double b = 2*rayOrigin.x()*rayDirection.x() + 2*rayOrigin.z()*rayDirection.z();
        double c = Math.pow(rayOrigin.x(), 2) + Math.pow(rayOrigin.z(), 2) - 1;
        IEquation eqn = new Equation(c, b, a);

        ArrayList<Double> intersections = new ArrayList<>(2);
        for (double solution : eqn.solve()) {
            double rayYPosition = ray.getOrigin().y() + solution*ray.getDirection().y();
            if (rayYPosition > this.lowerBound && rayYPosition < this.upperBound) {
                intersections.add(solution);
            }
        }
        return intersections;
    }

    private boolean isWithinAtParameter(IRay ray, double t) {
        double xPosition = ray.getOrigin().x() + t*ray.getDirection().x();
        double zPosition = ray.getOrigin().z() + t*ray.getDirection().z();
        return Math.pow(xPosition, 2) + Math.pow(zPosition, 2) <= 1;
    }

    @Override
    protected IVector localNormalAt(IPoint point, TextureParameters p) {
        double yDistance = Math.pow(point.x(), 2) + Math.pow(point.z(), 2);

        if (yDistance < 1  && point.y() >= this.upperBound - Cylinder.tolerance) {
            return new Vector(0, 1, 0);
        }
        if (yDistance < 1 && point.y() <= this.lowerBound + Cylinder.tolerance) {
            return new Vector(0, -1, 0);
        }
        return new Vector(point.x(), 0, point.z());
    }

    @Override
    protected BBox getLocalBoundingBox() {
        return new BBox(new Point(-1, this.lowerBound, -1), new Point(1, this.upperBound, 1));
    }
}

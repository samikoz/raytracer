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
import java.util.List;

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
        List<Intersection> filteredIntersections = new ArrayList<>();
        for (double position : intersectionPositions) {
            if (position > rayDomain.min && position < rayDomain.max) {
                filteredIntersections.add(new Intersection(this, ray, position, new TextureParameters()));
            }
        }
        return filteredIntersections.toArray(new Intersection[] {});
    }

    private ArrayList<Double> getSideIntersections(IRay ray) {
        IVector rayDirection = ray.getDirection();
        IPoint rayOrigin = ray.getOrigin();
        double a = Math.pow(rayDirection.x(), 2) + Math.pow(rayDirection.z(), 2);
        if (Math.abs(a) < tolerance) {
            return new ArrayList<>();
        }

        double b = 2*rayOrigin.x()*rayDirection.x() + 2*rayOrigin.z()*rayDirection.z();
        double c = Math.pow(rayOrigin.x(), 2) + Math.pow(rayOrigin.z(), 2) - 1;
        double delta = Math.pow(b, 2) - 4*a*c;
        if (delta < 0) {
            return new ArrayList<>();
        }

        ArrayList<Double> intersections = new ArrayList<>(4);
        for (double solution : new double[] {
                (-b - Math.sqrt(delta))/(2*a),
                (-b + Math.sqrt(delta))/(2*a)
        } ) {
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
    protected IVector localNormalAt(IPoint point, double u, double v) {
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

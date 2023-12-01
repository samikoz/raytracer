package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.algebra.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Interval;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.RayHit;
import io.raytracer.mechanics.TextureParameters;
import lombok.NonNull;

import java.util.Optional;
import java.util.Random;

import static java.lang.Math.log;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Volume extends Shape {
    private final double densityParameter;
    private final Shape boundary; //assumed convex

    private static final Random randGen = new Random();

    public Volume(double density, Shape boundary) {
        this.densityParameter = -1/density;
        this.boundary = boundary;
    }

    public Volume(double density, Shape boundary, @NonNull Material material) {
        super(material);
        this.densityParameter = -1/density;
        this.boundary = boundary;
    }

    @Override
    public void setTransform(ITransform transform) {
        super.setTransform(transform);
        this.boundary.setTransform(transform);
    }

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, Interval rayDomain) {
        Optional<RayHit> leftHit = RayHit.fromIntersections(this.boundary.intersect(ray, new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));
        if (!leftHit.isPresent()) {
            return new Intersection[]{};
        }
        double leftParameter = leftHit.get().rayParameter;
        Optional<RayHit> rightHit = RayHit.fromIntersections(this.boundary.intersect(ray, new Interval(leftParameter + 0.0001, Double.POSITIVE_INFINITY)));
        if (!rightHit.isPresent()) {
            return new Intersection[]{};
        }
        double rightParameter = rightHit.get().rayParameter;
        leftParameter = max(rayDomain.min, leftParameter);
        rightParameter = min(rayDomain.max, rightParameter);
        if (leftParameter >= rightParameter) {
            return new Intersection[]{};
        }
        leftParameter = max(leftParameter, 0);

        double rayLength = ray.getDirection().norm();
        double distInside = (rightParameter - leftParameter)*rayLength;
        double hitDist = this.densityParameter*log(Volume.randGen.nextDouble());
        if (hitDist > distInside) {
            return new Intersection[]{};
        }
        return new Intersection[] { new Intersection(this, ray, leftParameter + (hitDist / rayLength), new TextureParameters())};

    }

    @Override
    protected IVector localNormalAt(IPoint point, TextureParameters p) {
        return new Vector(1, 0, 0);
    }

    @Override
    protected BBox getLocalBoundingBox() {
        return this.boundary.getLocalBoundingBox();
    }
}

package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;
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
    protected Intersection[] getLocalIntersections(IRay ray, double tmin, double tmax) {
        Optional<RayHit> leftHit = this.boundary.intersect(ray, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        if (!leftHit.isPresent()) {
            return new Intersection[]{};
        }
        double leftParameter = leftHit.get().rayParameter;
        Optional<RayHit> rightHit = this.boundary.intersect(ray, leftParameter + 0.0001, Double.POSITIVE_INFINITY);
        if (!rightHit.isPresent()) {
            return new Intersection[]{};
        }
        double rightParameter = rightHit.get().rayParameter;
        leftParameter = max(tmin, leftParameter);
        rightParameter = min(tmax, rightParameter);
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
        return new Intersection[] { new Intersection(this, ray, leftParameter + (hitDist / rayLength), new TextureParameters(0, 0))};

    }

    @Override
    protected IVector localNormalAt(IPoint point, double u, double v) {
        return new Vector(1, 0, 0);
    }

    @Override
    protected Optional<BBox> getLocalBoundingBox() {
        return this.boundary.getLocalBoundingBox();
    }
}

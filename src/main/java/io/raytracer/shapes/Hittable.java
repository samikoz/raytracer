package io.raytracer.shapes;

import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Optional;

public abstract class Hittable {
    @Getter protected ITransform transform;
    @Getter private ITransform inverseTransform;
    @Getter @Setter private boolean isCastingShadows;

    public void setTransform(ITransform transform) {
        this.transform = transform;
        this.inverseTransform = transform.inverse();
    }

    public Hittable() {
        this.transform = new ThreeTransform();
        this.inverseTransform = new ThreeTransform();
        this.isCastingShadows = true;
    }

    abstract protected Intersection[] getLocalIntersections(IRay ray, double tmin, double tmax);

    abstract protected Optional<BBox> getLocalBoundingBox();

    public Optional<BBox> getBoundingBox() {
        return this.getLocalBoundingBox().map(box -> box.transform(this.transform));
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Hittable themShape = (Hittable) them;
        return this.inverseTransform.equals(themShape.inverseTransform);
    }

    @Override
    public int hashCode() {
        return this.inverseTransform.hashCode();
    }

    public Intersection[] intersect(IRay ray) {
        return this.intersect(ray, 0, Double.POSITIVE_INFINITY);
    }

    public Intersection[] intersect(IRay ray, double tmin, double tmax) {
        IRay transformedRay = ray.getTransformed(this.inverseTransform);
        return Arrays.stream(this.getLocalIntersections(transformedRay, tmin, tmax)).map(ray::reintersect).toArray(Intersection[]::new);
    }
}

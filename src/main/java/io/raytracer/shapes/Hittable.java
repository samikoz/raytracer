package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Interval;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.RayHit;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class Hittable {
    private Group parent;
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

    public Optional<Group> getParent() {
        return Optional.ofNullable(this.parent);
    }

    protected void setParent(Group group) {
        this.parent = group;
    }

    abstract protected Intersection[] getLocalIntersections(IRay ray, Interval rayDomain);

    abstract protected BBox getLocalBoundingBox();

    public BBox getBoundingBox() {
        return this.getLocalBoundingBox().transform(this.transform);
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

    public Optional<RayHit> intersect(IRay ray) {
        return this.intersect(ray, Interval.positiveReals());
    }

    public Optional<RayHit> intersect(IRay ray, Interval rayDomain) {
        return RayHit.fromIntersections(this.getIntersections(ray, rayDomain)).map(hit ->  hit.reintersect(ray));
    }

    protected Collection<Intersection> getIntersections(IRay ray, Interval rayDomain) {
        IRay transformedRay = ray.getTransformed(this.inverseTransform);
        return Arrays.stream(this.getLocalIntersections(transformedRay, rayDomain)).collect(Collectors.toList());
    }

    public IPoint transformToOwnSpace(IPoint worldPoint) {
        Optional<Group> parent = this.getParent();
        if (parent.isPresent()) {
            worldPoint = parent.get().transformToOwnSpace(worldPoint);
        }
        return this.getInverseTransform().act(worldPoint);
    }

    protected IVector transformToWorldSpace(IVector ownNormal) {
        IVector transposedNormal = this.getInverseTransform().transpose().act(ownNormal).normalise();
        Optional<Group> parent = this.getParent();
        if (parent.isPresent()) {
            transposedNormal = parent.get().transformToWorldSpace(transposedNormal);
        }
        return transposedNormal;
    }
}

package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.tools.IColour;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Arrays;
import java.util.Optional;

public abstract class Shape {
    private Group parent;
    @Getter private ITransform inverseTransform;
    @Getter private final Material material;
    @Getter @Setter private boolean isCastingShadows;

    public Optional<Group> getParent() {
        return Optional.ofNullable(this.parent);
    }

    protected void setParent(Group group) {
        this.parent = group;
    }

    public void setTransform(ITransform transform) {
        this.inverseTransform = transform.inverse();
    }

    Shape() {
        this.setTransform(new ThreeTransform());
        this.material = Material.builder().build();
        this.isCastingShadows = true;
    }

    Shape(@NonNull Material material) {
        this.setTransform(new ThreeTransform());
        this.material = material;
        this.isCastingShadows = true;
    }

    abstract protected double[] getLocalIntersectionPositions(IRay ray, double tmin, double tmax);

    abstract protected IVector normalLocally(IPoint point);

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Shape themShape = (Shape) them;
        return this.inverseTransform == themShape.inverseTransform && this.material == themShape.material;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[] { this.material.hashCode(), this.inverseTransform.hashCode() });
    }

    public Intersection[] intersect(IRay ray) {
        return this.intersect(ray, 0, Double.POSITIVE_INFINITY);
    }

    public Intersection[] intersect(IRay ray, double tmin, double tmax) {
        IRay transformedRay = ray.getTransformed(this.inverseTransform);
        return Arrays.stream(this.getLocalIntersectionPositions(transformedRay, tmin, tmax)).
            mapToObj(position -> ray.intersect(this, position)).toArray(Intersection[]::new);
    }

    public IVector normal(IPoint point) {
        IPoint transformedPoint = this.transformToOwnSpace(point);
        IVector normal = this.normalLocally(transformedPoint);
        return this.transformToWorldSpace(normal);
    }

    public IColour getIntrinsicColour(IPoint point) {
        IPoint objectPoint = this.transformToOwnSpace(point);
        Material material = this.getMaterial();
        return material.texture.colourAt(objectPoint);
    }

    public IPoint transformToOwnSpace(IPoint worldPoint) {
        Optional<Group> parent = this.getParent();
        if (parent.isPresent()) {
            worldPoint = parent.get().transformToOwnSpace(worldPoint);
        }
        return this.inverseTransform.act(worldPoint);
    }

    protected IVector transformToWorldSpace(IVector ownNormal) {
        IVector transposedNormal = this.inverseTransform.transpose().act(ownNormal).normalise();
        Optional<Group> parent = this.getParent();
        if (parent.isPresent()) {
            transposedNormal = parent.get().transformToWorldSpace(transposedNormal);
        }
        return transposedNormal;
    }
}

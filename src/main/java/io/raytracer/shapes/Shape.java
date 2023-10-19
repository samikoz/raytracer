package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.RayHit;
import io.raytracer.tools.IColour;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Arrays;
import java.util.Optional;

public abstract class Shape {
    private Group parent;
    @Getter protected ITransform transform;
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
        this.transform = transform;
        this.inverseTransform = transform.inverse();
    }

    public Shape() {
        this.transform = new ThreeTransform();
        this.inverseTransform = new ThreeTransform();
        this.material = Material.builder().build();
        this.isCastingShadows = true;
    }

    Shape(@NonNull Material material) {
        this.transform = new ThreeTransform();
        this.inverseTransform = new ThreeTransform();
        this.material = material;
        this.isCastingShadows = true;
    }

    abstract protected Intersection[] getLocalIntersections(IRay ray, double tmin, double tmax);

    abstract protected IVector localNormalAt(IPoint point, double u, double v);

    abstract protected Optional<BBox> getLocalBoundingBox();

    public Optional<BBox> getBoundingBox() {
        return this.getLocalBoundingBox().map(box -> box.transform(this.transform));
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Shape themShape = (Shape) them;
        return this.inverseTransform == themShape.inverseTransform && this.material == themShape.material;
    }

    public boolean doesInclude(Shape them) {
        return this.equals(them);
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
        return Arrays.stream(this.getLocalIntersections(transformedRay, tmin, tmax)).map(ray::reintersect).toArray(Intersection[]::new);
    }

    public IVector normal(Intersection i) {
        IPoint transformedPoint = this.transformToOwnSpace(i.ray.getPosition(i.rayParameter));
        IVector normal = this.localNormalAt(transformedPoint, i.mapping.u, i.mapping.v);
        return this.transformToWorldSpace(normal);
    }

    public IColour getIntrinsicColour(RayHit hit) {
        return this.getIntrinsicColour(hit.point);
    }

    IColour getIntrinsicColour(IPoint point) {
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

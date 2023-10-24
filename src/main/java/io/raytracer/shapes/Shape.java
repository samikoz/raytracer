package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Interval;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.RayHit;
import io.raytracer.tools.IColour;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Collection;

public abstract class Shape extends Hittable {
    @Getter private final Material material;

    public Shape() {
        super();
        this.material = Material.builder().build();
    }

    Shape(@NonNull Material material) {
        super();
        this.material = material;
    }

    abstract protected IVector localNormalAt(IPoint point, double u, double v);

    public Collection<Intersection> getIntersections(IRay ray, Interval rayDomain) {
        return super.getIntersections(ray, rayDomain);
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Shape themShape = (Shape) them;
        return this.getInverseTransform().equals(themShape.getInverseTransform()) && this.material == themShape.material;
    }

    public boolean doesInclude(Shape them) {
        return this.equals(them);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[] { this.material.hashCode(), this.getInverseTransform().hashCode() });
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
}

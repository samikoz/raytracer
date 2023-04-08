package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Arrays;
import java.util.Optional;

public abstract class Shape {
    @Setter private Group parent;
    @Getter private ITransform inverseTransform;
    @Getter private final Material material;

    public Optional<Group> getParent() {
        return Optional.ofNullable(this.parent);
    }

    public void setTransform(ITransform transform) {
        this.inverseTransform = transform.inverse();
    }

    Shape() {
        this.setTransform(new ThreeTransform());
        this.material = Material.builder().build();
    }

    Shape(@NonNull Material material) {
        this.setTransform(new ThreeTransform());
        this.material = material;
    }

    abstract protected double[] getLocalIntersectionPositions(IRay ray);

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
        IRay transformedRay = ray.getTransformed(this.inverseTransform);
        return Arrays.stream(this.getLocalIntersectionPositions(transformedRay)).
                mapToObj(position -> new Intersection(ray, position, this)).toArray(Intersection[]::new);
    }

    public IVector normal(IPoint point) {
        ITransform inverseTransform = this.inverseTransform;
        IPoint transformedPoint = inverseTransform.act(point);
        IVector normal = this.normalLocally(transformedPoint);
        return inverseTransform.transpose().act(normal).normalise();
    }
}

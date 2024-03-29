package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.RayHit;
import io.raytracer.mechanics.TextureParameters;
import io.raytracer.tools.IColour;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;

@Getter
public abstract class Shape extends Hittable {
    private final Material material;

    public Shape() {
        super();
        this.material = Material.builder().build();
    }

    public Shape(@NonNull Material material) {
        super();
        this.material = material;
    }

    abstract protected IVector localNormalAt(IPoint point, TextureParameters p);

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
        IPoint transformedPoint = this.transformToOwnSpace(i.ray.pointAt(i.rayParameter));
        IVector normal = this.localNormalAt(transformedPoint, i.mapping);
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

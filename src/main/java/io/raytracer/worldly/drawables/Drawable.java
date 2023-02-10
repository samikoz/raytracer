package io.raytracer.worldly.drawables;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.worldly.IIntersections;
import io.raytracer.worldly.IRay;
import io.raytracer.worldly.Intersection;
import io.raytracer.worldly.Intersections;
import io.raytracer.worldly.Material;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;

public abstract class Drawable {
    @Getter private ITransform inverseTransform;
    @Getter private final Material material;

    public void setTransform(ITransform transform) {
        this.inverseTransform = transform.inverse();
    }

    Drawable() {
        this.setTransform(new ThreeTransform());
        this.material = Material.builder().build();
    }

    Drawable(@NonNull Material material) {
        this.setTransform(new ThreeTransform());
        this.material = material;
    }

    public IIntersections intersect(IRay ray) {
        IRay transformedRay = ray.transform(this.inverseTransform);
        return new Intersections(Arrays.stream(this.getLocalIntersectionPositions(transformedRay)).
            mapToObj(position -> new Intersection(ray, position, this)).toArray(Intersection[]::new));
    }

    public IVector normal(IPoint point) {
        ITransform inverseTransform = this.inverseTransform;
        IPoint transformedPoint = inverseTransform.act(point);
        IVector normal = this.normalLocally(transformedPoint);
        return inverseTransform.transpose().act(normal).normalise();

    }

    abstract protected double[] getLocalIntersectionPositions(IRay ray);

    abstract protected IVector normalLocally(IPoint point);
}

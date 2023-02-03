package io.raytracer.worldly;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class Sphere implements IDrawable {
    @Getter @Setter private ITransform transform;
    @Getter private final Material material;

    public Sphere() {
        this.transform = new ThreeTransform();
        this.material = Material.builder().build();
    }

    public Sphere(@NonNull Material material) {
        this.transform = new ThreeTransform();
        this.material = material;
    }

    @Override
    public IIntersections intersect(IRay ray) {
        IRay transformedRay = ray.transform(this.transform.inverse());
        IVector rayOrigin = transformedRay.getOrigin().subtract(new Point(0, 0, 0));
        double a = transformedRay.getDirection().dot(transformedRay.getDirection());
        double b = 2 * transformedRay.getDirection().dot(rayOrigin);
        double c = rayOrigin.dot(rayOrigin) - 1;
        double delta = Math.pow(b, 2) - 4 * a * c;

        if (delta < 0) {
            return new Intersections();
        }
        return new Intersections(
                new Intersection(ray, (-b - Math.sqrt(delta)) / (2 * a), this),
                new Intersection(ray, (-b + Math.sqrt(delta)) / (2 * a), this));
    }

    @Override
    public IVector normal(@NonNull IPoint p) {
        ITransform inverseTransform = this.transform.inverse();
        IPoint spherePoint = inverseTransform.act(p);
        IVector sphereNormal = (spherePoint.subtract(new Point(0, 0, 0))).normalise();
        return inverseTransform.transpose().act(sphereNormal).normalise();
    }
}

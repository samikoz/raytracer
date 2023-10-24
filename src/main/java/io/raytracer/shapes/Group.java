package io.raytracer.shapes;

import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.RayHit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Group extends Hittable {
    public final List<Hittable> children;
    private BBox bbox;

    public Group() {
        super();
        this.children = new ArrayList<>();
        this.bbox = new BBox();
    }

    public Group add(Hittable object) {
        this.children.add(object);
        object.setParent(this);
        object.getBoundingBox().ifPresent(box -> this.bbox = new BBox(this.bbox, box));
        return this;
    }

    @Override
    public Optional<RayHit> intersect(IRay ray, double tmin, double tmax) {
        IRay transformedRay = ray.getTransformed(this.getInverseTransform());
        return this.children.stream().map(child -> child.intersect(transformedRay, tmin, tmax))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(hit -> hit.reintersect(ray)).min(Comparator.comparingDouble(hit -> hit.rayParameter));
    }

    @Override
    public Collection<Intersection> getIntersections(IRay ray, double tmin, double tmax) {
        IRay transformedRay = ray.getTransformed(this.getInverseTransform());
        return this.children.stream().map(child -> child.getIntersections(transformedRay, tmin, tmax))
                .flatMap(Collection::stream)
                .map(i -> new Intersection(i.object, transformedRay, i.rayParameter, i.mapping))
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, double tmin, double tmax) {
        throw new UnsupportedOperationException("a group doesn't have own local intersections");
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Group themGroup = (Group) them;
        return super.equals(them) && this.children.equals(themGroup.children);
    }

    @Override
    public int hashCode() {
        int childrenHash = Arrays.hashCode(this.children.stream().mapToInt(o -> o.hashCode()).toArray());
        return Arrays.hashCode(new int[] { childrenHash, super.hashCode() });
    }

    @Override
    protected Optional<BBox> getLocalBoundingBox() {
        return Optional.of(this.bbox);
    }
}

package io.raytracer.shapes;

import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.Interval;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.RayHit;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public class Group extends Hittable {
    private final Hittable left;
    private final Hittable right;
    private final BBox bbox;
    private static final Random randGen = new Random();
    private final List<Hittable> children;

    public Group(Hittable[] objects) {
        this(objects, 0, objects.length);
    }

    private Group(Hittable[] objects, int startIndex, int endIndex) {
        super();
        this.children = Collections.unmodifiableList(Arrays.asList(objects));
        int axis = Group.randGen.nextInt(3);
        Comparator<Hittable> comparator = this.compareBox(axis);
        int spanSize = endIndex - startIndex;

        if (spanSize == 1) {
            this.left = objects[startIndex];
            this.right = this.left;
        }
        else if (spanSize == 2) {
            if (comparator.compare(objects[startIndex], objects[startIndex+1]) < 0) {
                this.left = objects[startIndex];
                this.right = objects[startIndex+1];
            } else {
                this.left = objects[startIndex+1];
                this.right = objects[startIndex];
            }
        }
        else {
            Arrays.sort(objects, startIndex, endIndex, comparator);
            int midIndex = startIndex + spanSize/2;
            this.left =  new Group(objects, startIndex, midIndex);
            this.right = new Group(objects, midIndex, endIndex);
        }
        this.bbox = new BBox(this.left.getBoundingBox(), this.right.getBoundingBox());
    }

    @Override
    public void setTransform(ITransform transform) {
        super.setTransform(transform);
        this.children.forEach(child -> child.setParent(this));
    }
    
    public List<Hittable> getChildren() {
        return this.children;
    }

    public Optional<RayHit> intersect(IRay ray, Interval rayDomain) {
        IRay transformedRay = ray.getTransformed(this.getInverseTransform());
        if (!this.bbox.isHit(transformedRay, rayDomain)) {
            return Optional.empty();
        }
        Optional<RayHit> leftHit = this.left.intersect(transformedRay, rayDomain);
        Interval rightHitDomain = new Interval(rayDomain.min, leftHit.map(hit -> hit.rayParameter).orElse(rayDomain.max));
        Optional<RayHit> rightHit = this.right.intersect(transformedRay, rightHitDomain);
        return Stream.of(leftHit, rightHit).filter(Optional::isPresent).map(Optional::get).min(Comparator.comparingDouble(hit -> hit.rayParameter));
    }

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, Interval rayDomain) {
        throw new UnsupportedOperationException("a group doesn't have own local intersections");
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Group themGroup = (Group) them;
        return super.equals(them) && this.left.equals(themGroup.left) && this.right.equals(themGroup.right);
    }

    @Override
    public int hashCode() {
        int childrenHash = Arrays.hashCode(new int[] {this.left.hashCode(), this.right.hashCode()});
        return Arrays.hashCode(new int[] { childrenHash, super.hashCode() });
    }

    @Override
    protected BBox getLocalBoundingBox() {
        return this.bbox;
    }

    private Comparator<Hittable> compareBox(int axis) {
        return Comparator.comparingDouble(hitt -> hitt.getBoundingBox().axis(axis).min);
    }
}

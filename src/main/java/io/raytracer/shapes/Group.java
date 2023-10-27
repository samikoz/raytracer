package io.raytracer.shapes;

import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.Interval;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.RayHit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

public class Group extends Hittable {
    private final Hittable left;
    private final Hittable right;
    private final BBox bbox;
    private static final Random randGen = new Random();
    private final List<Hittable> children;

    public Group(Hittable[] objects) {
        this(objects, 0, objects.length, () -> Group.randGen.nextInt(3));
    }

    private Group(Hittable[] objects, int startIndex, int endIndex, Supplier<Integer> axisSupplier) {
        super();
        this.children = Collections.unmodifiableList(Arrays.asList(objects));
        int axis = axisSupplier.get();
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
            this.left =  new Group(objects, startIndex, midIndex, axisSupplier);
            this.right = new Group(objects, midIndex, endIndex, axisSupplier);
        }
        this.bbox = new BBox(this.left.getBoundingBox(), this.right.getBoundingBox());
    }

    Group(Hittable[] objects, Supplier<Integer> axisSupplier) {
        this(objects, 0, objects.length, axisSupplier);
    }

    @Override
    public void setTransform(ITransform transform) {
        super.setTransform(transform);
        this.children.forEach(child -> child.setParent(this));
    }
    
    public List<Hittable> getChildren() {
        return this.children;
    }

    public Intersection[] intersect(IRay ray, Interval rayDomain) {
        IRay transformedRay = ray.getTransformed(this.getInverseTransform());
        if (!this.bbox.isHit(transformedRay, rayDomain)) {
            return new Intersection[] {};
        }
        Optional<RayHit> leftHit = RayHit.fromIntersections(this.left.intersect(transformedRay, rayDomain));
        Interval rightHitDomain = new Interval(rayDomain.min, leftHit.map(hit -> hit.rayParameter).orElse(rayDomain.max));
        Intersection[] rightIntersections = this.right.intersect(transformedRay, rightHitDomain);
        List<Intersection> rightIntersList = new ArrayList<>(Arrays.asList(rightIntersections));
        if (leftHit.isPresent()) {
            Intersection hitIntersection = leftHit.get();
            Optional<Intersection> firstFartherIntersection = rightIntersList.stream().filter(i -> i.rayParameter > hitIntersection.rayParameter).findFirst();
            if (firstFartherIntersection.isPresent()) {
                int fartherIntersectionIndex = rightIntersList.indexOf(firstFartherIntersection.get());
                rightIntersList.add(fartherIntersectionIndex, hitIntersection);
            }
            else {
                rightIntersList.add(hitIntersection);
            }
        }
        return rightIntersList.stream().map(i -> i.reintersect(ray)).toArray(Intersection[]::new);
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

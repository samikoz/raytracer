package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Interval;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.TextureParameters;
import io.raytracer.shapes.operators.Difference;
import lombok.Builder;

import java.util.Optional;


public class Corner extends Shape {
    private final Cube outerCube;
    private final Cube cutoffCube;
    private final Shape itself;
    private final double offset = 1e-2;
    private final CubeCorner corner;

    @Builder
    public Corner(Material material, CubeCorner corner) {
        this.corner = Optional.ofNullable(corner).orElse(CubeCorner.FIRST);
        this.outerCube = Optional.ofNullable(material).map(m -> new Cube(material)).orElse(new Cube());
        this.cutoffCube = Optional.ofNullable(material).map(m -> new Cube(material)).orElse(new Cube());
        this.setupCutoffCube();
        this.itself = new Difference(this.outerCube, this.cutoffCube);
    }

    private void setupCutoffCube() {
        this.cutoffCube.setTransform(ThreeTransform.scaling(2).translate(
                (1+this.offset)*this.corner.x(), (1+this.offset)*this.corner.y(), (1+this.offset)*this.corner.z())
        );
    }

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, Interval rayDomain) {
        return this.itself.getLocalIntersections(ray, rayDomain);
    }

    @Override
    protected IVector localNormalAt(IPoint point, TextureParameters p) {
        return this.itself.localNormalAt(point, p);
    }

    @Override
    protected BBox getLocalBoundingBox() {
        return this.outerCube.getLocalBoundingBox();
    }
}
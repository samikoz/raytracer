package io.raytracer.tools;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.materials.Material;
import io.raytracer.shapes.Cube;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class CurveToCubesMapper {
    private final IPoint middle;
    private final double scale;
    private final Supplier<Cube> cubeMaker;

    public CurveToCubesMapper(IPoint middle, double scale, Material material) {
        this.middle = middle;
        this.scale = scale;
        this.cubeMaker = () -> new Cube(material);
    }

    public CurveToCubesMapper(IPoint middle, double scale) {
        this.middle = middle;
        this.scale = scale;
        this.cubeMaker = Cube::new;
    }

    public List<Cube> mapCurve(List<CurvepointData> curve) {
        return curve.stream().map(curvepoint -> {
            Cube aCube = this.cubeMaker.get();
            aCube.setTransform(this.mapCurveData(curvepoint));
            return aCube;
        }).collect(Collectors.toList());
    }

    private ThreeTransform mapCurveData(CurvepointData curvepointData) {
        IVector tv = this.getTranslationVector(curvepointData.point);
        return this.getScaledRotation(curvepointData.vector).translate(tv.get(0), tv.get(1), tv.get(2));
    }

    private IVector getTranslationVector(IPoint point) {
        return point.subtract(this.middle).multiply(this.scale);
    }

    private ThreeTransform getScaledRotation(IVector normal) {
        return ThreeTransform.rotation_z(Math.atan2(normal.get(1), normal.get(0)));
    }
}

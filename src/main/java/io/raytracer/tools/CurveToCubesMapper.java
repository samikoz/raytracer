package io.raytracer.tools;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.materials.Material;
import io.raytracer.shapes.Axis;
import io.raytracer.shapes.Cube;

import java.util.function.Function;
import java.util.function.Supplier;


public class CurveToCubesMapper {
    private final IPoint middle;
    private final double scale;
    private final ICurve curve;
    private final Supplier<Cube> cubeMaker;
    private final Function<Double, ThreeTransform> rotation;

    public CurveToCubesMapper(IPoint middle, double scale, ICurve curve, Axis axis, Material material) {
        this.middle = middle;
        this.scale = scale;
        this.curve = curve;
        this.cubeMaker = () -> new Cube(material);
        this.rotation = this.chooseRotation(axis);
    }

    public CurveToCubesMapper(IPoint middle, double scale, ICurve curve, Axis axis) {
        this.middle = middle;
        this.scale = scale;
        this.curve = curve;
        this.cubeMaker = Cube::new;
        this.rotation = this.chooseRotation(axis);
    }

    private Function<Double, ThreeTransform> chooseRotation(Axis axis) {
        //Axis points along the domain of the curve
        if (axis == Axis.X) {
            return ThreeTransform::rotation_z;
        }
        if (axis == Axis.Z){
            return ThreeTransform::rotation_x;
        }
        return null;
    }

    public Cube map(double t) {
        IPoint point = this.curve.pointAt(t);
        IVector normal = this.curve.normalAt(t);
        Cube aCube = this.cubeMaker.get();
        aCube.setTransform(this.mapCurveData(point, normal));
        return aCube;
    }

    private ThreeTransform mapCurveData(IPoint point, IVector normal) {
        IVector tv = this.getTranslationVector(point);
        return this.getScaledRotation(normal).translate(tv.x(), tv.y(), tv.z());
    }

    private IVector getTranslationVector(IPoint point) {
        return point.subtract(this.middle).multiply(this.scale);
    }

    private ThreeTransform getScaledRotation(IVector normal) {
        return this.rotation.apply(Math.PI/2 - Math.atan2(normal.y(), normal.x()));
    }
}

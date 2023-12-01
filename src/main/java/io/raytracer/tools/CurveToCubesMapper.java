package io.raytracer.tools;

import io.raytracer.geometry.IPoint;
import io.raytracer.algebra.ITransform;
import io.raytracer.geometry.ITuple;
import io.raytracer.geometry.IVector;
import io.raytracer.algebra.ThreeTransform;
import io.raytracer.geometry.Vector;
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

    public CurveToCubesMapper(IPoint middle, double scale, ICurve curve, Axis axis, Supplier<Cube> maker) {
        this.middle = middle;
        this.scale = scale;
        this.curve = curve;
        this.cubeMaker = maker;
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
        ITransform initialTransform = aCube.getTransform();
        aCube.setTransform(initialTransform.transform(this.mapCurveData(point, normal)));
        return aCube;
    }

    public ThreeTransform getTranslation(double t) {
        IPoint curvePoint = this.curve.pointAt(t);
        return ThreeTransform.translation(curvePoint.x(), curvePoint.y(), curvePoint.z());
    }

    public ThreeTransform getRotation(double t) {
        IVector curveNormal = this.curve.normalAt(t);
        return this.rotation.apply(-Math.PI/2 + Math.atan2(curveNormal.y(), curveNormal.x()));
    }

    private ThreeTransform mapCurveData(IPoint point, IVector normal) {
        ITuple tv = this.getTranslationPoint(point);
        return this.getNormalRotation(normal).translate(tv.x(), tv.y(), tv.z());
    }

    private IPoint getTranslationPoint(IPoint point) {
        return point.add(new Vector(this.middle)).multiply(this.scale);
    }

    private ThreeTransform getNormalRotation(IVector normal) {
        return this.rotation.apply(-Math.PI/2 + Math.atan2(normal.y(), normal.x()));
    }
}

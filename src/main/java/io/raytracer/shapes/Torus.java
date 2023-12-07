package io.raytracer.shapes;

import io.raytracer.algebra.Equation;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Interval;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.TextureParameters;

public class Torus extends Shape {
    private final double major;
    private final double minor;

    public Torus(double majorRadius, double minorRadius) {
        this.major = majorRadius;
        this.minor = minorRadius;
    }

    public Torus(double majorRadius, double minorRadius, Material material) {
        super(material);
        this.major = majorRadius;
        this.minor = minorRadius;
    }

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, Interval rayDomain) {
        IVector d = ray.getDirection();
        IPoint o = ray.getOrigin();
        double R2 = Math.pow(this.major, 2);
        double R2r2 = R2 - Math.pow(this.minor, 2);
        double a = Math.pow(d.x(),2) + Math.pow(d.y(),2) + Math.pow(d.z(),2);
        double b = d.x()*o.x() + d.y()*o.y() + d.z()*o.z();
        double c = Math.pow(o.x(),2) + Math.pow(o.y(),2) + Math.pow(o.z(),2);
        Equation eqn = new Equation(
            Math.pow(c,2) + 2*c*R2r2 - 4*R2*(c - Math.pow(o.z(),2)) + Math.pow(R2r2, 2),
            4*(b*c + b*R2r2 - 2*R2*(b - d.z()*o.z())),
            2*(a*c + 2*Math.pow(b,2) + a*R2r2 - 2*R2*(a - Math.pow(d.z(),2))),
            4*a*b,
            Math.pow(a, 2)
        );
        double[] solutions = eqn.solve();
        Intersection[] intersections = new Intersection[solutions.length];
        for (int i = 0; i < solutions.length; i++) {
            intersections[i] = new Intersection(this, ray, solutions[i], new TextureParameters());
        }
        return intersections;
    }

    @Override
    protected IVector localNormalAt(IPoint point, TextureParameters p) {
        double x = point.x(); double y = point.y(); double z = point.z();
        double xySqRoot = Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
        return new Vector(x, y, z*(1 + (this.major/(xySqRoot - this.major))));
    }

    @Override
    protected BBox getLocalBoundingBox() {
        return new BBox(
            new Point(-this.major - this.minor, -this.major - this.minor, -this.minor),
            new Point(this.major + this.minor, this.major + this.minor, this.minor));
    }
}

package io.raytracer.tools;

import io.raytracer.geometry.IPlane;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Line;
import io.raytracer.geometry.Point;
import io.raytracer.shapes.Plane;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class Pixel implements Serializable {
    public int x;
    public int y;

    public Pixel(double x, double y) {
        this.x = Math.round((float)x);
        this.y = Math.round((float)y);
    }

    public Pixel interpolate(Pixel p, double t) {
        IPoint from = new Point(this.x, this.y, 0);
        IPoint to = new Point(p.x, p.y, 0);
        IPoint interpolated = new Line(from, to).pointAt(t);
        return new Pixel(Math.round((float)interpolated.x()), Math.round((float)interpolated.y()));
    }

    public IPoint materialise(Camera cam, double distance) {
        return this.materialise(cam, new Plane(cam.getLookDirection(), cam.getEyePosition().add(cam.getLookDirection().multiply(distance))));
    }

    public IPoint materialise(Camera cam, IPlane plane) {
        return plane.intersect(cam.getRayThrough(this)).get();
    }
}

package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.Interval;
import io.raytracer.geometry.Point;
import lombok.RequiredArgsConstructor;

import java.util.stream.IntStream;
import java.util.stream.Stream;


@RequiredArgsConstructor
public class BBox {
    public final Interval x, y, z;
    public static final double paddingMargin = 1e-3;

    public BBox() {
        this.x = new Interval();
        this.y = new Interval();
        this.z = new Interval();
    }

    public BBox(IPoint corner1, IPoint corner2) {
        this.x = new Interval(Math.min(corner1.get(0), corner2.get(0)), Math.max(corner1.get(0), corner2.get(0)));
        this.y = new Interval(Math.min(corner1.get(1), corner2.get(1)), Math.max(corner1.get(1), corner2.get(1)));
        this.z = new Interval(Math.min(corner1.get(2), corner2.get(2)), Math.max(corner1.get(2), corner2.get(2)));
    }

    public BBox(BBox b1, BBox b2) {
        this.x = new Interval(b1.x, b2.x);
        this.y = new Interval(b1.y, b2.y);
        this.z = new Interval(b1.z, b2.z);
    }

    public BBox transform(ITransform t) {
        double[] x = this.x.toArray();
        double[] y = this.y.toArray();
        double[] z = this.z.toArray();
        Stream<IPoint> mapped = IntStream.range(0, 8).mapToObj(n -> new Point(x[n & 1], y[(n & 2) >> 1], z[(n & 4) >> 2])).map(t::act);
        Interval xex = new Interval(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        Interval yex = new Interval(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        Interval zex = new Interval(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        mapped.forEach(p -> {
            xex.min = Math.min(p.get(0), xex.min);
            xex.max = Math.max(p.get(0), xex.max);
            yex.min = Math.min(p.get(1), yex.min);
            yex.max = Math.max(p.get(1), yex.max);
            zex.min = Math.min(p.get(2), zex.min);
            zex.max = Math.max(p.get(2), zex.max);
        });
        return new BBox(xex, yex, zex);
    }

    public boolean isHit(IRay ray, Interval range) {
        for (int a = 0; a < 3; a++) {
            double invD = 1 / ray.getDirection().get(a);
            double orig = ray.getOrigin().get(a);
            double t0 = (this.axis(a).min - orig)*invD;
            double t1 = (this.axis(a).max - orig)*invD;

            if (invD < 0) {
                double swp = t0;
                t0 = t1;
                t1 = swp;
            }

            if (t0 > range.min) {
                range.min = t0;
            }
            if (t1 < range.max) {
                range.max = t1;
            }
            if (range.max <= range.min) {
                return false;
            }
        }
        return true;
    }

    public Interval axis(int n) {
        if (n == 1) {
            return y;
        }
        if (n == 2) {
            return z;
        }
        return x;
    }
}

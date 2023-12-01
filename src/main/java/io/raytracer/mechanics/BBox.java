package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.algebra.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Interval;
import io.raytracer.geometry.Point;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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
        this.x = new Interval(Math.min(corner1.x(), corner2.x()), Math.max(corner1.x(), corner2.x()));
        this.y = new Interval(Math.min(corner1.y(), corner2.y()), Math.max(corner1.y(), corner2.y()));
        this.z = new Interval(Math.min(corner1.z(), corner2.z()), Math.max(corner1.z(), corner2.z()));
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
        Collection<IPoint> mapped = IntStream.range(0, 8).mapToObj(n -> new Point(x[n & 1], y[(n & 2) >> 1], z[(n & 4) >> 2])).map(t::act).collect(Collectors.toList());
        double xexMin = Double.POSITIVE_INFINITY;
        double xexMax = Double.NEGATIVE_INFINITY;
        double yexMin = Double.POSITIVE_INFINITY;
        double yexMax = Double.NEGATIVE_INFINITY;
        double zexMin = Double.POSITIVE_INFINITY;
        double zexMax = Double.NEGATIVE_INFINITY;
        for (IPoint p : mapped) {
            xexMin = Math.min(p.x(), xexMin);
            xexMax = Math.max(p.x(), xexMax);
            yexMin = Math.min(p.y(), yexMin);
            yexMax = Math.max(p.y(), yexMax);
            zexMin = Math.min(p.z(), zexMin);
            zexMax = Math.max(p.z(), zexMax);
        }
        return new BBox(new Interval(xexMin, xexMax), new Interval(yexMin, yexMax), new Interval(zexMin, zexMax));
    }

    public boolean isHit(IRay ray, Interval range) {
        double min = range.min;
        double max = range.max;
        IVector rayDir = ray.getDirection();
        IPoint rayOrg = ray.getOrigin();
        int axisIndex = 0;
        for (Pair<Double, Double> dirOrg : new Pair[] { new Pair<>(rayDir.x(), rayOrg.x()), new Pair<>(rayDir.y(), rayOrg.y()), new Pair<>(rayDir.z(), rayOrg.z()) }) {
            double invD = 1 / dirOrg.getValue0();
            double orig = dirOrg.getValue1();
            double t0 = (this.axis(axisIndex).min - orig)*invD;
            double t1 = (this.axis(axisIndex).max - orig)*invD;

            if (invD < 0) {
                double swp = t0;
                t0 = t1;
                t1 = swp;
            }

            if (t0 > min) {
                min = t0;
            }
            if (t1 < max) {
                max = t1;
            }
            if (max <= min) {
                return false;
            }
            axisIndex++;
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

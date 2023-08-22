package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Interval;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class BBox {
    private final Interval x, y, z;

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

    public Interval axis(int n) {
        if (n == 1) {
            return y;
        }
        if (n == 2) {
            return z;
        }
        return x;
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
}

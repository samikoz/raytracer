package io.raytracer.worldly;

import io.raytracer.worldly.drawables.Drawable;
import lombok.NonNull;
import lombok.ToString;

import java.util.Arrays;


@ToString
public class Hit extends Intersection {
    private Hit(IRay ray, double rayParameter, @NonNull Drawable object) {
        super(ray, rayParameter, object);
    }

    static Hit fromIntersection(Intersection inter) {
        return new Hit(inter.ray, inter.rayParameter, inter.object);
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Hit themHit = (Hit) them;
        return (this.ray == themHit.ray && (int)this.rayParameter*1000 == (int)themHit.rayParameter*1000);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[] { this.ray.hashCode(), (int)this.rayParameter*1000 });
    }
}

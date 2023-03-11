package io.raytracer.worldly;

import io.raytracer.worldly.drawables.Drawable;
import lombok.NonNull;
import lombok.ToString;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;


@ToString
public class Hit extends Intersection {
    public final double refractiveFrom;
    public final double refractiveTo;
    private Hit(IRay ray, double rayParameter, @NonNull Drawable object, double refractiveFrom, double refractiveTo) {
        super(ray, rayParameter, object);
        this.refractiveFrom = refractiveFrom;
        this.refractiveTo = refractiveTo;
    }

    static Optional<Hit> fromIntersections(Intersection[] inters) {
        Optional<Intersection> firstPositive = Arrays.stream(inters).filter(i -> i.rayParameter > 0).min(Comparator.comparingDouble(i -> i.rayParameter));
        return firstPositive.map(inter -> new Hit(inter.ray, inter.rayParameter, inter.object, 1, 1));
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

package io.raytracer.mechanics;

import io.raytracer.shapes.Shape;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@ToString
@AllArgsConstructor
public class Intersection implements Comparable<Intersection> {
    @NonNull public Shape object;
    public IRay ray;
    public double rayParameter;
    public TextureParameters mapping;

    private static final double equalityTolerance = 1e-3;

    @Override
    public int compareTo(@NotNull Intersection intersection) {
        return Double.compare(this.rayParameter, intersection.rayParameter);
    }

    public Intersection reintersect(IRay ray) {
        return new Intersection(this.object, ray, this.rayParameter, this.mapping);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new double[] {this.object.hashCode(), this.ray.hashCode(), this.rayParameter});
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Intersection themIntersection = (Intersection) them;
        return this.object.equals(themIntersection.object) && this.ray.equals(themIntersection.ray) && Math.abs(this.rayParameter - themIntersection.rayParameter) < Intersection.equalityTolerance;
    }
}

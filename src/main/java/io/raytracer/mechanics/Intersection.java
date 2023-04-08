package io.raytracer.mechanics;

import io.raytracer.shapes.Shape;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

@ToString
@AllArgsConstructor
public class Intersection implements Comparable<Intersection> {
    public IRay ray;
    public double rayParameter;
    @NonNull public Shape object;

    @Override
    public int compareTo(@NotNull Intersection intersection) {
        return Double.compare(this.rayParameter, intersection.rayParameter);
    }
}

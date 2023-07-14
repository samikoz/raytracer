package io.raytracer.materials;

import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.RayHit;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.function.Function;

@RequiredArgsConstructor
public class RecasterContribution {
    public final Function<RayHit, Collection<IRay>> recaster;
    public final double contribution;
}

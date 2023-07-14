package io.raytracer.materials;

import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.RayHit;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class RecasterContribution {
    public final Function<RayHit, Optional<IRay>> recaster;
    public final double contribution;
}

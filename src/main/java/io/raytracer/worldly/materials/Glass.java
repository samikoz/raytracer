package io.raytracer.worldly.materials;

import io.raytracer.drawing.patterns.Pattern;
import lombok.Builder;

import java.util.Optional;

public class Glass extends Material {
    public static final double defaultTransparency = 1;
    public static final double defaultRefractiveIndex = 1.5;

    @Builder(builderMethodName = "glassBuilder")
    protected Glass(Pattern pattern, Double ambient, Double diffuse, Double specular, Double shininess,
                       Double reflectivity, Double transparency, Double refractiveIndex) {
        super(
            pattern, ambient, diffuse, specular, shininess, reflectivity,
            Optional.ofNullable(transparency).orElse(Glass.defaultTransparency),
            Optional.ofNullable(refractiveIndex).orElse(Glass.defaultRefractiveIndex)
        );
    }
}

package io.raytracer.materials;

import io.raytracer.textures.Texture;
import lombok.Builder;

import java.util.Optional;

public class Glass extends Material {
    public static final double defaultReflectivity = 0.9;
    public static final double defaultTransparency = 1;
    public static final double defaultRefractiveIndex = 1.5;

    @Builder(builderMethodName = "glassBuilder")
    protected Glass(Texture texture, Double ambient, Double diffuse, Double specular, Double shininess,
                    Double reflectivity, Double transparency, Double refractiveIndex) {
        super(
                texture, ambient, diffuse, specular, shininess,
            Optional.ofNullable(reflectivity).orElse(Glass.defaultReflectivity),
            Optional.ofNullable(transparency).orElse(Glass.defaultTransparency),
            Optional.ofNullable(refractiveIndex).orElse(Glass.defaultRefractiveIndex)
        );
    }
}

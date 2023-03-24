package io.raytracer.materials;

import io.raytracer.patterns.Monopattern;
import io.raytracer.patterns.Pattern;
import io.raytracer.tools.Colour;
import lombok.Builder;

import java.util.Arrays;
import java.util.Optional;

public class Material {
    public final Pattern pattern;
    private static final Pattern defaultPattern = new Monopattern(new Colour(0, 0, 0));
    public final double ambient;
    public final double diffuse;
    public final double specular;
    public final double shininess;
    public final double reflectivity;
    public final double transparency;
    public final double refractiveIndex;
    private static final double defaultRefractiveIndex = 1;
    private static final double equalityTolerance = 1e-3;

    @Builder(toBuilder = true)
    protected Material(Pattern pattern, Double ambient, Double diffuse, Double specular, Double shininess,
                       Double reflectivity, Double transparency, Double refractiveIndex) {
        this.pattern = Optional.ofNullable(pattern).orElse(Material.defaultPattern);
        this.ambient = Optional.ofNullable(ambient).orElse(0.0);
        this.diffuse = Optional.ofNullable(diffuse).orElse(0.0);
        this.specular = Optional.ofNullable(specular).orElse(0.0);
        this.shininess = Optional.ofNullable(shininess).orElse(0.0);
        this.reflectivity = Optional.ofNullable(reflectivity).orElse(0.0);
        this.transparency = Optional.ofNullable(transparency).orElse(0.0);
        this.refractiveIndex = Optional.ofNullable(refractiveIndex).orElse(Material.defaultRefractiveIndex);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[] {
                this.pattern.getHashCode(),
                Arrays.hashCode(new double[] {this.ambient, this.diffuse, this.specular, this.shininess})});
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Material themMaterial = (Material) them;
        return (this.pattern.equals(themMaterial.pattern)
            && Math.abs(this.ambient - themMaterial.ambient) < equalityTolerance
            && Math.abs(this.diffuse - themMaterial.diffuse) < equalityTolerance
            && Math.abs(this.specular - themMaterial.specular) < equalityTolerance
            && Math.abs(this.shininess - themMaterial.shininess) < equalityTolerance);
    }
}

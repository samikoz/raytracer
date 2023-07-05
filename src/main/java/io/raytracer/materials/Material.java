package io.raytracer.materials;

import io.raytracer.textures.MonocolourTexture;
import io.raytracer.textures.Texture;
import io.raytracer.tools.GammaColour;
import io.raytracer.tools.IColour;
import io.raytracer.tools.LinearColour;
import lombok.Builder;

import java.util.Arrays;
import java.util.Optional;

public class Material {
    public final Texture texture;
    private static final Texture DEFAULT_TEXTURE = new MonocolourTexture(new LinearColour(0, 0, 0));
    public final double ambient;
    public final double diffuse;
    public final double specular;
    public final double shininess;
    public final double reflectivity;
    public final double transparency;
    public final double refractiveIndex;
    public final IColour emit;
    private static final IColour defaultEmit = new GammaColour(0, 0, 0);
    private static final double defaultRefractiveIndex = 1;
    private static final double equalityTolerance = 1e-3;

    @Builder(toBuilder = true)
    protected Material(Texture texture, Double ambient, Double diffuse, Double specular, Double shininess,
                       Double reflectivity, Double transparency, Double refractiveIndex, IColour emit) {
        this.texture = Optional.ofNullable(texture).orElse(Material.DEFAULT_TEXTURE);
        this.ambient = Optional.ofNullable(ambient).orElse(0.0);
        this.diffuse = Optional.ofNullable(diffuse).orElse(0.0);
        this.specular = Optional.ofNullable(specular).orElse(0.0);
        this.shininess = Optional.ofNullable(shininess).orElse(0.0);
        this.reflectivity = Optional.ofNullable(reflectivity).orElse(0.0);
        this.transparency = Optional.ofNullable(transparency).orElse(0.0);
        this.refractiveIndex = Optional.ofNullable(refractiveIndex).orElse(Material.defaultRefractiveIndex);
        this.emit = Optional.ofNullable(emit).orElse(Material.defaultEmit);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[] {
            this.texture.getHashCode(),
            Arrays.hashCode(new double[] {
                this.ambient, this.diffuse, this.specular, this.shininess, this.reflectivity, this.transparency, this.refractiveIndex
            })
        });
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Material themMaterial = (Material) them;
        return (this.texture.equals(themMaterial.texture)
            && Math.abs(this.ambient - themMaterial.ambient) < equalityTolerance
            && Math.abs(this.diffuse - themMaterial.diffuse) < equalityTolerance
            && Math.abs(this.specular - themMaterial.specular) < equalityTolerance
            && Math.abs(this.shininess - themMaterial.shininess) < equalityTolerance);
    }
}

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
    private static final Texture default_texture = new MonocolourTexture(new LinearColour(0, 0, 0));
    public final double refractiveIndex;
    public final IColour emit;
    private static final IColour defaultEmit = new GammaColour(0, 0, 0);
    private static final double defaultRefractiveIndex = 1;
    private static final double equalityTolerance = 1e-3;

    @Builder(toBuilder = true)
    protected Material(Texture texture, Double refractiveIndex, IColour emit) {
        this.texture = Optional.ofNullable(texture).orElse(Material.default_texture);
        this.refractiveIndex = Optional.ofNullable(refractiveIndex).orElse(Material.defaultRefractiveIndex);
        this.emit = Optional.ofNullable(emit).orElse(Material.defaultEmit);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[] { this.texture.hashCode(), Double.hashCode(this.refractiveIndex), this.emit.hashCode() });
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Material themMaterial = (Material) them;
        return (this.texture.equals(themMaterial.texture)
            && Math.abs(this.refractiveIndex - themMaterial.refractiveIndex) < equalityTolerance
            && this.emit.equals(themMaterial.emit));
    }
}

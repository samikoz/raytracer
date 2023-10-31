package io.raytracer.materials;

import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.RayHit;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.textures.Texture;
import io.raytracer.tools.IColour;
import io.raytracer.tools.LinearColour;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Material {
    public final Texture texture;
    private static final Texture default_texture = new MonocolourTexture(new LinearColour(0, 0, 0));
    public final double refractiveIndex;
    public final IColour emit;
    @Getter private final List<RecasterContribution> recasterContributions;
    private static final IColour defaultEmit = new LinearColour(0, 0, 0);
    private static final double defaultRefractiveIndex = 1;
    private static final double equalityTolerance = 1e-3;

    @Builder(toBuilder = true)
    protected Material(Texture texture, Double refractiveIndex, IColour emit) {
        this.texture = Optional.ofNullable(texture).orElse(Material.default_texture);
        this.refractiveIndex = Optional.ofNullable(refractiveIndex).orElse(Material.defaultRefractiveIndex);
        this.emit = Optional.ofNullable(emit).orElse(Material.defaultEmit);
        this.recasterContributions = new ArrayList<>();
    }

    public Material addRecaster(Function<RayHit, Optional<IRay>> recaster, double contribution) {
        this.recasterContributions.add(new RecasterContribution(recaster, contribution));
        return this;
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

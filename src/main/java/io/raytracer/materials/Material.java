package io.raytracer.materials;

import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.RayHit;
import io.raytracer.mechanics.Recasters;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.textures.Texture;
import io.raytracer.tools.IColour;
import io.raytracer.tools.LinearColour;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;

public class Material {
    public final Texture texture;
    private static final Texture default_texture = new MonocolourTexture(new LinearColour(0, 0, 0));
    public final double refractiveIndex;
    public final IColour emit;
    @Getter
    private final List<RecasterContribution> recasterContributions;
    private static final IColour defaultEmit = new LinearColour(0, 0, 0);
    private static final double defaultRefractiveIndex = 1;
    private static final double equalityTolerance = 1e-3;

    protected Material(Texture texture, Double refractiveIndex, IColour emit, List<RecasterContribution> recasters) {
        this.texture = Optional.ofNullable(texture).orElse(Material.default_texture);
        this.refractiveIndex = Optional.ofNullable(refractiveIndex).orElse(Material.defaultRefractiveIndex);
        this.emit = Optional.ofNullable(emit).orElse(Material.defaultEmit);
        this.recasterContributions = recasters;
    }

    public static MaterialBuilder builder() {
        return new MaterialBuilder();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[]{this.texture.hashCode(), Double.hashCode(this.refractiveIndex), this.emit.hashCode()});
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Material themMaterial = (Material) them;
        return (this.texture.equals(themMaterial.texture)
                && Math.abs(this.refractiveIndex - themMaterial.refractiveIndex) < equalityTolerance
                && this.emit.equals(themMaterial.emit));
    }

    public MaterialBuilder toBuilder() {
        return new MaterialBuilder().texture(this.texture).refractiveIndex(this.refractiveIndex).emit(this.emit);
    }

    public static class MaterialBuilder {
        private Texture texture;
        private Double refractiveIndex;
        private IColour emit;

        private List<RecasterContribution> recasters;

        MaterialBuilder() {
            this.recasters = new ArrayList<>();
        }

        public MaterialBuilder texture(Texture texture) {
            this.texture = texture;
            return this;
        }

        public MaterialBuilder refractiveIndex(Double refractiveIndex) {
            this.refractiveIndex = refractiveIndex;
            return this;
        }

        public MaterialBuilder emit(IColour emit) {
            this.emit = emit;
            return this;
        }

        public MaterialBuilder recast(Function<RayHit, Optional<IRay>> recaster, double contribution) {
            this.recasters.add(new RecasterContribution(recaster, contribution));
            return this;
        }

        public Material build() {
            List<RecasterContribution> recasters = this.recasters;
            if (recasters.isEmpty()) {
                recasters = Collections.singletonList(new RecasterContribution(Recasters.diffuse, 1));
            }
            return new Material(this.texture, this.refractiveIndex, this.emit, recasters);
        }

        public String toString() {
            return "Material.MaterialBuilder(texture=" + this.texture + ", refractiveIndex=" + this.refractiveIndex + ", emit=" + this.emit + ")";
        }
    }
}

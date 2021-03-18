package io.raytracer.drawing;

import lombok.Builder;

import java.util.Arrays;

@Builder(toBuilder = true)
public class Material {
    @Builder.Default public final Colour colour = new ColourImpl(0, 0, 0);
    public final double ambient;
    public final double diffuse;
    public final double specular;
    public final double shininess;
    private static final double equalityTolerance = 1e-3;

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[] {
                this.colour.hashCode(),
                Arrays.hashCode(new double[] {this.ambient, this.diffuse, this.specular, this.shininess})});
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Material themMaterial = (Material) them;
        return (this.colour.equals(themMaterial.colour)
            && Math.abs(this.ambient - themMaterial.ambient) < equalityTolerance
            && Math.abs(this.diffuse - themMaterial.diffuse) < equalityTolerance
            && Math.abs(this.specular - themMaterial.specular) < equalityTolerance
            && Math.abs(this.shininess - themMaterial.shininess) < equalityTolerance);
    }
}

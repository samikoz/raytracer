package io.raytracer.drawing;

import java.util.Arrays;

public class Material {
    public Colour colour;
    public double ambient;
    public double diffuse;
    public double specular;
    public double shininess;
    private static final double equalityTolerance = 1e-3;

    public Material() {
        this.colour = new ColourImpl(1, 1, 1);
        this.ambient = 0.1;
        this.diffuse = 0.9;
        this.specular = 0.9;
        this.shininess = 200.0;
    }

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

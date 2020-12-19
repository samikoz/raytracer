package io.raytracer.light;

import io.raytracer.drawing.Colour;
import io.raytracer.drawing.ColourImpl;

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

    public void colour(Colour colour) {
        this.colour = colour;
    }

    public void ambient(double ambient) {
        this.ambient = ambient;
    }

    public void diffuse(double diffuse) {
        this.diffuse = diffuse;
    }

    public void specular(double specular) {
        this.specular = specular;
    }

    public void shininess(double shininess) {
        this.shininess = shininess;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[] {
                this.colour.hashCode(),
                Arrays.hashCode(new double[] {this.ambient, this.diffuse, this.specular, this.shininess})});
    }

    @Override
    public boolean equals(Object them) {
        if (this.getClass() != them.getClass()) return false;

        Material themMaterial = (Material) them;
        return (this.colour.equals(themMaterial.colour)
            && Math.abs(this.ambient - themMaterial.ambient) < this.equalityTolerance
            && Math.abs(this.diffuse - themMaterial.diffuse) < this.equalityTolerance
            && Math.abs(this.specular - themMaterial.specular) < this.equalityTolerance
            && Math.abs(this.shininess - themMaterial.shininess) < this.equalityTolerance);
    }
}

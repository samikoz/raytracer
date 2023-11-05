package io.raytracer.shapes.readymades;

import io.raytracer.materials.Material;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder(toBuilder = true)
@AllArgsConstructor
public class SpiralParameters {
    public int count;
    public boolean orientation;
    public double initialHeight;
    public double radius;
    public double stairWidth;
    public double stairHeight;
    public double stairDepth;
    public double elevationDiff;
    public double stairSeparation;
    public double angleOffset;
    public int skip;
    public Material stairMaterial;

    public int getOrientation() {
        return this.orientation ? 1 : -1;
    }
}

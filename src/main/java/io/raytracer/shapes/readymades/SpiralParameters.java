package io.raytracer.shapes.readymades;

import io.raytracer.geometry.IPoint;
import io.raytracer.materials.Material;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class SpiralParameters {
    public int countBefore;
    public int count;
    public IPoint centre;
    public boolean orientation;
    public double radius;
    public double stairWidth;
    public double stairHeight;
    public double stairDepth;
    public double stairDrop;
    public double stairSeparation;
    public double angleOffset;
    public int skip;
    public Material stairMaterial;

    public int getOrientation() {
        return Boolean.valueOf(this.orientation).compareTo(Boolean.TRUE);
    }
}

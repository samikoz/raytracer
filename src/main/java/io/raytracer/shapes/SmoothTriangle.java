package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.materials.Material;
import lombok.NonNull;

import java.util.Arrays;

public class SmoothTriangle extends Triangle {
    public final IVector n1, n2, n3;

    public SmoothTriangle(IPoint v1, IPoint v2, IPoint v3, IVector n1, IVector n2, IVector n3) {
        super(v1, v2, v3);
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
    }

    public SmoothTriangle(IPoint v1, IPoint v2, IPoint v3, IVector n1, IVector n2, IVector n3, @NonNull Material material) {
        super(v1, v2, v3, material);
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new double[] {
            v1.hashCode(), v2.hashCode(), v3.hashCode(),
            n1.hashCode(), n2.hashCode(), n3.hashCode()
        });
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        SmoothTriangle themTriangle = (SmoothTriangle) them;
        return (
            this.v1.equals(themTriangle.v1) &&
            this.v2.equals(themTriangle.v2) &&
            this.v3.equals(themTriangle.v3) &&
            this.n1.equals(themTriangle.n1) &&
            this.n2.equals(themTriangle.n2) &&
            this.n3.equals(themTriangle.n3)
        );
    }

    @Override
    protected IVector localNormalAt(IPoint point, double u, double v) {
        return this.n2.multiply(u).add(this.n3.multiply(v)).add(this.n1.multiply(1 - u - v));
    }

}

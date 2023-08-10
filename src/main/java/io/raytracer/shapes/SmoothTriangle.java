package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.materials.Material;
import lombok.NonNull;

public class SmoothTriangle extends Triangle {
    private final IVector n1, n2, n3;

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
    protected IVector localNormalAt(IPoint point, double u, double v) {
        return this.n2.multiply(u).add(this.n3.multiply(v)).add(this.n1.multiply(1 - u - v));
    }

}

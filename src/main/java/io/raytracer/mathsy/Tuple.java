package io.raytracer.mathsy;

public interface Tuple {
    double getX();
    double getY();
    double getZ();

    boolean equalTo(Tuple them);
}
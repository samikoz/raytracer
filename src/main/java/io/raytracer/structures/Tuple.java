package io.raytracer.structures;

interface Tuple {
    double getX();
    double getY();
    double getZ();

    boolean equalTo(Tuple them);
}
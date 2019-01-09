package io.raytracer.structures;

interface Tuple {
    double getX();
    double getY();
    double getZ();

    default double distance(Tuple them) {
        return Math.sqrt(
            Math.pow(this.getX() - them.getX(), 2) +
            Math.pow(this.getY() - them.getY(), 2) +
            Math.pow(this.getZ() - them.getZ(), 2)
        );
    }

    default boolean equalTo(Tuple them) {
        return (this.getClass() == them.getClass() && this.distance(them) < 1e-3);
    }
}
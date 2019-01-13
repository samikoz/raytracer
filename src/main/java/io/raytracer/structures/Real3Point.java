package io.raytracer.structures;

public final class Real3Point extends Real3Tuple implements Point {
    public Real3Point(double x, double y, double z) {
        super(x, y, z);
    }

    @Override
    public double distance(Point them) {
        return super.distance(them);
    }

    @Override
    public Real3Point add(Vector displacement) {
        return new Real3Point(
            this.getX() + displacement.getX(),
            this.getY() + displacement.getY(),
            this.getZ() + displacement.getZ()
        );
    }

    @Override
    public Real3Vector subtract(Point them) {
        return null;
    }

    @Override
    public Real3Point subtract(Vector displacement) {
        return null;
    }
}
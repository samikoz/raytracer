package io.raytracer.drawing;

public interface Canvas {

    void write(int x, int y, Colour colour);
    Colour read(int x, int y);

    String export();
}

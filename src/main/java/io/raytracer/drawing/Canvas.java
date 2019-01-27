package io.raytracer.drawing;

import java.util.Iterator;

public interface Canvas extends Iterable<Colour> {

    @Override
    Iterator<Colour> iterator();

    void write(int x, int y, Colour colour);
    Colour read(int x, int y);

    String exportToPPM();
}

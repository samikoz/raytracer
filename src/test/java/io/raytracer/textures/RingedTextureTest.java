package io.raytracer.textures;

import io.raytracer.tools.LinearColour;
import io.raytracer.geometry.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RingedTextureTest {
    private final LinearColour black = new LinearColour(0, 0, 0);
    private final LinearColour white = new LinearColour(1, 1, 1);

    @Test
    void ringTextureExtendsInBothXAndZ() {
        Texture ringed = new RingedTexture(white, black);

        assertEquals(white, ringed.ownColourAt(new Point(0, 0, 0)));
        assertEquals(black, ringed.ownColourAt(new Point(1, 0, 0)));
        assertEquals(black, ringed.ownColourAt(new Point(0, 0, 1)));
        assertEquals(black, ringed.ownColourAt(new Point(Math.sqrt(2) / 2, 0, Math.sqrt(2) / 2)));
    }
}
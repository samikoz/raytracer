package io.raytracer.textures;

import io.raytracer.tools.LinearColour;
import io.raytracer.tools.IColour;
import io.raytracer.geometry.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StripedTextureTest {
    private final LinearColour black = new LinearColour(0, 0, 0);
    private final LinearColour white = new LinearColour(1, 1, 1);

    @Test
    void stripedTextureIsConstantInY() {
        Texture striped = new StripedTexture(white, black);
        IColour firstColour = striped.colourAt(new Point(0, 0, 0));
        IColour secondColour = striped.colourAt(new Point(0, 1, 0));
        IColour thirdColour = striped.colourAt(new Point(0, 2.3, 0));

        assertEquals(white, firstColour);
        assertEquals(white, secondColour);
        assertEquals(white, thirdColour);
    }

    @Test
    void stripedTextureIsConstantInZ() {
        Texture striped = new StripedTexture(white, black);
        IColour firstColour = striped.colourAt(new Point(0, 0, 0));
        IColour secondColour = striped.colourAt(new Point(0, 0, 1));
        IColour thirdColour = striped.colourAt(new Point(0, 0, -2.3));

        assertEquals(white, firstColour);
        assertEquals(white, secondColour);
        assertEquals(white, thirdColour);
    }

    @Test
    void stripedTextureAlternatesInX() {
        Texture striped = new StripedTexture(white, black);
        IColour firstColour = striped.colourAt(new Point(0, 0, 0));
        IColour secondColour = striped.colourAt(new Point(1, 0, 0));
        IColour thirdColour = striped.colourAt(new Point(2.3, 0, 0));

        assertEquals(white, firstColour);
        assertEquals(black, secondColour);
        assertEquals(white, thirdColour);
    }
}
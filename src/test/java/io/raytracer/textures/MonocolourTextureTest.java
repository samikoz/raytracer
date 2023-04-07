package io.raytracer.textures;

import io.raytracer.tools.Colour;
import io.raytracer.tools.IColour;
import io.raytracer.geometry.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MonocolourTextureTest {

    @Test
    void monotextureAlwaysReturnsTheSameColour() {
        Colour colour = new Colour(0.2, 0.3, 0.4);
        MonocolourTexture texture = new MonocolourTexture(colour);

        IColour firstColour = texture.colourAt(new Point(-1, 2, 15));
        IColour secondColour = texture.colourAt(new Point(15, 0, 0.1));

        assertEquals(colour, firstColour);
        assertEquals(colour, secondColour);
    }
}
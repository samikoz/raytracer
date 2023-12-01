package io.raytracer.textures;

import io.raytracer.geometry.Point;
import io.raytracer.algebra.ThreeTransform;
import io.raytracer.tools.IColour;
import io.raytracer.tools.LinearColour;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlendTextureTest {

    @Test
    void colourAt() {
        IColour white = new LinearColour(1, 1, 1);
        IColour halfRed = new LinearColour(0.5, 0, 0);
        Texture firstStripes = new StripedTexture(white, halfRed);
        Texture secondStripes = new StripedTexture(white, halfRed);
        secondStripes.setTransform(ThreeTransform.rotation_z(-Math.PI / 2));
        Texture blend = new BlendTexture(firstStripes, secondStripes);

        assertEquals(halfRed.add(white), blend.colourAt(new Point(0.5, 0.5, 0)));
        assertEquals(halfRed.multiply(2), blend.colourAt(new Point(1.5, 0.5, 0)));
        assertEquals(white.multiply(2), blend.colourAt(new Point(0.5, 1.5, 0)));
    }
}
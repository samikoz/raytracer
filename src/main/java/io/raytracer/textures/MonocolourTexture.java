package io.raytracer.textures;

import io.raytracer.tools.IColour;
import io.raytracer.geometry.IPoint;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public class MonocolourTexture extends Texture {
    @NonNull private final IColour monocolour;

    public MonocolourTexture(@NotNull IColour colour) {
        super();
        this.monocolour = colour;
    }

    public IColour ownColourAt(IPoint point) {
        return this.monocolour;
    }

    public int getHashCode() {
        return this.monocolour.hashCode();
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        MonocolourTexture themMonocolour = (MonocolourTexture) them;
        return themMonocolour.monocolour.equals(this.monocolour);
    }
}

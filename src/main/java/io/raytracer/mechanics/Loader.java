package io.raytracer.mechanics;

import java.io.File;

public interface Loader {
    void populate(IWorld world);

    void load(File file);
}

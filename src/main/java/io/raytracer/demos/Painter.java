package io.raytracer.demos;

import io.raytracer.mechanics.LambertianWorld;
import io.raytracer.mechanics.World;
import io.raytracer.shapes.Group;
import io.raytracer.shapes.Hittable;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.LinearColour;
import lombok.Getter;

import java.io.IOException;

@Getter
public abstract class Painter {
    protected DemoSetup setup;
    public Painter(DemoSetup setup) {
        this.setup = setup;
    }

    abstract protected DemoSetup setSetup(DemoSetup setup);

    protected LinearColour backgroundColour() {
        return new LinearColour(0);
    }

    abstract protected Hittable[] makeObjects();

    protected World makeWorld() {
        World world = new LambertianWorld(this.backgroundColour());
        world.put(new Group(this.makeObjects()));
        return world;
    }

    public IPicture render() throws IOException {
        this.setup = this.setSetup(this.setup);
        IPicture picture = this.setup.makePicture();
        this.makeWorld().render(picture, this.setup.makeCamera());
        return picture;
    }
}

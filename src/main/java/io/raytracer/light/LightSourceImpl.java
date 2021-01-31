package io.raytracer.light;

import io.raytracer.drawing.ColourImpl;
import io.raytracer.drawing.Material;
import io.raytracer.geometry.Vector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.raytracer.drawing.Colour;
import io.raytracer.geometry.Point;

@RequiredArgsConstructor
public class LightSourceImpl implements LightSource {
    @NonNull public final Colour colour;
    @NonNull public final Point position;

    public Colour illuminate(@NonNull IlluminatedPoint illuminated) {
        Material material = illuminated.object.getMaterial();
        Colour effectiveColour = material.colour.mix(this.colour);
        Vector sourceDirection = this.position.subtract(illuminated.point).normalise();
        Colour ambientContribution = effectiveColour.multiply(material.ambient);
        double lightNormalCosine = sourceDirection.dot(illuminated.normalVector);

        Colour diffuseContribution;
        Colour specularContribution;
        if (lightNormalCosine < 0) {
            diffuseContribution = new ColourImpl(0, 0, 0);
            specularContribution = new ColourImpl(0, 0, 0);
        } else {
            diffuseContribution = effectiveColour.multiply(material.diffuse).multiply(lightNormalCosine);
            Vector reflectionVector = sourceDirection.negate().reflect(illuminated.normalVector);
            double reflectionEyeCosine = reflectionVector.dot(illuminated.eyeVector);

            if (reflectionEyeCosine <= 0) {
                specularContribution = new ColourImpl(0, 0, 0);
            } else {
                double specularFactor = Math.pow(reflectionEyeCosine, material.shininess);
                specularContribution = this.colour.multiply(material.specular).multiply(specularFactor);
            }
        }
        return ambientContribution.add(diffuseContribution).add(specularContribution);
    }
}

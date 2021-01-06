package io.raytracer.light;

import lombok.NonNull;

import io.raytracer.drawing.Colour;
import io.raytracer.drawing.ColourImpl;
import io.raytracer.geometry.Vector;

public class Lighting {
    public static Colour illuminate(@NonNull LightSource source, @NonNull Vector eyeVector, @NonNull IlluminatedPoint illuminated) {
        Colour effectiveColour = illuminated.material.colour.mix(source.colour);
        Vector sourceDirection = source.position.subtract(illuminated.point).normalise();
        Colour ambientContribution = effectiveColour.multiply(illuminated.material.ambient);
        double lightNormalCosine = sourceDirection.dot(illuminated.normalVector);

        Colour diffuseContribution;
        Colour specularContribution;
        if (lightNormalCosine < 0) {
            diffuseContribution = new ColourImpl(0, 0, 0);
            specularContribution = new ColourImpl(0, 0, 0);
        } else {
            diffuseContribution = effectiveColour.multiply(illuminated.material.diffuse).multiply(lightNormalCosine);
            Vector reflectionVector = sourceDirection.negate().reflect(illuminated.normalVector);
            double reflectionEyeCosine = reflectionVector.dot(eyeVector);

            if (reflectionEyeCosine <= 0) {
                specularContribution = new ColourImpl(0, 0, 0);
            } else {
                double specularFactor = Math.pow(reflectionEyeCosine, illuminated.material.shininess);
                specularContribution = source.colour.multiply(illuminated.material.specular).multiply(specularFactor);
            }
        }
        return ambientContribution.add(diffuseContribution).add(specularContribution);
    }
}

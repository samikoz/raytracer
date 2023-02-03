package io.raytracer.worldly;

import io.raytracer.drawing.Colour;
import io.raytracer.geometry.IVector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.raytracer.drawing.IColour;
import io.raytracer.geometry.IPoint;

@RequiredArgsConstructor
public class LightSource implements ILightSource {
    @NonNull public final IColour colour;
    @NonNull public final IPoint position;

    public IColour illuminate(MaterialPoint illuminated) {
        Material material = illuminated.object.getMaterial();
        IColour effectiveColour = material.colour.mix(this.colour);
        IVector sourceDirection = this.position.subtract(illuminated.point).normalise();
        IColour ambientContribution = effectiveColour.multiply(material.ambient);
        double lightNormalCosine = sourceDirection.dot(illuminated.normalVector);

        IColour diffuseContribution;
        IColour specularContribution;
        if (lightNormalCosine < 0) {
            diffuseContribution = new Colour(0, 0, 0);
            specularContribution = new Colour(0, 0, 0);
        } else {
            diffuseContribution = effectiveColour.multiply(material.diffuse).multiply(lightNormalCosine);
            IVector reflectionVector = sourceDirection.negate().reflect(illuminated.normalVector);
            double reflectionEyeCosine = reflectionVector.dot(illuminated.eyeVector);

            if (reflectionEyeCosine <= 0) {
                specularContribution = new Colour(0, 0, 0);
            } else {
                double specularFactor = Math.pow(reflectionEyeCosine, material.shininess);
                specularContribution = this.colour.multiply(material.specular).multiply(specularFactor);
            }
        }
        return ambientContribution.add(diffuseContribution).add(specularContribution);
    }
}

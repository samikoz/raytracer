package io.raytracer.tools;

import java.util.ArrayList;
import java.util.List;

public class AveragingPPMPicture extends PPMPicture {
    private final int averagingFactor;

    public AveragingPPMPicture(int x, int y, int averagingFactor) {
        super(x, y);
        this.averagingFactor = averagingFactor;
    }

    @Override
    protected String exportRow(ArrayList<IColour> ppmRow) {
        //more neatly would be to expose protected getPpmRow() or something
        List<String> exported = new ArrayList<>();
        ppmRow.forEach(rowElement -> exported.add(rowElement.multiply(1.0/averagingFactor).export()));
        return String.join(" ", exported);
    }
}

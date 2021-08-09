package DTO;

import Evolution.Selection.Truncation;

public class DTOTruncation implements DTOSelection{

    private Truncation truncation;

    public DTOTruncation(Truncation truncation) {
        this.truncation = truncation;
    }

    public final int getTopPercent() {
        return truncation.getTopPercent();
    }

    @Override
    public String toString() {
        return truncation.toString();
    }
}

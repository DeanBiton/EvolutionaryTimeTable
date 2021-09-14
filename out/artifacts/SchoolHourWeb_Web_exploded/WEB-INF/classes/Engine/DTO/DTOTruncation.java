package Engine.DTO;

import Engine.Evolution.Selection.Truncation;

public class DTOTruncation implements DTOSelection{

    private Truncation truncation;

    public DTOTruncation(Truncation truncation) {
        this.truncation = truncation;
    }

    public final int getTopPercent() {
        return truncation.getTopPercent();
    }

    @Override
    public int getElitism() {
        return truncation.getElitism();
    }

    @Override
    public String getName() {
        return truncation.getName();
    }

    @Override
    public String toString() {
        return truncation.toString();
    }
}

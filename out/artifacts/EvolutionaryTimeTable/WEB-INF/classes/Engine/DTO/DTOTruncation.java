package Engine.DTO;

import Engine.Evolution.Selection.Truncation;

public class DTOTruncation implements DTOSelection{

    private String name;
    private Truncation truncation;
    private int elitism;

    public DTOTruncation(Truncation truncation) {

        this.truncation = truncation;
        this.name = truncation.getName();
        this.elitism = truncation.getElitism();
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

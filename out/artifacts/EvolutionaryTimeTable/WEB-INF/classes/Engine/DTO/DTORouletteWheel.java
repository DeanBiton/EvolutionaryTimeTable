package Engine.DTO;

import Engine.Evolution.Selection.RouletteWheel;

public class DTORouletteWheel implements DTOSelection{

    private String name;
    private RouletteWheel rouletteWheel;
    private int elitism;

    public DTORouletteWheel(RouletteWheel rouletteWheel) {

        this.rouletteWheel = rouletteWheel;
        this.name = rouletteWheel.getName();
        this.elitism = rouletteWheel.getElitism();
    }



    @Override
    public int getElitism() {
        return rouletteWheel.getElitism();
    }

    @Override
    public String getName() {
        return rouletteWheel.getName();
    }

    @Override
    public String toString() {
        return rouletteWheel.toString();
    }
}

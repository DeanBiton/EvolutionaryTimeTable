package Engine.DTO;

import Engine.Evolution.Selection.RouletteWheel;

public class DTORouletteWheel implements DTOSelection{

    private RouletteWheel rouletteWheel;

    public DTORouletteWheel(RouletteWheel rouletteWheel) {
        this.rouletteWheel = rouletteWheel;
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

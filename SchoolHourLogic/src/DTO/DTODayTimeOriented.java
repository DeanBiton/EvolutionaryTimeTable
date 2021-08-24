package DTO;

import Evolution.MySolution.Crossover.DayTimeOriented;

public class DTODayTimeOriented implements DTOCrossover {

    private DayTimeOriented dayTimeOriented;

    public DTODayTimeOriented(DayTimeOriented dayTimeOriented) {
        this.dayTimeOriented = dayTimeOriented;
    }

    @Override
    public String getName() {
        return dayTimeOriented.getName();
    }

    @Override
    public final int getNumberOfSeparators(){
        return dayTimeOriented.getNumberOfSeperators();
    }

    @Override
    public String toString() {
        return dayTimeOriented.toString();
    }
}

package Engine.DTO;

import Engine.Evolution.MySolution.Crossover.DayTimeOriented;

public class DTODayTimeOriented implements DTOCrossover {

    private DayTimeOriented dayTimeOriented;
    private String name;
    private int numberOfSeparators;

    public DTODayTimeOriented(DayTimeOriented dayTimeOriented) {

        this.dayTimeOriented = dayTimeOriented;
        this.name = dayTimeOriented.getName();
        this.numberOfSeparators = dayTimeOriented.getNumberOfSeperators();
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

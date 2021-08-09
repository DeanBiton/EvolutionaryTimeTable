package DTO;

import Evolution.MySolution.Crossover.AspectOriented;
import Evolution.MySolution.Crossover.Crossover;
import Evolution.MySolution.Crossover.DayTimeOriented;
import Evolution.Selection.Truncation;

public interface DTOCrossover {
    public int getNumberOfSeparators();

    public static DTOCrossover getDTOCrossover(Crossover crossover)
    {
        DTOCrossover dtoCrossover = null;

        if(crossover instanceof DayTimeOriented)
        {
            dtoCrossover = new DTODayTimeOriented((DayTimeOriented) crossover);
        }
        else if(crossover instanceof AspectOriented)
        {
            dtoCrossover = new DTOAspectOriented((AspectOriented) crossover);
        }

        return dtoCrossover;
    }

    public static String getDTOCrossoverToString(DTOCrossover dtoCrossover) {
        StringBuilder stringBuilder = new StringBuilder();

        if(dtoCrossover instanceof DTODayTimeOriented)
        {
            stringBuilder.append(((DTODayTimeOriented)dtoCrossover).toString());
        }
        else if(dtoCrossover instanceof DTOAspectOriented)
        {
            stringBuilder.append(((DTOAspectOriented)dtoCrossover).toString());
        }

        return stringBuilder.toString();
    }
}

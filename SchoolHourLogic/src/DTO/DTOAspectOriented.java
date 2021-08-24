package DTO;

import Evolution.MySolution.Crossover.AspectOriented;

public class DTOAspectOriented implements DTOCrossover{

    private AspectOriented aspectOriented;

    public DTOAspectOriented(AspectOriented aspectOriented) {
        this.aspectOriented = aspectOriented;
    }

    @Override
    public String getName() {
        return aspectOriented.getName();
    }

    public AspectOriented.OrientationType getOrientationType()
    {
        return aspectOriented.getOrientationType();
    }

    @Override
    public final int getNumberOfSeparators(){
        return aspectOriented.getNumberOfSeperators();
    }

    @Override
    public String toString() {
        return aspectOriented.toString();
    }
}

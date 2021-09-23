package Engine.DTO;

import Engine.Evolution.MySolution.Crossover.AspectOriented;

public class DTOAspectOriented implements DTOCrossover{

    private AspectOriented aspectOriented;
    private String name;
    private int numberOfSeparators;

    public DTOAspectOriented(AspectOriented aspectOriented) {

        this.aspectOriented = aspectOriented;
        this.name = aspectOriented.getName();
        this.numberOfSeparators = aspectOriented.getNumberOfSeperators();
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

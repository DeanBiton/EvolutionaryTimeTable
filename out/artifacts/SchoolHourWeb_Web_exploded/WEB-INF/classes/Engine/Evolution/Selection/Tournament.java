package Engine.Evolution.Selection;

import Engine.Evolution.Evolutionary;
import Engine.Evolution.HelperFunc;

import java.util.ArrayList;
import java.util.List;

public class Tournament extends Selection{
    double pte;



    public Tournament(int elitism, double pte) {
        super(elitism);

        if(pte <= 0 || pte >= 1)
        {
            throw new RuntimeException ("pte must be between 0 and 1");
        }
        this.pte = pte;
    }

    public double getPte() {
        return pte;
    }

    @Override
    public String getName() {
        return "Tournament";
    }
    @Override
    protected List<Evolutionary> selection(List<Evolutionary> currentGen, int numberOfParents) {

        List<Evolutionary> selected= new ArrayList<>(numberOfParents);
        Evolutionary evolutionary1,evolutionary2,evolutionaryselected;
        double randomNumber;

        for (int i=0;i<numberOfParents;i++)
        {
            evolutionary1= currentGen.get(HelperFunc.getRandomNumber(0,currentGen.size()-1));
            evolutionary2= currentGen.get(HelperFunc.getRandomNumber(0,currentGen.size()-1));
            randomNumber= Math.random();

            evolutionaryselected= randomNumber>=pte?
                    evolutionary1.fitness()>evolutionary2.fitness()?evolutionary1:evolutionary2 :
                    evolutionary1.fitness()>evolutionary2.fitness()?evolutionary2:evolutionary1 ;

            selected.add(evolutionaryselected);
        }
        return selected;
    }
}

package Engine.Evolution.Selection;

import Engine.Evolution.Evolutionary;

import java.util.ArrayList;
import java.util.List;

public class RouletteWheel extends Selection{
    public RouletteWheel(int elitism) {
        super(elitism);
    }

    @Override
    public String getName() {
        return "RouletteWheel";
    }

    @Override
    protected List<Evolutionary> selection(List<Evolutionary> currentGen, int numberOfParents) {
        double fitnessSum =currentGen.stream().mapToDouble(Evolutionary::fitness).sum();
        double randomNUmber;
        double counter;
        List<Evolutionary> selected= new ArrayList<>(numberOfParents);

        for (int laps=0;laps<numberOfParents;laps++)
        {
            randomNUmber= Math.random()*fitnessSum;
            counter=0.0;
            for(int i=0;i<currentGen.size();i++)
            {
                counter+=currentGen.get(i).fitness();
                if(randomNUmber<counter)
                {
                    selected.add(currentGen.get(i));
                    break;
                }
            }
        }
        return selected;
    }
}

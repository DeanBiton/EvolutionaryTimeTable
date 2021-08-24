package Evolution.Selection;
import Evolution.Evolutionary;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public abstract class Selection implements Serializable {

    private int elitism;

    public Selection(int elitism) {
        if(elitism < 0)
        {
            throw new src.ShowException("elitism must be a positive number");
        }

        this.elitism = elitism;
    }

    public int getElitism() {
        return elitism;
    }

    public List<Evolutionary> selection(List<Evolutionary> currentGen, List<Evolutionary> children)
    {
        if(currentGen == null)
        {
            throw new RuntimeException("currentGen must be initialize.");
        }
        else if(children == null)
        {
            throw new RuntimeException("children must be initialize.");
        }
        else if(currentGen.isEmpty() )
        {
            throw new RuntimeException("currentGen must have at least 1 variable.");
        }
        else if(!children.isEmpty())
        {
            throw new RuntimeException("children must be empty");
        }

        int numberOfParent = (currentGen.size()-elitism) % 2 == 0 ? (currentGen.size()-elitism) : (currentGen.size()-elitism) + 1;

        Collections.sort(currentGen, Collections.reverseOrder(Evolutionary::compare));
        for(int i = 0; i < elitism; i++)
        {
            children.add(currentGen.get(i).clone());
        }

        return selection(currentGen, numberOfParent);
    }

    protected  abstract String getName();
    protected abstract List<Evolutionary> selection(List<Evolutionary> currentGen, int numberOfParents);
}

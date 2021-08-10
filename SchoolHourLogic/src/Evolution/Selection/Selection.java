package Evolution.Selection;
import Evolution.Evolutionary;

import java.io.Serializable;
import java.util.List;

public abstract class Selection implements Serializable {

    private int elitism;

    public Selection(int elitism) {
        if(elitism < 0)
        {
            throw new RuntimeException("elitism must be a positive number");
        }

        this.elitism = elitism;
    }

    public abstract List<Evolutionary> selection(List<Evolutionary> currentGen);
}

package Engine.Evolution.MySolution.MyMutation;
import Engine.ShowException;
import Engine.Evolution.MySolution.TupleGroup;

import java.io.Serializable;

public abstract class Mutation implements Serializable {

protected double probability;

public Mutation(double _probability) {

    if(_probability < 0 || _probability > 1)
    {
        throw new ShowException("Probability must be between 0 and 1");
    }

    probability = _probability;
}

    public double getProbability() {
        return probability;
    }

    public abstract void mutate(TupleGroup group);
}
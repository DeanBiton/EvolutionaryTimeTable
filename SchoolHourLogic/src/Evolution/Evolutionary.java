package Evolution;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface Evolutionary extends Serializable {

    public Evolutionary clone();

    public void random();

    public double fitness();

    public default double fitnessOfRule(FitnessRules rule)
    {
        return rule.fitnessRuleCalc(this);
    }

    public void mutation();

    public static int compare( Evolutionary tupleGroup1, Evolutionary tupleGroup2) {
        int result;
        double o1Fitness = tupleGroup1.fitness();
        double o2Fitness = tupleGroup2.fitness();
        if(o1Fitness > o2Fitness)
        {
            result = 1;
        }
        else if(o1Fitness < o2Fitness)
        {
            result = -1;
        }
        else
        {
            result = 0;
        }

        return result;
    }
}
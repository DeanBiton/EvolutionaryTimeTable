package Evolution.MySolution.Crossover;
import Evolution.Evolutionary;
import Evolution.HelperFunc;
import Evolution.MySolution.Tuple;
import Evolution.MySolution.TupleGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public abstract class Crossover implements Serializable {

    protected int numberOfSeparators;

    Crossover(int _numberOfSeparators)
    {
        numberOfSeparators = _numberOfSeparators;
    }

    public int getNumberOfSeperators() {
        return numberOfSeparators;
    }

    public List<Evolutionary> crossover(TupleGroup parent1, TupleGroup parent2)
    {
        if(parent1.getData() != parent2.getData())
        {
            throw new RuntimeException("The data of the 2 parents are not equal");
        }

        List<List<Tuple>> parent1List = crossoverInitialize(parent1);
        List<List<Tuple>> parent2List = crossoverInitialize(parent2);

        TupleGroup child1=new TupleGroup(parent1.getData());
        TupleGroup child2=new TupleGroup(parent1.getData());

         List<Tuple>  child1Tuples= new ArrayList<>();
         List<Tuple> child2Tuples= new ArrayList<>();

        Set<Integer> separatorsIndexes= new TreeSet<>();
        while(separatorsIndexes.size()!=numberOfSeparators)
        {
            separatorsIndexes.add( HelperFunc.getRandomNumber(1, parent1.getData().getNumberOfDays() * parent1.getData().getNumberOfHoursInADay()-1));
        }

        for(int i=0;i<parent1.getData().getNumberOfDays()*parent1.getData().getNumberOfHoursInADay();i++)
        {
            if(separatorsIndexes.contains(i))
            {
                List<Tuple> childTemp=child1Tuples;
                child1Tuples=child2Tuples;
                child2Tuples=childTemp;
            }


            for(Tuple tuple:parent1List.get(i))
            {
                child1Tuples.add(new Tuple(tuple));
            }
            for(Tuple tuple:parent2List.get(i))
            {
                child2Tuples.add(new Tuple(tuple));
            }
        }

        child1.setTuples(child1Tuples);
        child2.setTuples(child2Tuples);

        List<Evolutionary> children=new ArrayList<>();
        children.add(child1);
        children.add(child2);

        return children;
    }

    public abstract List<List<Tuple>> crossoverInitialize(TupleGroup parent);
}

package Engine.Evolution.MySolution.MyMutation;

import Engine.Evolution.HelperFunc;
import Engine.Evolution.MySolution.TupleGroup;

public class Sizer extends Mutation{

    private int totalTuples;

    public Sizer(double _probability, int _totalTuples) {
        super(_probability);
        totalTuples = _totalTuples;
    }

    public int getTotalTuples() {
        return totalTuples;
    }

    @Override
    public void mutate(TupleGroup group) {
        if((probability >= Math.random() || probability == 1) && totalTuples != 0)
        {
            int numberOfTuples = getNumberOfTuples(group);
            if(totalTuples > 0)
            {
                addTuples(group, numberOfTuples);
            }
            else
            {
                removeTuples(group, -numberOfTuples);
            }
        }
    }

    private int getNumberOfTuples(TupleGroup group)
    {
        int numberOfTuples = 0;
        if(totalTuples > 0)
        {
            int DH = group.getData().getNumberOfHoursInADay() * group.getData().getNumberOfDays();
            if(totalTuples + group.getTuples().size() > DH)
            {
                numberOfTuples = DH - group.getTuples().size();
            }
            else
            {
                numberOfTuples = totalTuples;
            }
        }
        else if(totalTuples < 0)
        {
            if(-totalTuples > group.getTuples().size())
            {
                numberOfTuples = group.getTuples().size();
            }
            else
            {
                numberOfTuples = totalTuples;
            }
        }

        return numberOfTuples;
    }

    private void addTuples(TupleGroup group, int numberOfTuples) {
        for (int i = 0; i < numberOfTuples; i++)
        {
            group.getTuples().add(group.getData().createRandomTuple());
        }
    }

    private void removeTuples(TupleGroup group, int numberOfTuples) {
        for (int i = 0; i < numberOfTuples; i++)
        {
            int tupleIndex = HelperFunc.getRandomNumber(0, group.getTuples().size()-1);
            group.getTuples().remove(tupleIndex);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Probability: ");
        stringBuilder.append(probability);
        stringBuilder.append(", Total Tuples: ");
        stringBuilder.append(totalTuples);
        stringBuilder.append(System.lineSeparator());

        return stringBuilder.toString();
    }
}

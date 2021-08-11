package Evolution.MySolution;
import Evolution.*;
import java.util.*;

public class TupleGroup implements Evolutionary {

    private List<Tuple> tuples;
    private SchoolHourData data;
    private double fitness;

    @Override
    public Evolutionary clone()
    {
        TupleGroup clone = new TupleGroup(data);
        List<Tuple> cloneTuples = new ArrayList<>();

        tuples.forEach(tuple -> cloneTuples.add(new Tuple(tuple)));
        clone.setTuples(cloneTuples);
        clone.fitness = fitness;
        return clone;
    }

    public TupleGroup(SchoolHourData _data) {
        data = _data;
        fitness = -1;
        tuples = new ArrayList<>();
    }

    public SchoolHourData getData() {
        return data;
    }

    public List<Tuple> getTuples() {
        return tuples;
    }

    public void setTuples(List<Tuple> tuples) {
        this.tuples = tuples;
    }

    @Override
    public void random() {
        int numberOfTuples = getNumberOfTuples();

        tuples = new ArrayList<>(numberOfTuples);

        for(int i = 0; i < numberOfTuples; i++)
        {
            tuples.add(data.createRandomTuple());
        }
    }

    private int getNumberOfTuples() {
        //sumOfHours
        return data.getClassrooms().values().stream()
                .mapToInt(classroom->classroom.getSubjectId2WeeklyHours().values().stream()
                        .mapToInt(i->i)
                        .sum())
                .sum();
    }

    @Override
    public double fitness() {

        if(this.fitness != -1)
        {
            return this.fitness;
        }

        List<Rule> rules = data.getRules();
        int numberOfHardRules = 0;
        int numberOfSoftRules = 0;
        double SumOfHardRulesScore = 0;
        double SumOfSoftRulesScore = 0;
        double fitness = 0;

        for(Rule rule : rules) {
            double score = this.fitnessOfRule(rule.getType());
            if(rule.getImplementationLevel() == Rule.RuleImplementationLevel.Hard)
            {
                numberOfHardRules++;
                SumOfHardRulesScore += score;
            }
            else
            {
                numberOfSoftRules++;
                SumOfSoftRulesScore += score;
            }
        }

        if(numberOfHardRules > 0)
        {
            fitness += SumOfHardRulesScore / numberOfHardRules * data.getHardRulesWeight() / 100;
        }
        if(numberOfSoftRules > 0)
        {
            fitness += SumOfSoftRulesScore / numberOfSoftRules * (100 - data.getHardRulesWeight()) / 100;
        }

        this.fitness = fitness;
        return fitness;
    }

    @Override
    public void mutation() {
        data.getMutations().forEach(mutation -> mutation.mutate(this));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder= new StringBuilder();
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("TupleGroup:");
        stringBuilder.append(System.lineSeparator());

        for (Tuple tuple:tuples)
        {
            stringBuilder.append(tuple);
            stringBuilder.append(System.lineSeparator());
        }
       return stringBuilder.toString();

    }
}

package Evolution.EndCondition;

public class EndConditionGetterClass {
    private int NumberOfGenerations;
    private double fitness;

    public EndConditionGetterClass(int numberOfGenerations, double fitness) {
        NumberOfGenerations = numberOfGenerations;
        this.fitness = fitness;
    }

    public int getNumberOfGenerations() {
        return NumberOfGenerations;
    }

    public double getFitness() {
        return fitness;
    }
}

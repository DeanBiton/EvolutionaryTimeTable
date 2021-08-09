package Evolution.EndCondition;

public class ByFitness implements EndCondition {

    private double fitness;

    public ByFitness(double fitness) {
        if(fitness <= 0)
        {
            throw new RuntimeException("fitness must be positive.");
        }
        else if(fitness > 100)
        {
            throw new RuntimeException("fitness can't be above 100.");
        }

        this.fitness = fitness;
    }

    @Override
    public boolean checkCondition(EndConditionGetterClass endConditionGetterClass) {
        if(fitness>endConditionGetterClass.getFitness())
            return false;
        else
            return true;
    }
}

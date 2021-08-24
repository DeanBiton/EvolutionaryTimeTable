package Evolution.EndCondition;

public class NumberOfGenerations implements EndCondition {

    private int numberOfGenerations;

    public NumberOfGenerations(int _numberOfGenerations) {
        if(_numberOfGenerations < 0)
        {
            throw new src.ShowException("Number of generations must be bigger then 0.");
        }

        this.numberOfGenerations = _numberOfGenerations;
    }

    @Override
    public boolean checkCondition(EndConditionGetterClass endConditionGetterClass) {
        if(numberOfGenerations>endConditionGetterClass.getNumberOfGenerations())
            return false;
        else
            return true;
    }
}

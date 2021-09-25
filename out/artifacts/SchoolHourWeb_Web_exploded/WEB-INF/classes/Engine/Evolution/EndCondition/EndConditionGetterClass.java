package Engine.Evolution.EndCondition;


public class EndConditionGetterClass {
    private int NumberOfGenerations;
    private double fitness;
    private int time;
    public EndConditionGetterClass(int numberOfGenerations, double fitness,int time) {
        NumberOfGenerations = numberOfGenerations;
        this.fitness = fitness;
        this.time=time;
    }
    public void update(int numberOfGenerations, double fitness,int time)
    {
        NumberOfGenerations = numberOfGenerations;
        this.fitness = fitness;
        this.time=time;
    }

    public int getNumberOfGenerations() {
        return NumberOfGenerations;
    }

    public double getFitness() {
        return fitness;
    }

    public int getTime(){return time;}


}

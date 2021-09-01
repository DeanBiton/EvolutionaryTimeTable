package JavaFX;

import javafx.util.Pair;

public class ConditionPairs {

    public Integer numberOfGeneration;
    public Double fitness;
    public Integer timeSeconds;

    public ConditionPairs() {
      numberOfGeneration=null;
      fitness=null;
      timeSeconds=null;
    }

    public void setNumberOfGeneration(int nm) {
        numberOfGeneration= nm;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void setTimeSeconds(int timeSeconds) {
        this.timeSeconds = timeSeconds;
    }
}

package Engine.DTO;

import Engine.Evolution.EndCondition.EndConditionGetterClass;

public class DTOAlgorithmContidions {
    private int NumberOfGenerations;
    private double fitness;
    private int time;

    public DTOAlgorithmContidions(EndConditionGetterClass endConditionGetterClass) {
      synchronized (endConditionGetterClass)
      {
          NumberOfGenerations=endConditionGetterClass.getNumberOfGenerations();
          fitness=endConditionGetterClass.getFitness();
          time=endConditionGetterClass.getTime();
      }

    }
}

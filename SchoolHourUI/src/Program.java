import Evolution.EndCondition.ByTime;
import Evolution.EndCondition.EndCondition;
import Evolution.EndCondition.NumberOfGenerations;
import Evolution.Evolutionary;
import Evolution.MySolution.Crossover.AspectOriented;
import Evolution.MySolution.Crossover.Crossover;

import java.util.ArrayList;
import java.util.List;

public class Program {

    public static void main(String[] args) {
        try{
            //SchoolHourSystem.run();


            SchoolHourManager s=new SchoolHourManager();
            s.LoadXML("C:\\Users\\belik\\IdeaProjects\\EvolutionaryTimeTable\\SchoolHourLogic\\src\\Xml\\EX2-small.xml");



            List<EndCondition> endConditions= new ArrayList<>();
            endConditions.add(new NumberOfGenerations(2000));
           // endConditions.add(new ByTime(1));
            s.runEvolutionaryAlgorithm(endConditions,20);
          //  Thread.sleep(5000);
           // s.suspend();
            //Thread.sleep(10000);
           // s.resume();
          //  Thread.sleep(4000);
          //  s.stopAlgorithm();

            //Crossover aspect= new AspectOriented(2, AspectOriented.OrientationType.TEACHER);
            //s.setCrossover(aspect);
           // Thread.sleep(5000);
            //s.resume();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}



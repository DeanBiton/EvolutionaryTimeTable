import Evolution.EndCondition.EndCondition;
import Evolution.EndCondition.NumberOfGenerations;
import Evolution.MySolution.Crossover.AspectOriented;
import Evolution.MySolution.Crossover.Crossover;

import java.util.ArrayList;
import java.util.List;

public class Program {

    public static void main(String[] args) {
        try{
            SchoolHourSystem.run();


           /* SchoolHourManager s=new SchoolHourManager();
            s.LoadXML("C:\\Users\\belik\\Downloads\\EX1-small.xml");

            List<EndCondition> endConditions= new ArrayList<>();
            endConditions.add(new NumberOfGenerations(1000));
            s.runEvolutionaryAlgorithm(endConditions,10);
            Thread.sleep(5000);
            s.suspend();
            Thread.sleep(2000);
            s.resume();
            Thread.sleep(4000);
            s.stopAlgorithm();*/

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



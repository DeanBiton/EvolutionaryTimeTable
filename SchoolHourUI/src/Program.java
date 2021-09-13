import Engine.Evolution.MySolution.TupleGroup;
import Engine.Evolution.Rule;

public class Program {

    public static void main(String[] args) {
        try{
            SchoolHourSystem.run();

          /*  Engine.SchoolHourManager s=new Engine.SchoolHourManager();
            s.LoadXML("C:\\Users\\belik\\IdeaProjects\\EvolutionaryTimeTable\\SchoolHourLogic\\src\\Engine.Xml\\EX2-small.xml");



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
            //s.resume();*/
/*
            Engine.SchoolHourManager s = new Engine.SchoolHourManager();
            s.LoadXML("C:\\Users\\user\\Desktop\\סמסטר קיץ\\תרגילי הקורס\\תרגיל 3\\EX3-small.xml");

            TupleGroup tupleGroup = new TupleGroup(s.data);
            tupleGroup.random();
            double fitness = Rule.RuleType.WorkingHoursPreference.fitnessRuleCalc(tupleGroup);
            System.out.println(fitness);
*/
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    /*
    			<ETT-Teacher id="3">
				<ETT-Name>Alila Maslul</ETT-Name>
				<ETT-Working-Hours>4</ETT-Working-Hours>
				<ETT-Teaching>
					<ETT-Teaches subject-id="3"/>
				</ETT-Teaching>
			</ETT-Teacher>

     */
}



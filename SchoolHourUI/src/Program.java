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

/*            Engine.SchoolHourManager s = new Engine.SchoolHourManager();
            s.LoadXML("C:\\Users\\user\\Desktop\\EX2-small.xml");

            TupleGroup tupleGroup = new TupleGroup(s.data);
            tupleGroup.random();
            double fitness = Rule.RuleType.Sequentiality.fitnessRuleCalc(tupleGroup);
            System.out.println(fitness);*/

        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}



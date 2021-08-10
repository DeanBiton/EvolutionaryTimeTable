package Evolution.MySolution;
import Evolution.HelperFunc;
import Evolution.MySolution.Crossover.Crossover;
import Evolution.MySolution.MyMutation.Flipping;
import Evolution.MySolution.MyMutation.Mutation;
import Evolution.MySolution.MyMutation.MutationSelector;
import Evolution.Rule;
import Xml.JAXBClasses.*;

import java.io.Serializable;
import java.util.*;

public class SchoolHourData implements Serializable {

    private final Map<Integer,Teacher> teachers= new TreeMap<>();
    private final Map<Integer,Classroom> classrooms = new TreeMap<>();
    private final Map<Integer, Subject> subjects=new TreeMap<>() ;
    private final List<Rule> rules = new ArrayList<>();
    private final int  numberOfDays;
    private final int numberOfHoursInADay;
    private final int hardRulesWeight;
    private final List<Mutation> mutations = new ArrayList<>();

    public List<Mutation> getMutations() {
        return mutations;
    }

    public int getHardRulesWeight() {
        return hardRulesWeight;
    }

    public Map<Integer, Teacher> getTeachers() {
        return teachers;
    }

    public Map<Integer, Classroom> getClassrooms() {
        return classrooms;
    }

    public Map<Integer, Subject> getSubjects() {
        return subjects;
    }

    public List<Rule> getRules() { return rules; }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public int getNumberOfHoursInADay() {
        return numberOfHoursInADay;
    }

    private void createSubjects(ETTSubjects ettSubjects)
    {
        for (ETTSubject s : ettSubjects.getETTSubject()) {
            if (null != subjects.putIfAbsent(s.getId(), new Subject(s.getId(), s.getName())))
                throw new RuntimeException("subject with same id");
        }
        if (false == checkOrder(subjects.keySet()))
            throw new RuntimeException("subjects id are not in an ordered sequence starting from 1");

    }

    private void createTeachers(ETTTeachers ettTeachers)
    {
        for (ETTTeacher t : ettTeachers.getETTTeacher()) {
            List<Integer> teacher_subjects = new ArrayList<>();
            for (ETTTeaches teach : t.getETTTeaching().getETTTeaches()) {
                if (teacher_subjects.contains(teach.getSubjectId()))
                    throw new RuntimeException("subject " + teach.getSubjectId() + " already exists in teacher" + t.getId());

                if (false == subjects.containsKey(teach.getSubjectId()))
                    throw new RuntimeException("subject" + teach.getSubjectId() + " doesnt exist in subjects, teacher id=" + t.getId());

                teacher_subjects.add(teach.getSubjectId());
            }
            if (null != teachers.putIfAbsent(t.getId(), new Teacher(t.getId(), t.getETTName(), teacher_subjects)))
                throw new RuntimeException("teachers with same id "+t.getId());

        }
        if (false == checkOrder(teachers.keySet()))
            throw new RuntimeException("teachers id are not in an ordered sequence starting from 1");
    }

    private void createClassrooms(ETTClasses ettClasses)
    {
        for (ETTClass c : ettClasses.getETTClass()) {
            int countHours = 0;
            Map<Integer, Integer> class_sub2weekhrs = new TreeMap<>();
            for (ETTStudy study : c.getETTRequirements().getETTStudy()) {
                countHours = countHours + study.getHours();
                if (countHours > numberOfDays * numberOfHoursInADay)
                    throw new RuntimeException("too many hours in class id" + c.getId());

                if (false == subjects.containsKey(study.getSubjectId()))
                    throw new RuntimeException("subject" + study.getSubjectId() + " doesnt exists in subjects, classroom id=" + c.getId());

                if (null != class_sub2weekhrs.putIfAbsent(study.getSubjectId(), study.getHours()))
                    throw new RuntimeException("subject" + study.getSubjectId() + " already exists in classroom id=" + c.getId());

            }

            if (null != classrooms.putIfAbsent(c.getId(), new Classroom(c.getId(), c.getETTName(), class_sub2weekhrs)))
                throw new RuntimeException("classrooms with same id "+ c.getId());

        }
        if (false == checkOrder(classrooms.keySet()))
            throw new RuntimeException("classrooms id are not in an ordered sequence starting from 1");

    }

    private void createRules(ETTRules ettRules)
    {
        for (ETTRule r : ettRules.getETTRule()) {
            Rule rule;
            try {
               if(Rule.RuleType.Sequentiality == Rule.RuleType.valueOf(r.getETTRuleId()))
               {
                   ArrayList<String> parameters = HelperFunc.getParameters(r.getETTConfiguration());
                   Integer totalHours = Integer.parseInt(parameters.get(0));
                   rule = new Rule(Rule.RuleType.valueOf(r.getETTRuleId()), Rule.RuleImplementationLevel.valueOf(r.getType()), totalHours);
               }
               else
               {
                   rule = new Rule(Rule.RuleType.valueOf(r.getETTRuleId()), Rule.RuleImplementationLevel.valueOf(r.getType()));
               }
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(r.getETTRuleId()+ " - rule doesnt exist");
            }

            if (rules.contains(rule))
                throw new RuntimeException("rule already exist");

            rules.add(rule);
        }
    }

    private void createMutations(ETTMutations ettMutations)
    {
        for(ETTMutation mutation:ettMutations.getETTMutation())
        {
            try{
                MutationSelector ms= MutationSelector.valueOf(mutation.getName());
                mutations.add(ms.create(mutation.getProbability(),mutation.getConfiguration()));
            }catch (IllegalArgumentException e)
            {
                throw new RuntimeException("mutation "+ mutation.getName()+" doesnt exists");
            }

        }
    }

    public SchoolHourData(ETTDescriptor descriptor) throws Exception {
        ETTTimeTable timeTable = descriptor.getETTTimeTable();
        numberOfDays = timeTable.getDays();//days
        if (numberOfDays <= 0)
            throw new Exception("numberOfDays is too small");


        numberOfHoursInADay = timeTable.getHours();//hours
        if (numberOfHoursInADay <= 0)
            throw new Exception("numberOfHoursInADay is too small");

        // keep it in this order
        createSubjects(timeTable.getETTSubjects());
        createTeachers(timeTable.getETTTeachers());
        createClassrooms(timeTable.getETTClasses());
        hardRulesWeight = timeTable.getETTRules().getHardRulesWeight();
        createRules(timeTable.getETTRules());
        createMutations(descriptor.getETTEvolutionEngine().getETTMutations());
    }

    private String getFullName(List<String> names){
        StringBuilder fullName=new StringBuilder();
        for(String name:names)
        {
            fullName.append(name);
            fullName.append(" ");
        }

        fullName.deleteCharAt(fullName.length()-1);
        return fullName.toString();
    }

    private Boolean checkOrder (Set<Integer> set)
    {
        int count=1;
        if(set.isEmpty())
            return false;

        List<Integer> sortedList = new ArrayList<>(set);
        Collections.sort(sortedList);

        for(Integer i:sortedList)
        {
            if(count++!=i)
                return false;
        }
        return true;
    }

    public Tuple createRandomTuple() {
        int randomDay = HelperFunc.getRandomNumber(1, numberOfDays);
        int randomHour = HelperFunc.getRandomNumber(1, numberOfHoursInADay);
        int randomClassroomId = HelperFunc.getRandomNumber(1, classrooms.size());
        Classroom randomClassroom = classrooms.get(randomClassroomId);
        int randomTeacherIndex = HelperFunc.getRandomNumber(1, teachers.size());
        Teacher randomTeacher = teachers.get(randomTeacherIndex);
        int randomSubjectIndex = HelperFunc.getRandomNumber(1, subjects.size());
        Subject randomSubject = subjects.get(randomSubjectIndex);

        return new Tuple(randomDay,randomHour,randomClassroom,randomTeacher,randomSubject);
    }
}

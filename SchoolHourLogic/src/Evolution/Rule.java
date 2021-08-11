package Evolution;
import Evolution.MySolution.*;
import com.sun.org.apache.bcel.internal.generic.RET;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Rule implements Serializable {

    private final RuleType type;
    private final RuleImplementationLevel implementationLevel;

    public Rule(RuleType _type, RuleImplementationLevel _implementationLevel) {
        type = _type;
        implementationLevel = _implementationLevel;
    }

    public Rule(RuleType _type, RuleImplementationLevel _implementationLevel, int _totalHours) {
        type = _type;
        implementationLevel = _implementationLevel;
        type.totalHours = _totalHours;
    }

    public RuleType getType() {
        return type;
    }

    public RuleImplementationLevel getImplementationLevel() {
        return implementationLevel;
    }

    public enum RuleType implements FitnessRules {
        TeacherIsHuman{
            public double fitnessRuleCalc(Evolutionary evolutionary){
                double sumOfTeachersScore = 0;
                TupleGroup group = getTupleGroup(evolutionary);
                int numberOfTeachers = group.getData().getTeachers().size();

                for(Teacher teacher : group.getData().getTeachers().values())
                {
                    int teacherNumberOfTupleCollisions = 0;
                    List<Tuple> teacherTuples = group.getTuples().stream().
                            filter(tuple -> tuple.getTeacher() == teacher).
                            collect(Collectors.toList());

                    for(int i = 1; i <= group.getData().getNumberOfDays(); i++)
                    {
                        for (int j = 1; j <= group.getData().getNumberOfHoursInADay(); j++)
                        {
                            int finalI = i;
                            int finalJ = j;
                            int numberOfTuples = (int) teacherTuples.stream().
                                    filter(tuple -> tuple.getDay() == finalI).
                                    filter(tuple -> tuple.getHour() == finalJ).
                                    count();

                            if(numberOfTuples > 1)
                            {
                                teacherNumberOfTupleCollisions += numberOfTuples;
                            }
                        }
                    }

                    if(teacherTuples.size() != 0)
                    {
                        sumOfTeachersScore += (double) (teacherTuples.size() - teacherNumberOfTupleCollisions)/ teacherTuples.size() * 100;
                    }
                    else
                    {
                        sumOfTeachersScore += 100;
                    }
                }

                return sumOfTeachersScore / numberOfTeachers;
            }
        }
        , Singularity{
            public double fitnessRuleCalc(Evolutionary evolutionary) {
                double sumOfClassroomsScore = 0;
                TupleGroup group = getTupleGroup(evolutionary);
                int numberOfClassrooms = group.getData().getClassrooms().size();

                for(Classroom classroom : group.getData().getClassrooms().values())
                {
                    int classroomNumberOfTupleCollisions = 0;

                    List<Tuple> classroomTuples = group.getTuples().stream().
                            filter(tuple -> tuple.getClassroom() == classroom).
                            collect(Collectors.toList());

                    for(int i = 1; i <= group.getData().getNumberOfDays(); i++)
                    {
                        for (int j = 1; j <= group.getData().getNumberOfHoursInADay(); j++)
                        {
                            int finalI = i;
                            int finalJ = j;
                            int numberOfTuples = (int) classroomTuples.stream().
                                    filter(tuple -> tuple.getDay() == finalI).
                                    filter(tuple -> tuple.getHour() == finalJ).
                                    count();

                            if(numberOfTuples > 1)
                            {
                                classroomNumberOfTupleCollisions += numberOfTuples;
                            }
                        }
                    }

                    if(classroomTuples.size() != 0)
                    {
                        sumOfClassroomsScore += (double) (classroomTuples.size() - classroomNumberOfTupleCollisions)/ classroomTuples.size() * 100;
                    }
                    else
                    {
                        sumOfClassroomsScore += 100;
                    }
                }

                return sumOfClassroomsScore / numberOfClassrooms;
            }
        }
        , Knowledgeable{
            public double fitnessRuleCalc(Evolutionary evolutionary) {
                double sumOfTeachersScore = 0;
                TupleGroup group = getTupleGroup(evolutionary);
                int numberOfTeachers = group.getData().getTeachers().size();

                for(Teacher teacher : group.getData().getTeachers().values())
                {
                    int teacherNumberOfTupleCollisions = 0;
                    List<Tuple> teacherTuples = group.getTuples().stream().
                            filter(tuple -> tuple.getTeacher() == teacher).
                            collect(Collectors.toList());

                    teacherNumberOfTupleCollisions = teacherTuples.stream().mapToInt((tuple) -> {
                        if(!tuple.getTeacher().getAllSubjectsTeaching().contains(tuple.getSubject().getId()))
                            return 1;
                        else
                            return 0;
                    }).sum();

                    if(teacherTuples.size() != 0)
                    {
                        sumOfTeachersScore += (double) (teacherTuples.size() - teacherNumberOfTupleCollisions)/ teacherTuples.size() * 100;
                    }
                    else
                    {
                        sumOfTeachersScore += 100;
                    }
                }

                    return sumOfTeachersScore / numberOfTeachers;
            }
        }
        , Satisfactory{
            public double fitnessRuleCalc(Evolutionary evolutionary){
                double sumOfClassroomsScore = 0;
                TupleGroup group = getTupleGroup(evolutionary);
                int numberOfClassrooms = group.getData().getClassrooms().size();

                for(Classroom classroom : group.getData().getClassrooms().values())
                {
                    int totalDemandedHours = 0;
                    List<Tuple> classroomTuples = group.getTuples().stream().
                            filter(tuple -> tuple.getClassroom() == classroom).
                            collect(Collectors.toList());
                    int totalUnwantedSubjectsHours = (int)classroomTuples.stream()
                            .filter(tuple -> !classroom.getSubjectId2WeeklyHours().containsKey(tuple.getSubject().getId()))
                            .count();

                    List<Pair<Integer, Double>> subjectsHoursDemandedAndTheirScores = new ArrayList<>();

                    for (Integer subjectID : classroom.getSubjectId2WeeklyHours().keySet())
                    {
                        double subjectScore = 0;
                        int subjectHoursDemanded = classroom.getSubjectId2WeeklyHours().get(subjectID);
                        Subject subject = group.getData().getSubjects().get(subjectID);
                        int subjectHoursMet = classroomTuples.stream().filter(tuple -> tuple.getSubject() == subject).
                                collect(Collectors.toList()).size();

                        totalDemandedHours += subjectHoursDemanded;
                        if(subjectHoursDemanded != 0)
                        {
                            if(subjectHoursMet > subjectHoursDemanded)
                                subjectScore = 100 * (double)subjectHoursDemanded / subjectHoursMet;
                            else
                                subjectScore = 100 * (double)subjectHoursMet / subjectHoursDemanded;
                        }

                        subjectsHoursDemandedAndTheirScores.add(new Pair<>(subjectHoursDemanded, subjectScore));
                    }

                    if(totalDemandedHours != 0)
                    {
                        int finalTotalDemandedHours = totalDemandedHours;
                        sumOfClassroomsScore += (double) subjectsHoursDemandedAndTheirScores.stream()
                                .mapToDouble(pair -> pair.getValue() * pair.getKey() )
                                .sum()/ (finalTotalDemandedHours+totalUnwantedSubjectsHours);
                    }
                    else
                    {
                        sumOfClassroomsScore += 100;
                    }
                }

                return sumOfClassroomsScore / numberOfClassrooms;
            }
        }
        , DayOffTeacher{
            public double fitnessRuleCalc(Evolutionary evolutionary) {
                double sumOfTeachersScore = 0;
                TupleGroup group = getTupleGroup(evolutionary);
                int numberOfTeachers = group.getData().getTeachers().size();
                int numberOfDays = group.getData().getNumberOfDays();

                for(Teacher teacher : group.getData().getTeachers().values())
                {
                    Set<Integer> teacherLearningDays = new HashSet<>();
                    group.getTuples().stream().
                            filter(tuple -> tuple.getTeacher() == teacher).
                            forEach(tuple -> teacherLearningDays.add(tuple.getDay()));

                    if(numberOfDays == 1)
                    {
                        sumOfTeachersScore += (teacherLearningDays.size() == 0) ? 100 : 0;
                    }
                    else
                    {
                        sumOfTeachersScore += teacherLearningDays.size() == numberOfDays ?
                                (double)(numberOfDays - 2) * 100 / (numberOfDays - 1) :
                                (double)teacherLearningDays.size() * 100 / (numberOfDays - 1);
                    }
                }

                return sumOfTeachersScore / numberOfTeachers;
            }
        }
        , Sequentiality {
            public double fitnessRuleCalc(Evolutionary evolutionary) {
                double sumOfSubjectsScore = 0;
                TupleGroup group = getTupleGroup(evolutionary);
                int numberOfSubjects = group.getData().getSubjects().size();
                int numberOfDays = group.getData().getNumberOfDays();
                int numberOfHours = group.getData().getNumberOfHoursInADay();

                for(Subject subject : group.getData().getSubjects().values())
                {
                    double sumOfDaysScore = 0;
                    List<Tuple> subjectTuples = group.getTuples().stream().
                            filter(tuple -> tuple.getSubject() == subject).
                            collect(Collectors.toList());

                    for (int day = 1; day <= numberOfDays; day++)
                    {
                        int finalDay = day;
                        int longetsConsecutiveHoursInADay = findLLongetsConsecutiveHoursOfSubject(
                                subjectTuples.stream()
                                        .filter(tuple -> tuple.getDay() == finalDay)
                                        .collect(Collectors.toList()));

                        if((longetsConsecutiveHoursInADay <= totalHours) || longetsConsecutiveHoursInADay == 0)
                        {
                            sumOfDaysScore += 100;
                        }
                        else
                        {
                            sumOfDaysScore += 100 - ((double) (longetsConsecutiveHoursInADay - totalHours) * 100 / (numberOfHours - totalHours));
                        }
                    }

                    sumOfSubjectsScore += sumOfDaysScore / numberOfDays;
                }

                    return sumOfSubjectsScore / numberOfSubjects;
            }

            private int findLLongetsConsecutiveHoursOfSubject(List<Tuple> subjectTuples)
            {
                int longetsHoursInADay = 0;
                int consecutiveHoursInADay = 1;

                subjectTuples = subjectTuples.stream().sorted((o1,o2)-> {
                    if(!o1.getHour().equals(o2.getHour()))
                        return o1.getHour() - o2.getHour();
                    else return 1;
                }).collect(Collectors.toList());

                for(int i = 1; i < subjectTuples.size(); i++)
                {
                    if(subjectTuples.get(i).getHour() - subjectTuples.get(i-1).getHour() == 1)
                    {
                        consecutiveHoursInADay++;
                    }
                    else if(subjectTuples.get(i).getHour() == subjectTuples.get(i-1).getHour())
                    {

                    }
                    else
                    {
                        if(consecutiveHoursInADay > longetsHoursInADay)
                        {
                            longetsHoursInADay = consecutiveHoursInADay;
                        }
                        consecutiveHoursInADay = 1;
                    }
                }

                if(consecutiveHoursInADay > longetsHoursInADay && !subjectTuples.isEmpty())
                {
                    longetsHoursInADay = consecutiveHoursInADay;
                }

                return longetsHoursInADay;
            }
        }
        ;

        int totalHours = 0;

        protected TupleGroup getTupleGroup(Evolutionary evolutionary)
        {
            if(!(evolutionary instanceof TupleGroup))
            {
                throw new IllegalArgumentException("evolutionary is not a Evolution.MySolution.TupleGroup");
            }

            return (TupleGroup) evolutionary;
        }

        @Override
        public double fitnessRuleCalc(Evolutionary evolutionary) {
            return 0;
        }
    }

    public enum RuleImplementationLevel
    {
        Soft, Hard
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return type == rule.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Rule name: ");
        stringBuilder.append(type.toString());
        stringBuilder.append(", ");
        stringBuilder.append(implementationLevel.toString());
        stringBuilder.append(System.lineSeparator());

        return stringBuilder.toString();
    }
}

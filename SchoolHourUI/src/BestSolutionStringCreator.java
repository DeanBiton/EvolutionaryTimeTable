import Engine.DTO.*;

import java.util.*;
import java.util.stream.Collectors;

public class BestSolutionStringCreator {

    public static String getBestSolutionString(SchoolHourSystem.eSolutionViewType viewType, DTOTupleGroupWithFitnessDetails bestSolution)
    {
        StringBuilder stringBuilder = new StringBuilder();

        if(viewType == SchoolHourSystem.eSolutionViewType.RAW)
        {
            bestSolution.getDtoTuples().sort((o1,o2)->{
                if(false==o1.getDay().equals(o2.getDay()))
                    return o1.getDay()-o2.getDay();
                else if(false==o1.getHour().equals(o2.getHour()))
                    return o1.getHour()-o2.getHour();
                else if(false==o1.getClassroom().equals(o2.getClassroom()))
                        return o1.getClassroom().getId()-o2.getClassroom().getId();
                else if(false==o1.getTeacher().equals(o2.getTeacher()))
                    return o1.getTeacher().getNameId()-o2.getTeacher().getNameId();
                else
                    return 0;
            });
            stringBuilder.append(getBestSolutionTuplesString(bestSolution));
        }
        else if(viewType == SchoolHourSystem.eSolutionViewType.TEACHER)
        {

            for(int teacherID = 1; teacherID <= bestSolution.getSchoolHoursData().getTeachers().size(); teacherID++)
            {
                stringBuilder.append(System.lineSeparator() + "Teacher ID: " + teacherID + " <Classroom ID, Subject ID>");
                int finalTeacherID = teacherID;
                List<DTOTuple> teacherTuples = bestSolution.getDtoTuples().stream().filter(dtoTuple -> dtoTuple.getTeacher().getNameId() == finalTeacherID).collect(Collectors.toList());
                stringBuilder.append(getTimeTableString(bestSolution.getSchoolHoursData().getNumberOfDays(), bestSolution.getSchoolHoursData().getNumberOfHoursInADay(), teacherTuples, true));
            }
        }
        else
        {

            for(int classroomID = 1; classroomID <= bestSolution.getSchoolHoursData().getClassrooms().size(); classroomID++)
            {
                stringBuilder.append(System.lineSeparator() + "Classroom ID: " + classroomID + " <Teacher ID, Subject ID>" + System.lineSeparator());
                int finalClassroomID = classroomID;
                List<DTOTuple> classroomTuples = bestSolution.getDtoTuples().stream().filter(dtoTuple -> dtoTuple.getClassroom().getId() == finalClassroomID).collect(Collectors.toList());
                stringBuilder.append(getTimeTableString(bestSolution.getSchoolHoursData().getNumberOfDays(), bestSolution.getSchoolHoursData().getNumberOfHoursInADay(), classroomTuples, false));
            }
        }

        stringBuilder.append(getFitnessDetailsString(bestSolution));
        return stringBuilder.toString();
    }

    private static String getBestSolutionTuplesString(DTOTupleGroupWithFitnessDetails tupleGroup)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("TupleGroup: ");
        stringBuilder.append(System.lineSeparator());

        for (DTOTuple tuple: tupleGroup.getDtoTuples())
        {
            stringBuilder.append("<Day=");
            stringBuilder.append(tuple.getDay());
            stringBuilder.append(", Hour=");
            stringBuilder.append(tuple.getHour());
            stringBuilder.append(", Classroom ID: ");
            stringBuilder.append(tuple.getClassroom().getId());
            stringBuilder.append(", Teacher ID: ");
            stringBuilder.append(tuple.getTeacher().getNameId());
            stringBuilder.append(", Subject ID: ");
            stringBuilder.append(tuple.getSubject().getId());
            stringBuilder.append(">");
            stringBuilder.append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }

    private static String getTimeTableString(int days, int hours, List<DTOTuple> dtoTuples, boolean byTeacher)
    {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder columnSeparator = new StringBuilder();
        StringBuilder columnValues = new StringBuilder();
        StringBuilder columnHeader = new StringBuilder();

        for(int i = 0; i <= days; ++i)
        {
            columnSeparator.append(i == 0 ? "+-------+" : "-------+");
            columnHeader.append(i == 0 ? "|  H/D  |" : String.format("   %d   |", i));
        }
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append(columnSeparator);
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append(columnHeader);
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append(columnSeparator);
        stringBuilder.append(System.lineSeparator());

        for(int j = 1; j <= hours; ++j)
        {
            Map<Integer, List<DTOTuple>> dayToTuplesOfHour = new HashMap<>();
            for(int i = 1; i <= days; ++i)
            {
                int thisDay = i;
                int thisHour = j;
                dayToTuplesOfHour.put(i, dtoTuples.stream()
                        .filter(tuple -> tuple.getDay().equals(thisDay) && tuple.getHour().equals(thisHour))
                        .sorted(Comparator.comparingInt(tuple ->
                                byTeacher ? tuple.getTeacher().getNameId() : tuple.getClassroom().getId()))
                        .collect(Collectors.toList()));
            }

            boolean firstLoop = true;
            do
            {
                for(int i = 0; i <= days; ++i)
                {
                    columnValues.append(i == 0 ? (firstLoop ? String.format("|   %d   |", j) : "|       |") :
                            (dayToTuplesOfHour.get(i).size() == 0 ? "       |" :
                                    String.format(" %2d,%2d |", byTeacher ?
                                                    dayToTuplesOfHour.get(i).get(0).getClassroom().getId() :
                                                    dayToTuplesOfHour.get(i).get(0).getTeacher().getNameId(),
                                            dayToTuplesOfHour.get(i).get(0).getSubject().getId())));
                    if(i != 0 && dayToTuplesOfHour.get(i).size() != 0)
                        dayToTuplesOfHour.get(i).remove(0);
                }
                firstLoop = false;
                stringBuilder.append(columnValues);
                stringBuilder.append(System.lineSeparator());
                columnValues = new StringBuilder();
            } while(dayToTuplesOfHour.values().stream().anyMatch(lst -> lst.size() != 0));

            stringBuilder.append(columnSeparator);
            stringBuilder.append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }

    private static String getFitnessDetailsString(DTOTupleGroupWithFitnessDetails dtoTupleGroupWithFitnessDetails)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("----------------------------------------------");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("fitness score = ");
        stringBuilder.append(String.format("%.3f", dtoTupleGroupWithFitnessDetails.getFitness()));
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("Rules and their scores:");
        stringBuilder.append(System.lineSeparator());
        for (DTORule dtoRule : dtoTupleGroupWithFitnessDetails.getRules())
        {
            stringBuilder.append("Rule name: ");
            stringBuilder.append(dtoRule.getRuleType().name());
            stringBuilder.append(", ");
            stringBuilder.append(dtoRule.getRuleImplementationLevel().name());
            stringBuilder.append(", Score: ");
            stringBuilder.append(String.format("%.3f", dtoRule.getScore()));
            stringBuilder.append(System.lineSeparator());
        }

        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("Hard rules average: ");
        stringBuilder.append(dtoTupleGroupWithFitnessDetails.getHardRulesAverage());
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("Soft rules average: ");
        stringBuilder.append(dtoTupleGroupWithFitnessDetails.getSoftRulesAverage());
        stringBuilder.append(System.lineSeparator());

        return stringBuilder.toString();
    }
}

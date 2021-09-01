import Engine.DTO.*;

import java.util.Map;

public class DataAndAlgorithmSettingsStringCreator {

    public static String getDataAndAlgorithmSettingsString(DTODataAndAlgorithmSettings dtoDataAndAlgorithmSettings)
    {
        StringBuilder stringBuilder = new StringBuilder();
        DTOSchoolHoursData dtoData = dtoDataAndAlgorithmSettings.getDtoSchoolHoursData();
        DTOEvolutionaryAlgorithmSettings dtoEvolutionaryAlgorithmSettings = dtoDataAndAlgorithmSettings.getDtoEvolutionaryAlgorithmSettings();

        stringBuilder.append(getTimeTableDetailsString(dtoData));
        String string = getAlgorithmSettingsString(dtoEvolutionaryAlgorithmSettings);
        stringBuilder.append(string);

        return stringBuilder.toString();
    }

    private static String getTimeTableDetailsString(DTOSchoolHoursData dtoData)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Time Table Details:");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append(getFrameLine());
        stringBuilder.append(getStringOfSubjects(dtoData));
        stringBuilder.append(getFrameLine());
        stringBuilder.append(getStringOfTeachers(dtoData));
        stringBuilder.append(getFrameLine());
        stringBuilder.append(getStringOfClassrooms(dtoData));
        stringBuilder.append(getFrameLine());
        stringBuilder.append(getStringOfRules(dtoData));
        stringBuilder.append(getFrameLine());

        return stringBuilder.toString();
    }

    private static String getStringOfSubjects(DTOSchoolHoursData dtoData)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("1. Subjects: ");
        stringBuilder.append(System.lineSeparator());
        if(!dtoData.getSubjects().isEmpty())
        {
            dtoData.getSubjects().values().forEach(dtoSubject -> {stringBuilder.append(dtoSubject.toString()); stringBuilder.append(System.lineSeparator());});
        }
        else
        {
            stringBuilder.append("None");
        }

        return stringBuilder.toString();
    }

    private static String getStringOfTeachers(DTOSchoolHoursData dtoData)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("2. Teachers: ");
        stringBuilder.append(System.lineSeparator());
        if(!dtoData.getTeachers().isEmpty())
        {
            dtoData.getTeachers().values().forEach(dtoTeacher -> {

                stringBuilder.append(dtoTeacher.toString());
                stringBuilder.append(System.lineSeparator());
                stringBuilder.append("Teaching Subjects: ");
                stringBuilder.append(System.lineSeparator());
                for (int subjectID : dtoTeacher.getAllSubjectsTeaching())
                {
                    DTOSubject subject = dtoData.getSubjects().get(subjectID);
                    stringBuilder.append(subject.toString());
                    stringBuilder.append(System.lineSeparator());
                }

                stringBuilder.append(System.lineSeparator());
            });
        }
        else
        {
            stringBuilder.append("None");
        }

        return stringBuilder.toString();
    }

    private static String getStringOfClassrooms(DTOSchoolHoursData dtoData)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("3. Classrooms: ");
        stringBuilder.append(System.lineSeparator());
        if(!dtoData.getClassrooms().isEmpty()) {
            dtoData.getClassrooms().values().forEach(dtoClassroom -> {
                stringBuilder.append(dtoClassroom.toString());
                stringBuilder.append(System.lineSeparator());
                stringBuilder.append("Subjects and their weekly hours:");
                stringBuilder.append(System.lineSeparator());
                Map<Integer, Integer> SubjectId2WeeklyHours = dtoClassroom.getSubjectId2WeeklyHours();
                for (int subjectID : SubjectId2WeeklyHours.keySet()) {
                    DTOSubject dtoSubject = dtoData.getSubjects().get(subjectID);
                    stringBuilder.append(dtoSubject.toString());
                    int weeklyHours = SubjectId2WeeklyHours.get(subjectID);
                    stringBuilder.append(", Weekly hours:");
                    stringBuilder.append(weeklyHours);
                    stringBuilder.append(System.lineSeparator());
                }
                stringBuilder.append(System.lineSeparator());
            });
        }
        else
        {
            stringBuilder.append("None");
        }

        return stringBuilder.toString();
    }

    private static String getStringOfRules(DTOSchoolHoursData dtoData)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("4. Rules:");
        stringBuilder.append(System.lineSeparator());
        for(DTORule dtoRule : dtoData.getRules())
        {
            stringBuilder.append(dtoRule.toString());
        }

        stringBuilder.append(System.lineSeparator());

        return stringBuilder.toString();
    }

    private static String getAlgorithmSettingsString(DTOEvolutionaryAlgorithmSettings dtoEvolutionaryAlgorithmSettings)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Algorithm Settings:");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("1. Population size: ");
        stringBuilder.append(dtoEvolutionaryAlgorithmSettings.getInitialPopulation());
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("2. Selection technique: ");
        stringBuilder.append(DTOSelection.getDTOSelectionToString(dtoEvolutionaryAlgorithmSettings.getDtoSelection()));
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("3. Crossover type: ");
        String string = DTOCrossover.getDTOCrossoverToString(dtoEvolutionaryAlgorithmSettings.getDtoCrossover());
        stringBuilder.append(string);
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("4. ");
        stringBuilder.append(dtoEvolutionaryAlgorithmSettings.getDtoMutations().toString());
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append(getFrameLine());

        return stringBuilder.toString();
    }

    private static String getFrameLine()
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("---------------------------------------------------");
        stringBuilder.append(System.lineSeparator());

        return stringBuilder.toString();
    }
}

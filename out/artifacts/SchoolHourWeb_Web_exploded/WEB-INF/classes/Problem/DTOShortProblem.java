package Problem;

import Engine.DTO.DTOSchoolHoursData;
import Engine.Evolution.MySolution.SchoolHourData;
import Engine.Evolution.Rule;

public class DTOShortProblem {

    private int numberOfTriedUsers;
    private String uploadUser;
    private double maxFitness;
    int NumberOfDays;
    int NumberOfHours;
    int NumberOfClasses;
    int NumberOfTeachers;
    int NumberOfSubjects;
    int NumberOfHardRules;
    int NumberOfSoftRules;

    public DTOShortProblem(TimeTableProblem timeTableProblem) {
        this.uploadUser=timeTableProblem.getUploadUser();
        this.maxFitness=timeTableProblem.getMaxFitness();
        DTOSchoolHoursData DTOschoolHourData=timeTableProblem.getSchoolHourManager().getDataAndAlgorithmSettings().getDtoSchoolHoursData();
        this.NumberOfDays=DTOschoolHourData.getNumberOfDays();
        this.NumberOfHours=DTOschoolHourData.getNumberOfHoursInADay();
        this.NumberOfClasses=DTOschoolHourData.getClassrooms().size();
        this.NumberOfTeachers=DTOschoolHourData.getTeachers().size();
        this.NumberOfSubjects=DTOschoolHourData.getSubjects().size();
        this.NumberOfHardRules= (int) DTOschoolHourData.getRules().stream().filter(r->r.getImplementationLevel().equals(Rule.RuleImplementationLevel.Hard)).count();
        this.NumberOfSoftRules=DTOschoolHourData.getRules().size()-this.NumberOfHardRules;

    }
}

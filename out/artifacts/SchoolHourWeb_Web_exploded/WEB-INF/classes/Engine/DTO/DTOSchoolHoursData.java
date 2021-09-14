package Engine.DTO;

import Engine.Evolution.MySolution.SchoolHourData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DTOSchoolHoursData {

    private final SchoolHourData schoolHourData;
    private final Map<Integer, DTOTeacher> teachers= new TreeMap<>();
    private final Map<Integer, DTOClassroom> classrooms = new TreeMap<>();
    private final Map<Integer, DTOSubject> subjects=new TreeMap<>() ;
    private final List<DTORule> rules = new ArrayList<>();
    private final int  numberOfDays;
    private final int numberOfHoursInADay;
    private final int hardRulesWeight;
    private final DTOMutations dtoMutations;

    public DTOSchoolHoursData(SchoolHourData schoolHourData) {
        this.schoolHourData= schoolHourData;

        schoolHourData.getTeachers().entrySet().forEach(t-> teachers.put(new Integer(t.getKey()),new DTOTeacher(t.getValue())));
        schoolHourData.getClassrooms().entrySet().forEach(c->classrooms.put(new Integer(c.getKey()),new DTOClassroom(c.getValue())));
        schoolHourData.getSubjects().entrySet().forEach(s->subjects.put(new Integer(s.getKey()),new DTOSubject(s.getValue())));
        schoolHourData.getRules().forEach(r->rules.add(new DTORule(r)));

        dtoMutations=new DTOMutations(schoolHourData.getMutations());
        numberOfDays=schoolHourData.getNumberOfDays();
        numberOfHoursInADay=schoolHourData.getNumberOfHoursInADay();
        hardRulesWeight=schoolHourData.getHardRulesWeight();
    }

    public Map<Integer, DTOTeacher> getTeachers() {
        return teachers;
    }

    public Map<Integer, DTOClassroom> getClassrooms() {
        return classrooms;
    }

    public Map<Integer, DTOSubject> getSubjects() {
        return subjects;
    }

    public List<DTORule> getRules() {
        return rules;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public int getNumberOfHoursInADay() {
        return numberOfHoursInADay;
    }

    public int getHardRulesWeight() {
        return hardRulesWeight;
    }

    public DTOMutations getDtoMutations() {
        return dtoMutations;
    }

    @Override
    public String toString() {
        return schoolHourData.toString();
    }
}

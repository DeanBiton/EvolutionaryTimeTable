package Engine.DTO;

import Engine.Evolution.MySolution.TupleGroup;
import java.util.ArrayList;
import java.util.List;

public class DTOTupleGroup {
    protected TupleGroup tupleGroup;
    protected List<DTOTuple> dtoTuples;
    protected double fitness;
    public DTOSchoolHoursData schoolHoursData;

    public DTOTupleGroup(TupleGroup tupleGroup, DTOSchoolHoursData schoolHoursData) {
        this.tupleGroup = tupleGroup;
        fitness = tupleGroup.fitness();
        dtoTuples = new ArrayList<>(tupleGroup.getTuples().size());
        tupleGroup.getTuples().forEach(t -> {

                    Integer dtoDay = t.getDay();
                    Integer dtoHour = t.getHour();
                    DTOSubject dtoSubject = schoolHoursData.getSubjects().get(t.getSubject().getId());
                    DTOTeacher dtoTeacher = schoolHoursData.getTeachers().get(t.getTeacher().getId());
                    DTOClassroom dtoClassroom = schoolHoursData.getClassrooms().get(t.getClassroom().getId());

                    dtoTuples.add(new DTOTuple(dtoDay, dtoHour, dtoClassroom, dtoTeacher, dtoSubject));
                }

        );
        this.schoolHoursData = schoolHoursData;
    }

    public double getFitness() {
        return fitness;
    }

    public List<DTOTuple> getDtoTuples() {
        return dtoTuples;
    }

    public DTOSchoolHoursData getSchoolHoursData() {
        return schoolHoursData;
    }

    public int getnumberoftuples()
    {
        return tupleGroup.getTuples().size();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder= new StringBuilder();
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("TupleGroup:");
        stringBuilder.append(System.lineSeparator());

        for (DTOTuple tuple:dtoTuples)
        {
            stringBuilder.append(tuple);
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }
}
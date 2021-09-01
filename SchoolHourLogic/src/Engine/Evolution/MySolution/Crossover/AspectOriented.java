package Engine.Evolution.MySolution.Crossover;

import Engine.Evolution.MySolution.Classroom;
import Engine.Evolution.MySolution.Teacher;
import Engine.Evolution.MySolution.Tuple;
import Engine.Evolution.MySolution.TupleGroup;

import java.util.ArrayList;
import java.util.List;

public class AspectOriented extends Crossover{

    private OrientationType orientationType;

    public enum OrientationType
    {
        TEACHER, CLASS
    }

    public AspectOriented(int _numberOfSeparators, OrientationType _orientationType) {
        super(_numberOfSeparators);
        orientationType = _orientationType;
    }

    @Override
    public List<List<Tuple>> crossoverInitialize(TupleGroup parent) {
        int DH = parent.getData().getNumberOfDays()*parent.getData().getNumberOfHoursInADay();
        List<List<Tuple>> tupleLists = new ArrayList<>(DH);
        List<Tuple> parentTuples = new ArrayList<>();

        parentTuples.addAll(parent.getTuples());
        for(int i = 0; i < DH; i++)
        {
            List<Tuple> lst = new ArrayList<>();
            tupleLists.add(lst);
        }

        parentTuples.sort((tuple1, tuple2) -> {
            int compare;
            if(orientationType == OrientationType.TEACHER)
                compare = teacherComparator(tuple1.getTeacher(),tuple2.getTeacher());
            else
                compare = classroomComparator(tuple1.getClassroom(), tuple2.getClassroom());

            return compare;
        });

        int location = 0;

        for(Tuple tuple : parentTuples)
        {
            tupleLists.get(location).add(tuple);
            location++;

            if(location == DH)
            {
                location = 0;
            }
        }

        return tupleLists;
    }

    @Override
    public String getName() {
        return "AspectOriented";
    }

    public OrientationType getOrientationType() {
        return orientationType;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("AspectOriented");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("Number of separators: ");
        stringBuilder.append(numberOfSeparators);
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("Orientation Type: ");
        stringBuilder.append(orientationType.name());
        stringBuilder.append(System.lineSeparator());

        return stringBuilder.toString();
    }

    public int teacherComparator(Teacher teacher1, Teacher teacher2)
    {
        if(teacher1 == null || teacher2 == null)
        {
            throw new RuntimeException("At least 1 teacher is null");
        }

        return teacher1.getId() - teacher2.getId();
    }

    public int classroomComparator(Classroom classroom1, Classroom classroom2)
    {
        if(classroom1 == null || classroom2 == null)
        {
            throw new RuntimeException("At least 1 teacher is null");
        }

        return classroom1.getId() - classroom2.getId();
    }
}

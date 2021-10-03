package Engine.DTO;

import Engine.Evolution.MySolution.Teacher;

import java.util.ArrayList;
import java.util.List;

public class DTOTeacher {

    private final Teacher teacher;

    public DTOTeacher(Teacher _teacher) {
        teacher=_teacher;
    }

    public final int getNameId() {
        return teacher.getId();
    }

    public final String getName(){
        return teacher.getName();
    }

    public final List<Integer> getAllSubjectsTeaching(){

        List<Integer> lst= new ArrayList<>(teacher.getAllSubjectsTeaching().size());
        teacher.getAllSubjectsTeaching().forEach(t->lst.add(new Integer(t)));
          return lst;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOTeacher that = (DTOTeacher) o;
        return teacher.equals(that.teacher);
    }

    @Override
    public int hashCode() {
        return teacher.hashCode();
    }

    @Override
    public String toString() {
        return teacher.toString();    }
}

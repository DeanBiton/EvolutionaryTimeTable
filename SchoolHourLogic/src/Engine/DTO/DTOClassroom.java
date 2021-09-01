package Engine.DTO;

import Engine.Evolution.MySolution.Classroom;
import java.util.*;

public class DTOClassroom {
    private final Classroom classroom;

    public DTOClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public final int getId() {
        return classroom.getId();
    }

    public final String getName() {
        return classroom.getName();
    }

    public final Map<Integer, Integer> getSubjectId2WeeklyHours() {

         return Collections.unmodifiableMap(classroom.getSubjectId2WeeklyHours());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOClassroom that = (DTOClassroom) o;
        return Objects.equals(classroom, that.classroom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classroom);
    }

    @Override
    public String toString() {
        return classroom.toString();
    }
}

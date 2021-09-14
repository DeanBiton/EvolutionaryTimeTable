package Engine.Evolution.MySolution;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class Classroom implements Serializable {

    private final int id;
    private final String name;
    private final Map<Integer, Integer> SubjectId2WeeklyHours;

    Classroom(int _id, String _name, Map<Integer, Integer> _SubjectId2WeeklyHours) {
        this.id = _id;
        this.name = _name;
        this.SubjectId2WeeklyHours = _SubjectId2WeeklyHours;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, Integer> getSubjectId2WeeklyHours() {
        return SubjectId2WeeklyHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Classroom classroom = (Classroom) o;
        return id == classroom.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Classroom ID: ");
        stringBuilder.append(id);

        return stringBuilder.toString();
    }
}

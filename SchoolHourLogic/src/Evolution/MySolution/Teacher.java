package Evolution.MySolution;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Teacher implements Serializable {
    private final int id;
    private final String name;
    private final List<Integer> teaching;

    Teacher(int _id, String _name, List<Integer> _teaching) {
        this.id = _id;
        this.name = _name;
        this.teaching = _teaching;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getAllSubjectsTeaching() {
        return teaching;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return id == teacher.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Teacher ID: ");
        stringBuilder.append(id);

        return stringBuilder.toString();
    }
}

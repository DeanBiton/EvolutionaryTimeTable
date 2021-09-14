package Engine.Evolution.MySolution;

import java.io.Serializable;

public class Subject implements Serializable {
    private final int id;
    private final String name;

    Subject(int _id, String _name)
    {
        this.id = _id;
        this.name = _name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Subject ID: ");
        stringBuilder.append(id);
        stringBuilder.append(", Subject Name: ");
        stringBuilder.append(name);

        return stringBuilder.toString();
    }
}

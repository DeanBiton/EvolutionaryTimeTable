package DTO;

import Evolution.MySolution.Subject;

import java.util.Objects;

public class DTOSubject {
    private final Subject subject;

    public DTOSubject(Subject subject) {
        this.subject = subject;
    }

    public final int getId() {
        return subject.getId();
    }

    public final String getName() {
        return subject.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOSubject subject1 = (DTOSubject) o;
        return subject.equals(subject1.subject);
    }

    @Override
    public int hashCode() {
        return subject.hashCode();
    }

    @Override
    public String toString() {
        return subject.toString();
    }
}

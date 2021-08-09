package Evolution.MySolution;

import java.io.Serializable;

public class Tuple implements Serializable {

    private Integer day;
    private Integer hour;
    private Classroom classroom;
    private Teacher teacher;
    private Subject subject;

    public Tuple(Integer _day, Integer _hour, Classroom _classroom, Teacher _teacher, Subject _subject) {
        day = _day;
        hour = _hour;
        classroom = _classroom;
        teacher = _teacher;
        subject = _subject;
    }

    public Tuple(Tuple other)
    {
        this(other.getDay(),other.getHour(),other.getClassroom(),other.getTeacher(),other.getSubject());
    }

    public Integer getDay() {
        return day;
    }

    public Integer getHour() {
        return hour;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "Day=" + day +
                ", Hour=" + hour +
                ", " + classroom +
                ", " + teacher +
                ", " + subject +
                '}';
    }
}

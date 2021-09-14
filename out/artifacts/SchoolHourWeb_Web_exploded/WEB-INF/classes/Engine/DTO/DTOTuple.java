package Engine.DTO;

import Engine.Evolution.MySolution.Tuple;

public class DTOTuple {

    private Integer day;
    private Integer hour;
    private DTOClassroom classroom;
    private DTOTeacher teacher;
    private DTOSubject subject;

    public DTOTuple( Integer day, Integer hour, DTOClassroom classroom, DTOTeacher teacher, DTOSubject subject) {
        this.day = day;
        this.hour = hour;
        this.classroom = classroom;
        this.teacher = teacher;
        this.subject = subject;
    }

    public DTOTuple(Tuple tuple) {
        this.classroom=new DTOClassroom(tuple.getClassroom());
        this.teacher=new DTOTeacher(tuple.getTeacher());
        this.subject=new DTOSubject(tuple.getSubject());
        this.day= new Integer(tuple.getDay());
        this.hour=new Integer(tuple.getHour());
    }

    public Integer getDay() {
        return day;
    }

    public Integer getHour() {
        return hour;
    }

    public DTOClassroom getClassroom() {
        return classroom;
    }

    public DTOTeacher getTeacher() {
        return teacher;
    }

    public DTOSubject getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return "<" +
                "Day=" + day +
                ", Hour=" + hour +
                ", " + classroom +
                ", " + teacher +
                ", " + subject +
                '>';
    }
}

package ba.unsa.etf.rpr.projekat;

import java.util.Objects;

public class Curriculum {
    private int id;
    private Course course;
    private Semester semester;
    private Subject subject;
    private String requiredSubject;

    public Curriculum(int id, Course course, Semester semester, Subject subject, String requiredSubject) {
        this.id = id;
        this.course = course;
        this.semester = semester;
        this.subject = subject;
        this.requiredSubject = requiredSubject;
    }

    public Curriculum() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getRequiredSubject() {
        return requiredSubject;
    }

    public void setRequiredSubject(String requiredSubject) {
        this.requiredSubject = requiredSubject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curriculum that = (Curriculum) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

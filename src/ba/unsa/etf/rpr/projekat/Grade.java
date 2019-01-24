package ba.unsa.etf.rpr.projekat;

import java.util.Objects;

public class Grade {
    private int id;
    private Student student;
    private Subject subject;
    private float points;
    private int score;

    public Grade(int id, Student student, Subject subject, float points, int score) {
        this.id = id;
        this.student = student;
        this.subject = subject;
        this.points = points;
        this.score = score;
    }

    public Grade() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade = (Grade) o;
        return id == grade.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

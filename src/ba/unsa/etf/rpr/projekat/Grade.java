package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;
import java.util.Objects;

public class Grade {
    private int id;
    private Student student;
    private Subject subject;
    private float points;
    private int score;
    private LocalDate gradeDate;
    private Professor professor;

    public Grade(int id, Student student, Subject subject, float points, int score, LocalDate gradeDate, Professor professor) {
        this.id = id;
        this.student = student;
        this.subject = subject;
        this.points = points;
        this.score = score;
        this.gradeDate = gradeDate;
        this.professor = professor;
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

    public LocalDate getGradeDate() {
        return gradeDate;
    }

    public void setGradeDate(LocalDate gradeDate) {
        this.gradeDate = gradeDate;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
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

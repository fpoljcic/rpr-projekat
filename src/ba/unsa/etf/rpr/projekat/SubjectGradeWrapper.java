package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;

public class SubjectGradeWrapper {
    // Subject attributes
    private int idSubject;
    private String name;
    private String code;
    private int ects;
    private Professor professor;
    private Subject reqSubject;

    // Grade attributes
    private int idGrade;
    private Student student;
    private Subject subject;
    private float points;
    private int score;
    private LocalDate gradeDate;
    private Professor professorGrade;

    public SubjectGradeWrapper(Subject subject, Grade grade) {
        if (subject != null) {
            idSubject = subject.getId();
            name = subject.getName();
            code = subject.getCode();
            ects = subject.getEcts();
            professor = subject.getProfessor();
            reqSubject = subject.getReqSubject();
        }

        if (grade != null) {
            idGrade = grade.getId();
            student = grade.getStudent();
            this.subject = grade.getSubject();
            points = grade.getPoints();
            score = grade.getScore();
            gradeDate = grade.getGradeDate();
            professorGrade = grade.getProfessor();
        }
    }

    public SubjectGradeWrapper() {
    }

    public int getIdSubject() {
        return idSubject;
    }

    public void setIdSubject(int idSubject) {
        this.idSubject = idSubject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getEcts() {
        return ects;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Subject getReqSubject() {
        return reqSubject;
    }

    public void setReqSubject(Subject reqSubject) {
        this.reqSubject = reqSubject;
    }

    public int getIdGrade() {
        return idGrade;
    }

    public void setIdGrade(int idGrade) {
        this.idGrade = idGrade;
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

    public Professor getProfessorGrade() {
        return professorGrade;
    }

    public void setProfessorGrade(Professor professorGrade) {
        this.professorGrade = professorGrade;
    }
}

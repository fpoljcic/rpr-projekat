package ba.unsa.etf.rpr.projekat;

import java.util.Objects;

public class Subject {
    private int id;
    private String name;
    private String code;
    private int ects;
    private Professor professor;
    private Subject reqSubject;


    public Subject(int id, String name, String code, int ects, Professor professor, Subject reqSubject) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.ects = ects;
        this.professor = professor;
        this.reqSubject = reqSubject;
    }

    public Subject() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return id == subject.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

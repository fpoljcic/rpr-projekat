package ba.unsa.etf.rpr.projekat;

public class Subject {
    private int id;
    private String name;
    private String code;
    private int ects;
    private Professor professor;


    public Subject(int id, String name, String code, int ects, Professor professor) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.ects = ects;
        this.professor = professor;
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
}

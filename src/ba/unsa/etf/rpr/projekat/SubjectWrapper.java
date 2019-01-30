package ba.unsa.etf.rpr.projekat;

public class SubjectWrapper {
    // Subject attributes
    private int idSubject;
    private String name;
    private String code;
    private int ects;
    private Professor professor;
    private Subject reqSubject;

    private int noStudents;

    public SubjectWrapper() {
    }

    public SubjectWrapper(Subject subject, int noStudents) {
        if (subject != null) {
            idSubject = subject.getId();
            name = subject.getName();
            code = subject.getCode();
            ects = subject.getEcts();
            professor = subject.getProfessor();
            reqSubject = subject.getReqSubject();
        }
        this.noStudents = noStudents;
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

    public int getNoStudents() {
        return noStudents;
    }

    public void setNoStudents(int noStudents) {
        this.noStudents = noStudents;
    }
}

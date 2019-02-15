package ba.unsa.etf.rpr.projekat;

public class SubjectWrapper {
    // Subject attributes
    private int idSubject;
    private String name;
    private String code;
    private int ects;
    private Professor professor;
    private Subject reqSubject;

    private float avgGrade;
    private int noStudentsGraded;
    private int noStudentsNotGraded;

    public SubjectWrapper() {
    }

    public SubjectWrapper(Subject subject, int noStudentsGraded, int noStudentsNotGraded, float avgGrade) {
        if (subject != null) {
            idSubject = subject.getId();
            name = subject.getName();
            code = subject.getCode();
            ects = subject.getEcts();
            professor = subject.getProfessor();
            reqSubject = subject.getReqSubject();
        }
        this.noStudentsGraded = noStudentsGraded;
        this.noStudentsNotGraded = noStudentsNotGraded;
        this.avgGrade = avgGrade;
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
        return noStudentsGraded + noStudentsNotGraded;
    }

    public float getAvgGrade() {
        return avgGrade;
    }

    public void setAvgGrade(float avgGrade) {
        this.avgGrade = avgGrade;
    }

    public int getNoStudentsGraded() {
        return noStudentsGraded;
    }

    public void setNoStudentsGraded(int noStudentsGraded) {
        this.noStudentsGraded = noStudentsGraded;
    }

    public int getNoStudentsNotGraded() {
        return noStudentsNotGraded;
    }

    public void setNoStudentsNotGraded(int noStudentsNotGraded) {
        this.noStudentsNotGraded = noStudentsNotGraded;
    }

    public String getPercentPassed() {
        return Math.round(noStudentsGraded * 10000f / (noStudentsGraded + noStudentsNotGraded)) / 100f + "%";
    }
}

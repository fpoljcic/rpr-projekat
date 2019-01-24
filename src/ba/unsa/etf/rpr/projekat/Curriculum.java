package ba.unsa.etf.rpr.projekat;

public class Curriculum {
    private int id;
    private Course course;
    private Semester semester;
    private Subject mainSubject;
    private Subject secondarySubject;

    public Curriculum(int id, Course course, Semester semester, Subject mainSubject, Subject secondarySubject) {
        this.id = id;
        this.course = course;
        this.semester = semester;
        this.mainSubject = mainSubject;
        this.secondarySubject = secondarySubject;
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

    public Subject getMainSubject() {
        return mainSubject;
    }

    public void setMainSubject(Subject mainSubject) {
        this.mainSubject = mainSubject;
    }

    public Subject getSecondarySubject() {
        return secondarySubject;
    }

    public void setSecondarySubject(Subject secondarySubject) {
        this.secondarySubject = secondarySubject;
    }
}

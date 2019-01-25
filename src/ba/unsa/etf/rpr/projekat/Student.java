package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;
import java.util.Objects;

public class Student extends Person {
    private LocalDate birthDate;
    private Semester semester;
    private Course course;
    private LocalDate pauseDate;

    public Student(int id, String firstName, String lastName, String jmbg, String address, String email, Login login, LocalDate birthDate, Semester semester, Course course, LocalDate pauseDate) {
        super(id, firstName, lastName, jmbg, address, email, login);
        this.birthDate = birthDate;
        this.semester = semester;
        this.course = course;
        this.pauseDate = pauseDate;
    }

    public Student() {
        super();
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getPauseDate() {
        return pauseDate;
    }

    public void setPauseDate(LocalDate pauseDate) {
        this.pauseDate = pauseDate;
    }
}

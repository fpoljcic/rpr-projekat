package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;

public class StudentGradeSubjectWrapper {
    // Person attributes
    private int id;
    private String firstName;
    private String lastName;
    private String jmbg;
    private String address;
    private String email;
    private Login login;

    // Student attributes
    private LocalDate birthDate;
    private Semester semester;
    private Course course;
    private LocalDate pauseDate;

    // Grade attributes
    private float points;

    // Subject attributes
    private String subjectName;

    public StudentGradeSubjectWrapper() {
    }

    public StudentGradeSubjectWrapper(Student student, Grade grade) {
        if (student != null) {
            id = student.getId();
            firstName = student.getFirstName();
            lastName = student.getLastName();
            jmbg = student.getJmbg();
            address = student.getAddress();
            email = student.getEmail();
            login = student.getLogin();
            birthDate = student.getBirthDate();
            semester = student.getSemester();
            course = student.getCourse();
            pauseDate = student.getPauseDate();
        }
        points = grade.getPoints();
        subjectName = grade.getSubject().getName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
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

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}

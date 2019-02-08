package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;

public class StudentGradeSubjectWrapper {
    // Person, Student attributes
    private Student student;

    // Grade attributes
    private Grade grade;

    // Subject attributes
    private String subjectName;

    public StudentGradeSubjectWrapper() {
    }

    public StudentGradeSubjectWrapper(Student student, Grade grade) {
        this.student = student;
        this.grade = grade;
        if (grade != null)
            subjectName = grade.getSubject().getName();
    }

    public Student getStudent() {
        return student;
    }

    public int getId() {
        return student.getId();
    }

    public void setId(int id) {
        student.setId(id);
    }

    public String getFirstName() {
        return student.getFirstName();
    }

    public void setFirstName(String firstName) {
        student.setFirstName(firstName);
    }

    public String getLastName() {
        return student.getLastName();
    }

    public void setLastName(String lastName) {
        student.setLastName(lastName);
    }

    public String getJmbg() {
        return student.getJmbg();
    }

    public void setJmbg(String jmbg) {
        student.setJmbg(jmbg);
    }

    public String getAddress() {
        return student.getAddress();
    }

    public void setAddress(String address) {
        student.setAddress(address);
    }

    public String getEmail() {
        return student.getEmail();
    }

    public void setEmail(String email) {
        student.setEmail(email);
    }

    public Login getLogin() {
        return student.getLogin();
    }

    public void setLogin(Login login) {
        student.setLogin(login);
    }

    public LocalDate getBirthDate() {
        return student.getBirthDate();
    }

    public void setBirthDate(LocalDate birthDate) {
        student.setBirthDate(birthDate);
    }

    public Semester getSemester() {
        return student.getSemester();
    }

    public void setSemester(Semester semester) {
        student.setSemester(semester);
    }

    public Course getCourse() {
        return student.getCourse();
    }

    public void setCourse(Course course) {
        student.setCourse(course);
    }

    public LocalDate getPauseDate() {
        return student.getPauseDate();
    }

    public void setPauseDate(LocalDate pauseDate) {
        student.setPauseDate(pauseDate);
    }

    public float getPoints() {
        return grade.getPoints();
    }

    public void setPoints(float points) {
        grade.setPoints(points);
    }

    public Grade getGrade() {
        return grade;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}

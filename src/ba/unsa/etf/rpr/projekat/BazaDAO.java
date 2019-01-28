package ba.unsa.etf.rpr.projekat;

import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;

public class BazaDAO {
    private static BazaDAO instance = null;
    private PreparedStatement fetchLoginStmt, getLoginStmt, getProfessorStmt, getStudentStmt, getCourseStmt, getSemesterStmt, getSubjectStmt;
    private PreparedStatement addSubjectStmt, addProfessorStmt, addStudentStmt, addCourseStmt, addCurriculumStmt, addPersonStmt, addLoginStmt, addAdministratorStmt;
    private PreparedStatement updateSubjectStmt, updateProfessorStmt, updateStudentStmt, updateCourseStmt, updateCurriculumStmt, updatePersonStmt, updateLoginStmt;
    private PreparedStatement deleteSubjectStmt, deleteProfessorStmt, deleteStudentStmt, deleteCourseStmt, deleteCurriculumStmt, deletePersonStmt;
    private PreparedStatement allSubjectStmt, allProfessorStmt, allStudentStmt, allCourseStmt, allCurriculumStmt, allSemesterStmt;
    private PreparedStatement getNoStudentsOnSubjectStmt, getavgSubjectGradeStmt, getNoSubjectGradedStmt, getNoSubjectNotGradedStmt;
    private Connection conn;

    public static BazaDAO getInstance() {
        if (instance == null)
            instance = new BazaDAO();
        return instance;
    }

    public static void removeInstance() {
        instance = null;
    }

    private BazaDAO() {
        String url = "jdbc:oracle:thin:@ora.db.lab.ri.etf.unsa.ba:1521:ETFLAB";
        String username = "FP18120";
        String password = "dsmLP88q";
        try {
            conn = DriverManager.getConnection(url, username, password);
            //Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa konektovanjem na bazu: " + error.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        try {
            fetchLoginStmt = conn.prepareStatement("SELECT * FROM FP18120.LOGIN WHERE USERNAME=? AND PASSWORD=? AND USER_TYPE=?");

            getLoginStmt = conn.prepareStatement("SELECT * FROM FP18120.LOGIN WHERE ID=?");
            getProfessorStmt = conn.prepareStatement("SELECT * FROM FP18120.PERSON, FP18120.PROFESSOR WHERE PROFESSOR.ID=? AND PERSON.ID=PROFESSOR.ID");
            getStudentStmt = conn.prepareStatement("SELECT * FROM FP18120.PERSON, FP18120.STUDENT WHERE STUDENT.ID=? AND PERSON.ID=STUDENT.ID");
            getCourseStmt = conn.prepareStatement("SELECT * FROM FP18120.COURSE WHERE ID=?");
            getSemesterStmt = conn.prepareStatement("SELECT * FROM FP18120.SEMESTER WHERE ID=?");
            getSubjectStmt = conn.prepareStatement("SELECT * FROM FP18120.SUBJECT WHERE ID=?");

            addSubjectStmt = conn.prepareStatement("INSERT INTO FP18120.SUBJECT VALUES (FP18120.SUBJECT_SEQ.NEXTVAL, ?, ?, ?, ?, ?)");
            addPersonStmt = conn.prepareStatement("INSERT INTO FP18120.PERSON VALUES (FP18120.PERSON_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)");
            addProfessorStmt = conn.prepareStatement("INSERT INTO FP18120.PROFESSOR VALUES (FP18120.PERSON_SEQ.CURRVAL, ?)");
            addStudentStmt = conn.prepareStatement("INSERT INTO FP18120.STUDENT VALUES (FP18120.PERSON_SEQ.CURRVAL, ?, ?, ?, ?)");
            addAdministratorStmt = conn.prepareStatement("INSERT INTO FP18120.ADMINISTRATOR VALUES (FP18120.PERSON_SEQ.CURRVAL)");
            addCourseStmt = conn.prepareStatement("INSERT INTO FP18120.COURSE VALUES (FP18120.COURSE_SEQ.NEXTVAL, ?)");
            addCurriculumStmt = conn.prepareStatement("INSERT INTO FP18120.CURRICULUM VALUES (FP18120.CURRICULUM_SEQ.NEXTVAL, ?, ?, ?, ?)");
            addLoginStmt = conn.prepareStatement("INSERT INTO FP18120.LOGIN VALUES (FP18120.LOGIN_SEQ.NEXTVAL, ?, ?, ?, ?, ?)");

            updateSubjectStmt = conn.prepareStatement("UPDATE FP18120.SUBJECT SET NAME=?, CODE=?, ECTS=?, PROFESSOR_ID=?, REQ_SUBJECT_ID=? WHERE ID=?");
            updatePersonStmt = conn.prepareStatement("UPDATE FP18120.PERSON SET FIRST_NAME=?, LAST_NAME=?, JMBG=?, ADDRESS=?, EMAIL=?, LOGIN_ID=? WHERE ID=?");
            updateProfessorStmt = conn.prepareStatement("UPDATE FP18120.PROFESSOR SET TITLE=? WHERE ID=?");
            updateStudentStmt = conn.prepareStatement("UPDATE FP18120.STUDENT SET BIRTH_DATE=?, SEMESTER_ID=?, COURSE_ID=?, PAUSE_DATE=? WHERE ID=?");
            updateCourseStmt = conn.prepareStatement("UPDATE FP18120.COURSE SET NAME=? WHERE ID=?");
            updateCurriculumStmt = conn.prepareStatement("UPDATE FP18120.CURRICULUM SET COURSE_ID=?, SEMESTER_ID=?, SUBJECT_ID=?, REQUIRED_SUBJECT=? WHERE ID=?");
            updateLoginStmt = conn.prepareStatement("UPDATE FP18120.LOGIN SET USERNAME=?, PASSWORD=?, DATE_CREATED=?, USER_TYPE=?, LAST_LOGIN_DATE=? WHERE ID=?");

            deleteSubjectStmt = conn.prepareStatement("DELETE FROM FP18120.SUBJECT WHERE ID=?");
            deletePersonStmt = conn.prepareStatement("DELETE FROM FP18120.PERSON WHERE ID=?");
            deleteProfessorStmt = conn.prepareStatement("DELETE FROM FP18120.PROFESSOR WHERE ID=?");
            deleteStudentStmt = conn.prepareStatement("DELETE FROM FP18120.STUDENT WHERE ID=?");
            deleteCourseStmt = conn.prepareStatement("DELETE FROM FP18120.COURSE WHERE ID=?");
            deleteCurriculumStmt = conn.prepareStatement("DELETE FROM FP18120.CURRICULUM WHERE ID=?");

            allSubjectStmt = conn.prepareStatement("SELECT * FROM FP18120.SUBJECT");
            allProfessorStmt = conn.prepareStatement("SELECT * FROM FP18120.PERSON, FP18120.PROFESSOR WHERE PERSON.ID=PROFESSOR.ID");
            allStudentStmt = conn.prepareStatement("SELECT * FROM FP18120.PERSON, FP18120.STUDENT WHERE PERSON.ID=STUDENT.ID");
            allCourseStmt = conn.prepareStatement("SELECT * FROM FP18120.COURSE");
            allCurriculumStmt = conn.prepareStatement("SELECT * FROM FP18120.CURRICULUM");
            allSemesterStmt = conn.prepareStatement("SELECT * FROM FP18120.SEMESTER");

            getNoStudentsOnSubjectStmt = conn.prepareStatement("SELECT COUNT(*) FROM FP18120.GRADE, FP18120.STUDENT WHERE SUBJECT_ID=? AND STUDENT.ID = STUDENT_ID AND PAUSE_DATE IS NULL");
            getavgSubjectGradeStmt = conn.prepareStatement("SELECT ROUND(AVG(SCORE),2) FROM FP18120.GRADE, FP18120.STUDENT WHERE SCORE IS NOT NULL AND SUBJECT_ID=? AND STUDENT.ID = STUDENT_ID AND PAUSE_DATE IS NULL");
            getNoSubjectGradedStmt = conn.prepareStatement("SELECT COUNT(*) FROM FP18120.GRADE, FP18120.STUDENT WHERE SCORE IS NOT NULL AND SUBJECT_ID=? AND STUDENT.ID = STUDENT_ID AND PAUSE_DATE IS NULL");
            getNoSubjectNotGradedStmt = conn.prepareStatement("SELECT COUNT(*) FROM FP18120.GRADE, FP18120.STUDENT WHERE SCORE IS NULL AND SUBJECT_ID=? AND STUDENT.ID = STUDENT_ID AND PAUSE_DATE IS NULL");
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa konektovanjem na bazu: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String headerText, Alert.AlertType type) {
        Alert error = new Alert(type);
        error.setTitle(title);
        error.setHeaderText(headerText);
        error.show();
    }

    private Login getLoginInfo(ResultSet resultSet) {
        Login login = null;
        try {
            while (resultSet.next()) {
                login = new Login();
                login.setId(resultSet.getInt(1));
                login.setUsername(resultSet.getString(2));
                login.setPassword(resultSet.getString(3));
                login.setDateCreated((resultSet.getDate(4) == null) ? null : (resultSet.getDate(4).toLocalDate()));
                login.setUserType(resultSet.getString(5));
                login.setLastLoginDate((resultSet.getDate(6) == null) ? null : (resultSet.getDate(6).toLocalDate()));
            }
        } catch (SQLException ignored) {
            return null;
        }
        return login;
    }

    public Login getLogin(String username, String password, String user_type) {
        Login login;
        try {
            fetchLoginStmt.setString(1, username);
            fetchLoginStmt.setString(2, password);
            fetchLoginStmt.setString(3, user_type);
            var resultSet = fetchLoginStmt.executeQuery();
            login = getLoginInfo(resultSet);
        } catch (SQLException ignored) {
            return null;
        }
        return login;
    }

    public Login getLogin(int id) {
        Login login;
        try {
            getLoginStmt.setInt(1, id);
            var resultSet = getLoginStmt.executeQuery();
            login = getLoginInfo(resultSet);
        } catch (SQLException ignored) {
            return null;
        }
        return login;
    }

    public Professor getProfessor(int id) {
        Professor professor = null;
        try {
            getProfessorStmt.setInt(1, id);
            var resultSet = getProfessorStmt.executeQuery();
            while (resultSet.next()) {
                professor = new Professor();
                professor.setId(resultSet.getInt(1));
                professor.setFirstName(resultSet.getString(2));
                professor.setLastName(resultSet.getString(3));
                professor.setJmbg(resultSet.getString(4));
                professor.setAddress(resultSet.getString(5));
                professor.setEmail(resultSet.getString(6));
                professor.setLogin(getLogin(resultSet.getInt(7)));
                professor.setTitle(resultSet.getString(9));
            }
        } catch (SQLException ignored) {
            return null;
        }
        return professor;
    }

    public ArrayList<Subject> subjects () throws SQLException {
        ArrayList<Subject> subjects = new ArrayList<>();
        var resultSet = allSubjectStmt.executeQuery();
        while (resultSet.next()) {
            Subject subject = getSubject(resultSet.getInt(1));
            subjects.add(subject);
        }
        return subjects;
    }

    public ArrayList<Professor> professors () throws SQLException {
        ArrayList<Professor> professors = new ArrayList<>();
        var resultSet = allProfessorStmt.executeQuery();
        while (resultSet.next()) {
            Professor professor = getProfessor(resultSet.getInt(1));
            professors.add(professor);
        }
        return professors;
    }

    public Course getCourse(int id) {
        Course course = null;
        try {
            getCourseStmt.setInt(1, id);
            var resultSet = getCourseStmt.executeQuery();
            while (resultSet.next()) {
                course = new Course();
                course.setId(resultSet.getInt(1));
                course.setName(resultSet.getString(2));
            }
        } catch (SQLException ignored) {
            return null;
        }
        return course;
    }

    public Semester getSemester(int id) {
        Semester semester = null;
        try {
            getSemesterStmt.setInt(1, id);
            var resultSet = getSemesterStmt.executeQuery();
            while (resultSet.next()) {
                semester = new Semester();
                semester.setId(resultSet.getInt(1));
                semester.setNo(resultSet.getInt(2));
                semester.setCycleNo(resultSet.getInt(3));
                semester.setEcts(resultSet.getInt(4));
            }
        } catch (SQLException ignored) {
            return null;
        }
        return semester;
    }

    public Student getStudent(int id) {
        Student student = null;
        try {
            getStudentStmt.setInt(1, id);
            var resultSet = getStudentStmt.executeQuery();
            while (resultSet.next()) {
                student = new Student();
                student.setId(resultSet.getInt(1));
                student.setFirstName(resultSet.getString(2));
                student.setLastName(resultSet.getString(3));
                student.setJmbg(resultSet.getString(4));
                student.setAddress(resultSet.getString(5));
                student.setEmail(resultSet.getString(6));
                student.setLogin(getLogin(resultSet.getInt(7)));
                student.setBirthDate(resultSet.getDate(9).toLocalDate());
                student.setSemester(getSemester(resultSet.getInt(10)));
                student.setCourse(getCourse(resultSet.getInt(11)));
                student.setPauseDate((resultSet.getDate(12) == null) ? null : (resultSet.getDate(12).toLocalDate()));
            }
        } catch (SQLException ignored) {
            return null;
        }
        return student;
    }

    public ArrayList<Student> students () throws SQLException {
        ArrayList<Student> students = new ArrayList<>();
        var resultSet = allStudentStmt.executeQuery();
        while (resultSet.next()) {
            Student student = getStudent(resultSet.getInt(1));
            students.add(student);
        }
        return students;
    }

    public ArrayList<Course> courses () throws SQLException {
        ArrayList<Course> courses = new ArrayList<>();
        var resultSet = allCourseStmt.executeQuery();
        while (resultSet.next()) {
            Course course = new Course();
            course.setId(resultSet.getInt(1));
            course.setName(resultSet.getString(2));
            courses.add(course);
        }
        return courses;
    }

    public ArrayList<Semester> semesters () throws SQLException {
        ArrayList<Semester> semesters = new ArrayList<>();
        var resultSet = allSemesterStmt.executeQuery();
        while (resultSet.next()) {
            Semester semester = getSemester(resultSet.getInt(1));
            semesters.add(semester);
        }
        return semesters;
    }

    private Subject getSubject(int id) {
        Subject subject = null;
        try {
            getSubjectStmt.setInt(1, id);
            var resultSet = getSubjectStmt.executeQuery();
            while (resultSet.next()) {
                subject = new Subject();
                subject.setId(resultSet.getInt(1));
                subject.setName(resultSet.getString(2));
                subject.setCode(resultSet.getString(3));
                subject.setEcts(resultSet.getInt(4));
                subject.setProfessor(getProfessor(resultSet.getInt(5)));
                subject.setReqSubject(getSubject(resultSet.getInt(6)));
            }
        } catch (SQLException ignored) {
            return null;
        }
        return subject;
    }


    public ArrayList<Curriculum> curriculums () throws SQLException {
        ArrayList<Curriculum> curriculums = new ArrayList<>();
        var resultSet = allCurriculumStmt.executeQuery();
        while (resultSet.next()) {
            Curriculum curriculum = new Curriculum();
            curriculum.setId(resultSet.getInt(1));
            curriculum.setCourse(getCourse(resultSet.getInt(2)));
            curriculum.setSemester(getSemester(resultSet.getInt(3)));
            curriculum.setSubject(getSubject(resultSet.getInt(4)));
            curriculum.setRequiredSubject(resultSet.getString(5));
            curriculums.add(curriculum);
        }
        return curriculums;
    }

    public void addSubject(Subject subject) throws SQLException {
        addSubjectStmt.setString(1, subject.getName());
        addSubjectStmt.setString(2, subject.getCode());
        addSubjectStmt.setInt(3, subject.getEcts());
        addSubjectStmt.setInt(4, subject.getProfessor().getId());
        addSubjectStmt.setInt(5, subject.getReqSubject().getId());
        addSubjectStmt.executeUpdate();
    }

    public void addLogin(Login login) throws SQLException {
        addLoginStmt.setString(1, login.getUsername());
        addLoginStmt.setString(2, login.getPassword());
        addLoginStmt.setDate(3, Date.valueOf(login.getDateCreated()));
        addLoginStmt.setString(4, login.getUserType());
        addLoginStmt.setDate(5, (login.getLastLoginDate() == null) ? null : Date.valueOf(login.getLastLoginDate()));
        addLoginStmt.executeUpdate();
    }

    public void addProfessor(Professor professor) throws SQLException {
        addPerson(professor);
        addProfessorStmt.setString(1, professor.getTitle());
        addProfessorStmt.executeUpdate();
    }

    public void addPerson(Person person) throws SQLException {
        addLogin(person.getLogin());
        addPersonStmt.setString(1, person.getFirstName());
        addPersonStmt.setString(2, person.getLastName());
        addPersonStmt.setString(3, person.getJmbg());
        addPersonStmt.setString(4, person.getAddress());
        addPersonStmt.setString(5, person.getEmail());
        addPersonStmt.setInt(6, person.getLogin().getId());
        addPersonStmt.executeUpdate();
    }

    public void addStudent(Student student) throws SQLException {
        addPerson(student);
        addStudentStmt.setDate(1, Date.valueOf(student.getBirthDate()));
        addStudentStmt.setInt(2, student.getSemester().getId());
        addStudentStmt.setInt(3, student.getCourse().getId());
        addStudentStmt.setDate(4, (student.getPauseDate() == null) ? null : Date.valueOf(student.getPauseDate()));
        addStudentStmt.executeUpdate();
    }

    public void addAdministrator(Administrator administrator) throws SQLException {
        addPerson(administrator);
        addAdministratorStmt.executeUpdate();
    }

    public void addCourse(Course course) throws SQLException {
        addCourseStmt.setString(1, course.getName());
        addCourseStmt.executeUpdate();
    }

    public void addCurriculum(Curriculum curriculum) throws SQLException {
        addCurriculumStmt.setInt(1, curriculum.getCourse().getId());
        addCurriculumStmt.setInt(2, curriculum.getSemester().getId());
        addCurriculumStmt.setInt(3, curriculum.getSubject().getId());
        addCurriculumStmt.setString(4, curriculum.getRequiredSubject());
        addCurriculumStmt.executeUpdate();
    }

    public void updateSubject(Subject subject) throws SQLException {
        updateSubjectStmt.setString(1, subject.getName());
        updateSubjectStmt.setString(2, subject.getCode());
        updateSubjectStmt.setInt(3, subject.getEcts());
        updateSubjectStmt.setInt(4, subject.getProfessor().getId());
        updateSubjectStmt.setInt(5, subject.getId());
        updateCourseStmt.setInt(6, subject.getReqSubject().getId());
        updateSubjectStmt.executeUpdate();
    }

    public void updatePerson(Person person) throws SQLException {
        updatePersonStmt.setString(1, person.getFirstName());
        updatePersonStmt.setString(2, person.getLastName());
        updatePersonStmt.setString(3, person.getJmbg());
        updatePersonStmt.setString(4, person.getAddress());
        updatePersonStmt.setString(5, person.getEmail());
        updatePersonStmt.setInt(6, person.getLogin().getId());
        updatePersonStmt.setInt(7, person.getId());
        updatePersonStmt.executeUpdate();
    }

    public void updateAdministrator(Administrator administrator) throws SQLException {
        updatePerson(administrator);
    }

    public void updateProfessor(Professor professor) throws SQLException {
        updatePerson(professor);
        updateProfessorStmt.setString(1, professor.getTitle());
        updateProfessorStmt.setInt(2, professor.getId());
        updateProfessorStmt.executeUpdate();
    }

    public void updateStudent(Student student) throws SQLException {
        updatePerson(student);
        updateStudentStmt.setDate(1, Date.valueOf(student.getBirthDate()));
        updateStudentStmt.setInt(2, student.getSemester().getId());
        updateStudentStmt.setInt(3, student.getCourse().getId());
        updateStudentStmt.setInt(4, student.getId());
        updateStudentStmt.setDate(5, (student.getPauseDate() == null) ? null : Date.valueOf(student.getPauseDate()));
        updateStudentStmt.executeUpdate();
    }

    public void updateCourse(Course course) throws SQLException {
        updateCourseStmt.setString(1, course.getName());
        updateCourseStmt.setInt(2, course.getId());
        updateCourseStmt.executeUpdate();
    }

    public void updateCurriculum(Curriculum curriculum) throws SQLException {
        updateCurriculumStmt.setInt(1, curriculum.getCourse().getId());
        updateCurriculumStmt.setInt(2, curriculum.getSemester().getId());
        updateCurriculumStmt.setInt(3, curriculum.getSubject().getId());
        updateCurriculumStmt.setString(4, curriculum.getRequiredSubject());
        updateCurriculumStmt.executeUpdate();
    }

    public void updateLogin(Login login) throws SQLException {
        updateLoginStmt.setString(1, login.getUsername());
        updateLoginStmt.setString(2, login.getPassword());
        updateLoginStmt.setDate(3, (login.getDateCreated() == null) ? null : Date.valueOf(login.getDateCreated()));
        updateLoginStmt.setString(4, login.getUserType());
        updateLoginStmt.setDate(5, (login.getDateCreated() == null) ? null : Date.valueOf(login.getLastLoginDate()));
        updateLoginStmt.setInt(6, login.getId());
        updateLoginStmt.executeUpdate();
    }

    public void deleteSubject(Subject subject) throws SQLException {
        deleteSubjectStmt.setInt(1, subject.getId());
        deleteSubjectStmt.executeUpdate();
    }

    public void deletePerson(Person person) throws SQLException {
        deleteSubjectStmt.setInt(1, person.getId());
        deleteSubjectStmt.executeUpdate();
    }

    public void deleteProfessor(Professor professor) throws SQLException {
        deleteProfessorStmt.setInt(1, professor.getId());
        deleteProfessorStmt.executeUpdate();
    }

    public void deleteStudent(Student student) throws SQLException {
        deleteStudentStmt.setInt(1, student.getId());
        deleteStudentStmt.executeUpdate();
    }

    public void deleteCourse(Course course) throws SQLException {
        deleteCourseStmt.setInt(1, course.getId());
        deleteCourseStmt.executeUpdate();
    }

    public void deleteCurriculum(Curriculum curriculum) throws SQLException {
        deleteCurriculumStmt.setInt(1, curriculum.getId());
        deleteCurriculumStmt.executeUpdate();
    }

    public float getAvgSubjectGrade(Subject subject) throws SQLException {
        float average = 0;
        getavgSubjectGradeStmt.setInt(1, subject.getId());
        var resultSet = getavgSubjectGradeStmt.executeQuery();
        while (resultSet.next())
            average = resultSet.getFloat(1);
        return average;
    }

    public float getPercentSubjectPassed (Subject subject) throws SQLException {
        return Math.round(getNoSubjectGraded(subject) * 10000f / (getNoSubjectGraded(subject) + getNoSubjectNotGraded(subject))) / 100f;
    }

    public int getNoStudentsOnSubject(Subject subject) throws SQLException {
        return getCountInfo(subject, getNoStudentsOnSubjectStmt);
    }

    public int getNoSubjectGraded(Subject subject) throws SQLException {
        return getCountInfo(subject, getNoSubjectGradedStmt);
    }

    public int getNoSubjectNotGraded(Subject subject) throws SQLException {
        return getCountInfo(subject, getNoSubjectNotGradedStmt);
    }

    private int getCountInfo(Subject subject, PreparedStatement getNoSubjectNotGradedStmt) throws SQLException {
        int no = 0;
        getNoSubjectNotGradedStmt.setInt(1, subject.getId());
        var resultSet = getNoSubjectNotGradedStmt.executeQuery();
        while (resultSet.next())
            no = resultSet.getInt(1);
        return no;
    }
}

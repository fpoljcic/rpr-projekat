package ba.unsa.etf.rpr.projekat;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;

import static oracle.jdbc.OracleTypes.DATE;
import static oracle.jdbc.OracleTypes.INTEGER;

public class BazaDAO {
    private static BazaDAO instance = null;
    private PreparedStatement fetchLoginStmt, getLoginStmt, getProfessorStmt, getStudentStmt, getAdminStmt, getCourseStmt, getSemesterStmt, getSubjectStmt, getStudentLoginStmt, getProfessorLoginStmt, getGradeStmt, getSemestersCycleStmt;
    private PreparedStatement addSubjectStmt, addProfessorStmt, addStudentStmt, addCourseStmt, addCurriculumStmt, addPersonStmt, addLoginStmt, addAdministratorStmt, addGradeStmt;
    private PreparedStatement updateSubjectStmt, updateProfessorStmt, updateStudentStmt, updateCourseStmt, updateCurriculumStmt, updatePersonStmt, updateLoginStmt, updateGradeStmt;
    private PreparedStatement deleteSubjectStmt, deleteCourseStmt, deleteCurriculumStmt, deletePersonStmt, deleteLoginSmt;
    private PreparedStatement allSubjectStmt, allProfessorStmt, allStudentStmt, allCourseStmt, allCurriculumStmt, allSemesterStmt, allCyclesStmt, allAdminStmt;
    private PreparedStatement allSubjectStudentStmt, allSubjectPassedStudentStmt;
    private PreparedStatement allSubjectProfessorStmt, allStudentProfessorStmt;
    private PreparedStatement getStudentsStmt, getSubjectSemesterStmt, getLoginIdStmt;
    private PreparedStatement getavgSubjectGradeStmt, getNoSubjectGradedStmt, getNoSubjectNotGradedStmt;
    private PreparedStatement getavgProfessorGradeStmt, getNoProfessorGradedStmt, getNoProfessorNotGradedStmt;
    private PreparedStatement getavgStudentGradeStmt, getNoStudentGradedStmt, getNoStudentNotGradedStmt;
    private PreparedStatement getavgCourseGradeStmt, getNoStudentsOnCourseStmt;
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
            fetchLoginStmt = conn.prepareStatement("SELECT * FROM FP18120.LOGIN WHERE USERNAME=? AND USER_TYPE=?");
            getLoginIdStmt = conn.prepareStatement("SELECT LOGIN.ID FROM FP18120.LOGIN, FP18120.PERSON WHERE LOGIN.ID = LOGIN_ID AND FIRST_NAME=? AND LAST_NAME=? AND EMAIL=? AND USER_TYPE=?");

            getLoginStmt = conn.prepareStatement("SELECT * FROM FP18120.LOGIN WHERE ID=?");
            getProfessorStmt = conn.prepareStatement("SELECT * FROM FP18120.PERSON, FP18120.PROFESSOR WHERE PROFESSOR.ID=? AND PERSON.ID = PROFESSOR.ID");
            getStudentStmt = conn.prepareStatement("SELECT * FROM FP18120.PERSON, FP18120.STUDENT WHERE STUDENT.ID=? AND PERSON.ID = STUDENT.ID");
            getAdminStmt = conn.prepareStatement("SELECT * FROM FP18120.PERSON, FP18120.ADMINISTRATOR WHERE ADMINISTRATOR.ID=? AND PERSON.ID = ADMINISTRATOR.ID");
            getStudentLoginStmt = conn.prepareStatement("SELECT * FROM FP18120.PERSON, FP18120.STUDENT WHERE LOGIN_ID=? AND PERSON.ID = STUDENT.ID");
            getProfessorLoginStmt = conn.prepareStatement("SELECT * FROM FP18120.PERSON, FP18120.PROFESSOR WHERE LOGIN_ID=? AND PERSON.ID = PROFESSOR.ID");
            getCourseStmt = conn.prepareStatement("SELECT * FROM FP18120.COURSE WHERE ID=?");
            getSemesterStmt = conn.prepareStatement("SELECT * FROM FP18120.SEMESTER WHERE ID=?");
            getSubjectStmt = conn.prepareStatement("SELECT * FROM FP18120.SUBJECT WHERE ID=?");
            getSubjectSemesterStmt = conn.prepareStatement("SELECT SUBJECT.ID FROM FP18120.SUBJECT, FP18120.CURRICULUM WHERE CURRICULUM.SUBJECT_ID=SUBJECT.ID AND SEMESTER_ID=? AND REQUIRED_SUBJECT=? AND COURSE_ID=?");
            getGradeStmt = conn.prepareStatement("SELECT * FROM FP18120.GRADE WHERE ID=?");
            getSemestersCycleStmt = conn.prepareStatement("SELECT NO FROM FP18120.SEMESTER WHERE CYCLE_NO=?");
            getStudentsStmt = conn.prepareStatement("SELECT STUDENT.ID FROM FP18120.PERSON, FP18120.STUDENT, FP18120.COURSE, FP18120.SEMESTER WHERE PERSON.ID = STUDENT.ID AND COURSE_ID = COURSE.ID AND SEMESTER.ID = SEMESTER_ID");

            addSubjectStmt = conn.prepareStatement("INSERT INTO FP18120.SUBJECT VALUES (FP18120.SUBJECT_SEQ.NEXTVAL, ?, ?, ?, ?, ?)");
            addPersonStmt = conn.prepareStatement("INSERT INTO FP18120.PERSON VALUES (FP18120.PERSON_SEQ.NEXTVAL, ?, ?, ?, ?, ?, FP18120.LOGIN_SEQ.CURRVAL)");
            addProfessorStmt = conn.prepareStatement("INSERT INTO FP18120.PROFESSOR VALUES (FP18120.PERSON_SEQ.CURRVAL, ?)");
            addStudentStmt = conn.prepareStatement("INSERT INTO FP18120.STUDENT VALUES (FP18120.PERSON_SEQ.CURRVAL, ?, ?, ?, ?)");
            addAdministratorStmt = conn.prepareStatement("INSERT INTO FP18120.ADMINISTRATOR VALUES (FP18120.PERSON_SEQ.CURRVAL)");
            addCourseStmt = conn.prepareStatement("INSERT INTO FP18120.COURSE VALUES (FP18120.COURSE_SEQ.NEXTVAL, ?)");
            addCurriculumStmt = conn.prepareStatement("INSERT INTO FP18120.CURRICULUM VALUES (FP18120.CURRICULUM_SEQ.NEXTVAL, ?, ?, ?, ?)");
            addLoginStmt = conn.prepareStatement("INSERT INTO FP18120.LOGIN VALUES (FP18120.LOGIN_SEQ.NEXTVAL, ?, ?, ?, ?, ?)");
            addGradeStmt = conn.prepareStatement("INSERT INTO FP18120.GRADE VALUES (FP18120.GRADE_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)");

            updateSubjectStmt = conn.prepareStatement("UPDATE FP18120.SUBJECT SET NAME=?, CODE=?, ECTS=?, PROFESSOR_ID=?, REQ_SUBJECT_ID=? WHERE ID=?");
            updatePersonStmt = conn.prepareStatement("UPDATE FP18120.PERSON SET FIRST_NAME=?, LAST_NAME=?, JMBG=?, ADDRESS=?, EMAIL=?, LOGIN_ID=? WHERE ID=?");
            updateProfessorStmt = conn.prepareStatement("UPDATE FP18120.PROFESSOR SET TITLE=? WHERE ID=?");
            updateStudentStmt = conn.prepareStatement("UPDATE FP18120.STUDENT SET BIRTH_DATE=?, SEMESTER_ID=?, COURSE_ID=?, PAUSE_DATE=? WHERE ID=?");
            updateCourseStmt = conn.prepareStatement("UPDATE FP18120.COURSE SET NAME=? WHERE ID=?");
            updateCurriculumStmt = conn.prepareStatement("UPDATE FP18120.CURRICULUM SET COURSE_ID=?, SEMESTER_ID=?, SUBJECT_ID=?, REQUIRED_SUBJECT=? WHERE ID=?");
            updateLoginStmt = conn.prepareStatement("UPDATE FP18120.LOGIN SET USERNAME=?, PASSWORD=?, DATE_CREATED=?, USER_TYPE=?, LAST_LOGIN_DATE=? WHERE ID=?");
            updateGradeStmt = conn.prepareStatement("UPDATE FP18120.GRADE SET STUDENT_ID=?, SUBJECT_ID=?, POINTS=?, SCORE=?, GRADE_DATE=?, PROFESSOR_ID=? WHERE ID=?");

            deleteSubjectStmt = conn.prepareStatement("DELETE FROM FP18120.SUBJECT WHERE ID=?");
            deletePersonStmt = conn.prepareStatement("DELETE FROM FP18120.PERSON WHERE ID=?");
            deleteLoginSmt = conn.prepareStatement("DELETE FROM FP18120.LOGIN WHERE ID=?");
            deleteCourseStmt = conn.prepareStatement("DELETE FROM FP18120.COURSE WHERE ID=?");
            deleteCurriculumStmt = conn.prepareStatement("DELETE FROM FP18120.CURRICULUM WHERE ID=?");

            allSubjectStmt = conn.prepareStatement("SELECT * FROM FP18120.SUBJECT");
            allProfessorStmt = conn.prepareStatement("SELECT * FROM FP18120.PERSON, FP18120.PROFESSOR WHERE PERSON.ID = PROFESSOR.ID");
            allStudentStmt = conn.prepareStatement("SELECT * FROM FP18120.PERSON, FP18120.STUDENT WHERE PERSON.ID = STUDENT.ID");
            allAdminStmt = conn.prepareStatement("SELECT * FROM FP18120.PERSON, FP18120.ADMINISTRATOR WHERE PERSON.ID = ADMINISTRATOR.ID");
            allCourseStmt = conn.prepareStatement("SELECT * FROM FP18120.COURSE");
            allCurriculumStmt = conn.prepareStatement("SELECT * FROM FP18120.CURRICULUM");
            allSemesterStmt = conn.prepareStatement("SELECT * FROM FP18120.SEMESTER");
            allCyclesStmt = conn.prepareStatement("SELECT DISTINCT(CYCLE_NO) FROM FP18120.SEMESTER");

            allSubjectStudentStmt = conn.prepareStatement("SELECT SUBJECT.ID, GRADE.ID FROM FP18120.SUBJECT, FP18120.GRADE, FP18120.STUDENT WHERE SUBJECT.ID = SUBJECT_ID AND STUDENT.ID = STUDENT_ID AND SCORE IS NULL AND STUDENT.ID=?");
            allSubjectPassedStudentStmt = conn.prepareStatement("SELECT SUBJECT.ID, GRADE.ID FROM FP18120.SUBJECT, FP18120.GRADE, FP18120.STUDENT WHERE SUBJECT.ID = SUBJECT_ID AND STUDENT.ID = STUDENT_ID AND SCORE IS NOT NULL AND STUDENT.ID=?");

            allSubjectProfessorStmt = conn.prepareStatement("SELECT * FROM FP18120.SUBJECT WHERE PROFESSOR_ID=?");
            allStudentProfessorStmt = conn.prepareStatement("SELECT STUDENT.ID, GRADE.ID, SUBJECT_ID FROM FP18120.PERSON, FP18120.STUDENT, FP18120.GRADE, FP18120.SUBJECT, FP18120.PROFESSOR WHERE PERSON.ID = STUDENT.ID AND STUDENT.ID = STUDENT_ID AND SUBJECT_ID = SUBJECT.ID AND SUBJECT.PROFESSOR_ID = PROFESSOR.ID AND SCORE IS NULL AND PROFESSOR.ID=?");

            getavgSubjectGradeStmt = conn.prepareStatement("SELECT ROUND(AVG(SCORE),2) FROM FP18120.GRADE, FP18120.STUDENT WHERE SCORE IS NOT NULL AND SUBJECT_ID=? AND STUDENT.ID = STUDENT_ID AND PAUSE_DATE IS NULL");
            getNoSubjectGradedStmt = conn.prepareStatement("SELECT COUNT(*) FROM FP18120.GRADE, FP18120.STUDENT WHERE SCORE IS NOT NULL AND SUBJECT_ID=? AND STUDENT.ID = STUDENT_ID AND PAUSE_DATE IS NULL");
            getNoSubjectNotGradedStmt = conn.prepareStatement("SELECT COUNT(*) FROM FP18120.GRADE, FP18120.STUDENT WHERE SCORE IS NULL AND SUBJECT_ID=? AND STUDENT.ID = STUDENT_ID AND PAUSE_DATE IS NULL");

            getavgProfessorGradeStmt = conn.prepareStatement("SELECT ROUND(AVG(SCORE),2) FROM FP18120.GRADE, FP18120.STUDENT, FP18120.SUBJECT WHERE SUBJECT_ID = SUBJECT.ID AND SCORE IS NOT NULL AND SUBJECT.PROFESSOR_ID=? AND STUDENT.ID = STUDENT_ID AND PAUSE_DATE IS NULL");
            getNoProfessorGradedStmt = conn.prepareStatement("SELECT COUNT(*) FROM FP18120.GRADE, FP18120.STUDENT, FP18120.SUBJECT WHERE SUBJECT_ID = SUBJECT.ID AND SCORE IS NOT NULL AND SUBJECT.PROFESSOR_ID=? AND STUDENT.ID = STUDENT_ID AND PAUSE_DATE IS NULL");
            getNoProfessorNotGradedStmt = conn.prepareStatement("SELECT COUNT(*) FROM FP18120.GRADE, FP18120.STUDENT, FP18120.SUBJECT WHERE SUBJECT_ID = SUBJECT.ID AND SCORE IS NULL AND SUBJECT.PROFESSOR_ID=? AND STUDENT.ID = STUDENT_ID AND PAUSE_DATE IS NULL");

            getavgStudentGradeStmt = conn.prepareStatement("SELECT ROUND(AVG(SCORE),2) FROM FP18120.GRADE, FP18120.STUDENT WHERE SCORE IS NOT NULL AND STUDENT.ID = STUDENT_ID AND STUDENT.ID=?");
            getNoStudentGradedStmt = conn.prepareStatement("SELECT COUNT(*) FROM FP18120.GRADE, FP18120.STUDENT WHERE SCORE IS NOT NULL AND STUDENT.ID = STUDENT_ID AND STUDENT.ID=?");
            getNoStudentNotGradedStmt = conn.prepareStatement("SELECT COUNT(*) FROM FP18120.GRADE, FP18120.STUDENT WHERE SCORE IS NULL AND STUDENT.ID = STUDENT_ID AND STUDENT.ID=?");

            getavgCourseGradeStmt = conn.prepareStatement("SELECT ROUND(AVG(SCORE),2) FROM FP18120.COURSE, FP18120.GRADE, FP18120.STUDENT WHERE COURSE_ID = COURSE.ID AND SCORE IS NOT NULL AND COURSE.ID=? AND STUDENT.ID = STUDENT_ID AND PAUSE_DATE IS NULL");
            getNoStudentsOnCourseStmt = conn.prepareStatement("SELECT COUNT(*) FROM FP18120.COURSE, FP18120.STUDENT WHERE COURSE_ID = COURSE.ID AND COURSE.ID=? AND PAUSE_DATE IS NULL");
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

    public Login getLogin(String username, String user_type) {
        Login login;
        try {
            fetchLoginStmt.setString(1, username);
            fetchLoginStmt.setString(2, user_type);
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

    public ArrayList<Administrator> admins() throws SQLException {
        ArrayList<Administrator> administrators = new ArrayList<>();
        var resultSet = allAdminStmt.executeQuery();
        while (resultSet.next()) {
            Administrator administrator = getAdmin(resultSet.getInt(1));
            administrators.add(administrator);
        }
        return administrators;
    }

    public Administrator getAdmin(int id) {
        Administrator administrator = null;
        try {
            getAdminStmt.setInt(1, id);
            var resultSet = getAdminStmt.executeQuery();
            administrator = getAdminInfo(administrator, resultSet);
        } catch (SQLException ignored) {
            return null;
        }
        return administrator;
    }

    private Administrator getAdminInfo(Administrator administrator, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            administrator = new Administrator();
            setPersonInfo(administrator, resultSet);
        }
        return administrator;
    }

    private void setPersonInfo(Person person, ResultSet resultSet) throws SQLException {
        person.setId(resultSet.getInt(1));
        person.setFirstName(resultSet.getString(2));
        person.setLastName(resultSet.getString(3));
        person.setJmbg(resultSet.getString(4));
        person.setAddress(resultSet.getString(5));
        person.setEmail(resultSet.getString(6));
        person.setLogin(getLogin(resultSet.getInt(7)));
    }

    public ArrayList<Professor> professors() throws SQLException {
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

    public ArrayList<Student> students() throws SQLException {
        ArrayList<Student> students = new ArrayList<>();
        var resultSet = allStudentStmt.executeQuery();
        while (resultSet.next()) {
            Student student = getStudent(resultSet.getInt(1));
            students.add(student);
        }
        return students;
    }

    public ArrayList<Course> courses() throws SQLException {
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

    public ArrayList<String> coursesNames() throws SQLException {
        ArrayList<String> courses = new ArrayList<>();
        var resultSet = allCourseStmt.executeQuery();
        while (resultSet.next())
            courses.add(resultSet.getString(2));
        return courses;
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


    public ArrayList<Curriculum> curriculums() throws SQLException {
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
        if (subject.getReqSubject() == null)
            addSubjectStmt.setNull(5, INTEGER);
        else
            addSubjectStmt.setInt(5, subject.getReqSubject().getId());
        addSubjectStmt.executeUpdate();
    }

    public void addLogin(Login login) throws SQLException {
        addLoginStmt.setString(1, login.getUsername());
        addLoginStmt.setString(2, login.getPassword());
        addLoginStmt.setDate(3, Date.valueOf(login.getDateCreated()));
        addLoginStmt.setString(4, login.getUserType());
        if (login.getLastLoginDate() == null)
            addLoginStmt.setNull(5, DATE);
        else
            addLoginStmt.setDate(5, (Date.valueOf(login.getLastLoginDate())));
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
        addPersonStmt.executeUpdate();
    }

    public void addStudent(Student student) throws SQLException {
        addPerson(student);
        addStudentStmt.setDate(1, Date.valueOf(student.getBirthDate()));
        addStudentStmt.setInt(2, student.getSemester().getId());
        addStudentStmt.setInt(3, student.getCourse().getId());
        if (student.getPauseDate() == null)
            addStudentStmt.setNull(4, DATE);
        else
            addStudentStmt.setDate(4, (Date.valueOf(student.getPauseDate())));
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
        if (subject.getReqSubject() == null)
            updateSubjectStmt.setNull(5, INTEGER);
        else
            updateSubjectStmt.setInt(5, subject.getReqSubject().getId());
        updateSubjectStmt.setInt(6, subject.getId());
        updateSubjectStmt.executeUpdate();
    }

    public void updatePerson(Person person) throws SQLException {
        updateLogin(person.getLogin());
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
        if (student.getPauseDate() == null)
            updateStudentStmt.setNull(4, DATE);
        else
            updateStudentStmt.setDate(4, Date.valueOf(student.getPauseDate()));
        updateStudentStmt.setInt(5, student.getId());
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
        updateCurriculumStmt.setInt(5, curriculum.getId());
        updateCurriculumStmt.executeUpdate();
    }

    public void updateGrade(Grade grade) throws SQLException {
        updateGradeStmt.setInt(1, grade.getStudent().getId());
        updateGradeStmt.setInt(2, grade.getSubject().getId());
        updateGradeStmt.setFloat(3, grade.getPoints());
        if (grade.getScore() <= 5)
            updateGradeStmt.setNull(4, INTEGER);
        else
            updateGradeStmt.setInt(4, grade.getScore());
        if (grade.getGradeDate() == null)
            updateGradeStmt.setNull(5, DATE);
        else
            updateGradeStmt.setDate(5, Date.valueOf(grade.getGradeDate()));
        if (grade.getProfessor() == null)
            updateGradeStmt.setInt(6, INTEGER);
        else
            updateGradeStmt.setInt(6, grade.getProfessor().getId());
        updateGradeStmt.setInt(7, grade.getId());
        updateGradeStmt.executeUpdate();
    }

    public void updateLogin(Login login) throws SQLException {
        updateLoginStmt.setString(1, login.getUsername());
        updateLoginStmt.setString(2, login.getPassword());
        if (login.getDateCreated() == null)
            updateLoginStmt.setNull(3, DATE);
        else
            updateLoginStmt.setDate(3, Date.valueOf(login.getDateCreated()));
        updateLoginStmt.setString(4, login.getUserType());
        if (login.getLastLoginDate() == null)
            updateLoginStmt.setNull(5, DATE);
        else
            updateLoginStmt.setDate(5, Date.valueOf(login.getLastLoginDate()));
        updateLoginStmt.setInt(6, login.getId());
        updateLoginStmt.executeUpdate();
    }

    public void deleteSubject(Subject subject) throws SQLException {
        deleteSubjectStmt.setInt(1, subject.getId());
        deleteSubjectStmt.executeUpdate();
    }

    private void deletePerson(Person person) throws SQLException {
        deletePersonStmt.setInt(1, person.getId());
        deletePersonStmt.executeUpdate();
        deleteLogin(person.getLogin());
    }

    private void deleteLogin(Login login) throws SQLException {
        deleteLoginSmt.setInt(1, login.getId());
        deleteLoginSmt.executeUpdate();
    }

    public void deleteProfessor(Professor professor) throws SQLException {
        deletePerson(professor);
    }

    public void deleteStudent(Student student) throws SQLException {
        deletePerson(student);
    }

    public void deleteAdmin(Administrator administrator) throws SQLException {
        deletePerson(administrator);
    }

    public void deleteCourse(Course course) throws SQLException {
        deleteCourseStmt.setInt(1, course.getId());
        deleteCourseStmt.executeUpdate();
    }

    public void deleteCurriculum(Curriculum curriculum) throws SQLException {
        deleteCurriculumStmt.setInt(1, curriculum.getId());
        deleteCurriculumStmt.executeUpdate();
    }

    private int getCountInfo(int id, PreparedStatement preparedStatement) throws SQLException {
        int no = 0;
        preparedStatement.setInt(1, id);
        var resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            no = resultSet.getInt(1);
        return no;
    }

    private float getAvgInfo(int id, PreparedStatement preparedStatement) throws SQLException {
        float average = 0;
        preparedStatement.setInt(1, id);
        var resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            average = resultSet.getInt(1);
        return average;
    }

    public float getAvgStudentGrade(Student student) throws SQLException {
        return getAvgInfo(student.getId(), getavgStudentGradeStmt);
    }

    public int getNoStudentGraded(Student student) throws SQLException {
        return getCountInfo(student.getId(), getNoStudentGradedStmt);
    }

    public int getNoStudentNotGraded(Student student) throws SQLException {
        return getCountInfo(student.getId(), getNoStudentNotGradedStmt);
    }

    public float getAvgSubjectGrade(Subject subject) throws SQLException {
        return getAvgInfo(subject.getId(), getavgSubjectGradeStmt);
    }

    public int getNoSubjectGraded(Subject subject) throws SQLException {
        return getCountInfo(subject.getId(), getNoSubjectGradedStmt);
    }

    public int getNoSubjectNotGraded(Subject subject) throws SQLException {
        return getCountInfo(subject.getId(), getNoSubjectNotGradedStmt);
    }

    public float getAvgProfessorGrade(Professor professor) throws SQLException {
        return getAvgInfo(professor.getId(), getavgProfessorGradeStmt);
    }

    public int getNoProfessorGraded(Professor professor) throws SQLException {
        return getCountInfo(professor.getId(), getNoProfessorGradedStmt);
    }

    public int getNoProfessorNotGraded(Professor professor) throws SQLException {
        return getCountInfo(professor.getId(), getNoProfessorNotGradedStmt);
    }

    public float getAvgCourseGrade(Course course) throws SQLException {
        return getAvgInfo(course.getId(), getavgCourseGradeStmt);
    }

    public int getNoStudentsOnCourse(Course course) throws SQLException {
        return getCountInfo(course.getId(), getNoStudentsOnCourseStmt);
    }

    private Student getStudentInfo(Student student, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            student = new Student();
            setPersonInfo(student, resultSet);
            student.setBirthDate(resultSet.getDate(9).toLocalDate());
            student.setSemester(getSemester(resultSet.getInt(10)));
            student.setCourse(getCourse(resultSet.getInt(11)));
            student.setPauseDate((resultSet.getDate(12) == null) ? null : (resultSet.getDate(12).toLocalDate()));
        }
        return student;
    }

    public Student getStudent(Login login) {
        Student student = null;
        try {
            getStudentLoginStmt.setInt(1, login.getId());
            var resultSet = getStudentLoginStmt.executeQuery();
            student = getStudentInfo(student, resultSet);
        } catch (SQLException ignored) {
            return null;
        }
        return student;
    }

    public Student getStudent(int id) {
        Student student = null;
        try {
            getStudentStmt.setInt(1, id);
            var resultSet = getStudentStmt.executeQuery();
            student = getStudentInfo(student, resultSet);
        } catch (SQLException ignored) {
            return null;
        }
        return student;
    }

    public ArrayList<Subject> subjects() throws SQLException {
        ArrayList<Subject> subjects = new ArrayList<>();
        var resultSet = allSubjectStmt.executeQuery();
        while (resultSet.next()) {
            Subject subject = getSubject(resultSet.getInt(1));
            subjects.add(subject);
        }
        return subjects;
    }

    public ArrayList<SubjectGradeWrapper> subjects(Student student) throws SQLException {
        return getSubjectGrades(student, allSubjectStudentStmt);
    }

    public ArrayList<SubjectGradeWrapper> subjectsPassed(Student student) throws SQLException {
        return getSubjectGrades(student, allSubjectPassedStudentStmt);
    }

    private ArrayList<SubjectGradeWrapper> getSubjectGrades(Student student, PreparedStatement preparedStatement) throws SQLException {
        ArrayList<SubjectGradeWrapper> subjectGradeWrappers = new ArrayList<>();
        preparedStatement.setInt(1, student.getId());
        var resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Subject subject = getSubject(resultSet.getInt(1));
            Grade grade = getGrade(resultSet.getInt(2));
            SubjectGradeWrapper subjectGradeWrapper = new SubjectGradeWrapper(subject, grade);
            subjectGradeWrappers.add(subjectGradeWrapper);
        }
        return subjectGradeWrappers;
    }

    private Grade getGrade(int id) {
        Grade grade = null;
        try {
            getGradeStmt.setInt(1, id);
            var resultSet = getGradeStmt.executeQuery();
            while (resultSet.next()) {
                grade = new Grade();
                grade.setId(resultSet.getInt(1));
                grade.setStudent(getStudent(resultSet.getInt(2)));
                grade.setSubject(getSubject(resultSet.getInt(3)));
                grade.setPoints(resultSet.getFloat(4));
                grade.setScore(resultSet.getInt(5));
                grade.setGradeDate((resultSet.getDate(6) == null) ? null : resultSet.getDate(6).toLocalDate());
                grade.setProfessor(getProfessor(resultSet.getInt(7)));
            }
        } catch (SQLException ignored) {
            return null;
        }
        return grade;
    }

    public ArrayList<SubjectWrapper> subjects(Professor professor) throws SQLException {
        ArrayList<SubjectWrapper> subjectWrappers = new ArrayList<>();
        allSubjectProfessorStmt.setInt(1, professor.getId());
        var resultSet = allSubjectProfessorStmt.executeQuery();
        while (resultSet.next()) {
            Subject subject = getSubject(resultSet.getInt(1));
            SubjectWrapper subjectWrapper = new SubjectWrapper(subject, getNoSubjectNotGraded(subject));
            subjectWrappers.add(subjectWrapper);
        }
        return subjectWrappers;
    }

    public ArrayList<StudentGradeSubjectWrapper> students(Professor professor) throws SQLException {
        ArrayList<StudentGradeSubjectWrapper> studentGradeSubjectWrappers = new ArrayList<>();
        allStudentProfessorStmt.setInt(1, professor.getId());
        var resultSet = allStudentProfessorStmt.executeQuery();
        while (resultSet.next()) {
            Student student = getStudent(resultSet.getInt(1));
            Grade grade = getGrade(resultSet.getInt(2));
            StudentGradeSubjectWrapper studentGradeSubjectWrapper = new StudentGradeSubjectWrapper(student, grade);
            studentGradeSubjectWrappers.add(studentGradeSubjectWrapper);
        }
        return studentGradeSubjectWrappers;
    }

    private Professor getProfessorInfo(Professor professor, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            professor = new Professor();
            setPersonInfo(professor, resultSet);
            professor.setTitle(resultSet.getString(9));
        }
        return professor;
    }

    public Professor getProfessor(Login login) {
        Professor professor = null;
        try {
            getProfessorLoginStmt.setInt(1, login.getId());
            var resultSet = getProfessorLoginStmt.executeQuery();
            professor = getProfessorInfo(professor, resultSet);
        } catch (SQLException ignored) {
            return null;
        }
        return professor;
    }

    public Professor getProfessor(int id) {
        Professor professor = null;
        try {
            getProfessorStmt.setInt(1, id);
            var resultSet = getProfessorStmt.executeQuery();
            professor = getProfessorInfo(professor, resultSet);
        } catch (SQLException ignored) {
            return null;
        }
        return professor;
    }

    public ArrayList<Semester> semesters() throws SQLException {
        ArrayList<Semester> semesters = new ArrayList<>();
        var resultSet = allSemesterStmt.executeQuery();
        while (resultSet.next()) {
            Semester semester = getSemester(resultSet.getInt(1));
            semesters.add(semester);
        }
        return semesters;
    }

    public ArrayList<String> cycles() throws SQLException {
        ArrayList<String> cycles = new ArrayList<>();
        var resultSet = allCyclesStmt.executeQuery();
        while (resultSet.next()) {
            String cycle = String.valueOf(resultSet.getInt(1));
            cycles.add(cycle);
        }
        return cycles;
    }

    public ArrayList<String> getSemestersOnCycle(int cycleNo) throws SQLException {
        ArrayList<String> semesters = new ArrayList<>();
        getSemestersCycleStmt.setInt(1, cycleNo);
        var resultSet = getSemestersCycleStmt.executeQuery();
        while (resultSet.next()) {
            String semester = String.valueOf(resultSet.getInt(1));
            semesters.add(semester);
        }
        return semesters;
    }

    public ArrayList<Student> getStudents(String selectedCourse, String selectedCycle, String selectedSemester) throws SQLException {
        ArrayList<Student> students = new ArrayList<>();
        String getStudentsQuery = "SELECT STUDENT.ID FROM FP18120.PERSON, FP18120.STUDENT, FP18120.COURSE, FP18120.SEMESTER WHERE PERSON.ID = STUDENT.ID AND COURSE_ID = COURSE.ID AND SEMESTER.ID = SEMESTER_ID";
        if (!selectedCourse.equals("Svi smjerovi"))
            getStudentsQuery += " AND NAME=?";
        if (!selectedCycle.equals("Svi ciklusi"))
            getStudentsQuery += " AND CYCLE_NO=?";
        if (!selectedSemester.equals("Svi semestri"))
            getStudentsQuery += " AND NO=?";
        getStudentsStmt = conn.prepareStatement(getStudentsQuery);
        int indeks = 1;
        if (!selectedCourse.equals("Svi smjerovi"))
            getStudentsStmt.setString(indeks++, selectedCourse);
        if (!selectedCycle.equals("Svi ciklusi"))
            getStudentsStmt.setInt(indeks++, Integer.valueOf(selectedCycle));
        if (!selectedSemester.equals("Svi semestri"))
            getStudentsStmt.setInt(indeks, Integer.valueOf(selectedSemester));
        var resultSet = getStudentsStmt.executeQuery();
        while (resultSet.next()) {
            Student student = getStudent(resultSet.getInt(1));
            students.add(student);
        }
        return students;
    }

    public ArrayList<Subject> getSubjects(Semester semester, Course course, boolean required) throws SQLException {
        ArrayList<Subject> subjects = new ArrayList<>();
        getSubjectSemesterStmt.setInt(1, semester.getId());
        if (required)
            getSubjectSemesterStmt.setString(2, "Da");
        else
            getSubjectSemesterStmt.setString(2, "Ne");
        getSubjectSemesterStmt.setInt(3, course.getId());
        var resultSet = getSubjectSemesterStmt.executeQuery();
        while (resultSet.next()) {
            Subject subject = getSubject(resultSet.getInt(1));
            subjects.add(subject);
        }
        return subjects;
    }

    public void advanceStudent(Student student, ObservableList<Subject> subjects) throws SQLException {
        addGradeStmt.setInt(1, student.getId());
        addGradeStmt.setInt(3, 10);
        addGradeStmt.setNull(4, INTEGER);
        addGradeStmt.setNull(5, DATE);
        addGradeStmt.setNull(6, INTEGER);
        for (Subject subject : subjects) {
            addGradeStmt.setInt(2, subject.getId());
            addGradeStmt.executeUpdate();
        }
    }

    public int getLoginId(String firstName, String lastName, String email, String userType) throws SQLException {
        getLoginIdStmt.setString(1, firstName);
        getLoginIdStmt.setString(2, lastName);
        getLoginIdStmt.setString(3, email);
        getLoginIdStmt.setString(4, userType);
        var resultSet = getLoginIdStmt.executeQuery();
        int id = -1;
        while (resultSet.next())
            id = resultSet.getInt(1);
        return id;
    }
}

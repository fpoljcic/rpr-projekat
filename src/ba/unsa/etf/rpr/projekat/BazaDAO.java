package ba.unsa.etf.rpr.projekat;

import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;

public class BazaDAO {
    private static BazaDAO instance = null;
    private PreparedStatement getLoginStmt, getProfessorStmt, getStudentStmt, getCourseStmt, getSemesterStmt, getSubjectStmt;
    private PreparedStatement addSubjectStmt, addProfessorStmt, addStudentStmt, addCourseStmt, addCurriculumStmt, addPersonStmt;
    private PreparedStatement allSubjectStmt, allProfessorStmt, allStudentStmt, allCourseStmt, allCurriculumStmt;
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
            getLoginStmt = conn.prepareStatement("SELECT * FROM FP18120.LOGIN WHERE USERNAME=? AND PASSWORD=? AND USER_TYPE=?");
            getProfessorStmt = conn.prepareStatement("SELECT * FROM FP18120.PERSON, FP18120.PROFESSOR WHERE PROFESSOR.ID=? AND PERSON.ID=PROFESSOR.ID");
            getStudentStmt = conn.prepareStatement("SELECT * FROM FP18120.PERSON, FP18120.STUDENT WHERE STUDENT.ID=? AND PERSON.ID=STUDENT.ID");
            getCourseStmt = conn.prepareStatement("SELECT * FROM FP18120.COURSE WHERE ID=?");
            getSemesterStmt = conn.prepareStatement("SELECT * FROM FP18120.SEMESTER WHERE ID=?");
            getSubjectStmt = conn.prepareStatement("SELECT * FROM FP18120.SUBJECT WHERE ID=?");

            addSubjectStmt = conn.prepareStatement("INSERT INTO FP18120.SUBJECT VALUES (FP18120.SUBJECT_SEQ.NEXTVAL, ?, ?, ?, ?)");
            addPersonStmt = conn.prepareStatement("INSERT INTO FP18120.PERSON VALUES (FP18120.PERSON_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)");
            addProfessorStmt = conn.prepareStatement("INSERT INTO FP18120.PROFESSOR VALUES (FP18120.PERSON_SEQ.CURRVAL, ?)");
            addStudentStmt = conn.prepareStatement("INSERT INTO FP18120.STUDENT VALUES (FP18120.PERSON_SEQ.CURRVAL, ?, ?, ?)");
            addCourseStmt = conn.prepareStatement("INSERT INTO FP18120.COURSE VALUES (FP18120.COURSE_SEQ.NEXTVAL, ?)");
            addCurriculumStmt = conn.prepareStatement("INSERT INTO FP18120.CURRICULUM VALUES (FP18120.CURRICULUM_SEQ.NEXTVAL, ?, ?, ?, ?)");


            allSubjectStmt = conn.prepareStatement("SELECT * FROM FP18120.SUBJECT");
            allProfessorStmt = conn.prepareStatement("SELECT * FROM FP18120.PERSON, FP18120.PROFESSOR WHERE PERSON.ID=PROFESSOR.ID");
            allStudentStmt = conn.prepareStatement("SELECT * FROM FP18120.PERSON, FP18120.STUDENT WHERE PERSON.ID=STUDENT.ID");
            allCourseStmt = conn.prepareStatement("SELECT * FROM FP18120.COURSE");
            allCurriculumStmt = conn.prepareStatement("SELECT * FROM FP18120.CURRICULUM");
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

    public Login getLogin(String username, String password, String user_type) {
        Login login = null;
        try {
            getLoginStmt.setString(1, username);
            getLoginStmt.setString(2, password);
            getLoginStmt.setString(3, user_type);
            var resultSet = getLoginStmt.executeQuery();
            while (resultSet.next()) {
                login = new Login();
                login.setId(resultSet.getInt(1));
                login.setUsername(resultSet.getString(2));
                login.setPassword(resultSet.getString(3));
                login.setDateCreated(resultSet.getDate(4).toLocalDate());
                login.setUserType(resultSet.getString(5));
            }
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
                professor.setTitle(resultSet.getString(9));
            }
        } catch (SQLException ignored) {
            return null;
        }
        return professor;
    }

    public ArrayList<Subject> subjects () {
        ArrayList<Subject> subjects = new ArrayList<>();
        try {
            var resultSet = allSubjectStmt.executeQuery();
            while (resultSet.next()) {
                Subject subject = new Subject();
                subject.setId(resultSet.getInt(1));
                subject.setName(resultSet.getString(2));
                subject.setCode(resultSet.getString(3));
                subject.setEcts(resultSet.getInt(4));
                subject.setProfessor(getProfessor(resultSet.getInt(5)));
                subjects.add(subject);
            }
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
        }
        return subjects;
    }

    public ArrayList<Professor> professors () {
        ArrayList<Professor> professors = new ArrayList<>();
        try {
            var resultSet = allProfessorStmt.executeQuery();
            while (resultSet.next()) {
                Professor professor = new Professor();
                professor.setId(resultSet.getInt(1));
                professor.setFirstName(resultSet.getString(2));
                professor.setLastName(resultSet.getString(3));
                professor.setJmbg(resultSet.getString(4));
                professor.setAddress(resultSet.getString(5));
                professor.setEmail(resultSet.getString(6));
                professor.setTitle(resultSet.getString(9));
                professors.add(professor);
            }
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
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

    public ArrayList<Student> students () {
        ArrayList<Student> students = new ArrayList<>();
        try {
            var resultSet = allStudentStmt.executeQuery();
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt(1));
                student.setFirstName(resultSet.getString(2));
                student.setLastName(resultSet.getString(3));
                student.setJmbg(resultSet.getString(4));
                student.setAddress(resultSet.getString(5));
                student.setEmail(resultSet.getString(6));
                student.setBirthDate(resultSet.getDate(9).toLocalDate());
                student.setSemester(getSemester(resultSet.getInt(10)));
                student.setCourse(getCourse(resultSet.getInt(11)));
                students.add(student);
            }
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
        }
        return students;
    }

    public ArrayList<Course> courses () {
        ArrayList<Course> courses = new ArrayList<>();
        try {
            var resultSet = allCourseStmt.executeQuery();
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt(1));
                course.setName(resultSet.getString(2));
                courses.add(course);
            }
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
        }
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
            }
        } catch (SQLException ignored) {
            return null;
        }
        return subject;
    }


    public ArrayList<Curriculum> curriculums () {
        ArrayList<Curriculum> curriculums = new ArrayList<>();
        try {
            var resultSet = allCurriculumStmt.executeQuery();
            while (resultSet.next()) {
                Curriculum curriculum = new Curriculum();
                curriculum.setId(resultSet.getInt(1));
                curriculum.setCourse(getCourse(resultSet.getInt(2)));
                curriculum.setSemester(getSemester(resultSet.getInt(3)));
                curriculum.setMainSubject(getSubject(resultSet.getInt(4)));
                curriculum.setSecondarySubject(getSubject(resultSet.getInt(5)));
                curriculums.add(curriculum);
            }
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
        }
        return curriculums;
    }

    public void addSubject(Subject subject) {
        try {
            addSubjectStmt.setInt(1, subject.getId());
            addSubjectStmt.setString(2, subject.getName());
            addSubjectStmt.setString(3, subject.getCode());
            addSubjectStmt.setInt(4, subject.getEcts());
            addSubjectStmt.setInt(5, subject.getProfessor().getId());
            addSubjectStmt.executeUpdate();
        } catch (SQLException error) {
            showAlert("Greška", "Problem pri dodavanju: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void addProfessor(Professor professor) {
        try {
            addPerson(professor);
            addProfessorStmt.setString(1, professor.getTitle());
            addProfessorStmt.executeUpdate();
        } catch (SQLException error) {
            showAlert("Greška", "Problem pri dodavanju: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void addPerson(Person person) throws SQLException {
        addPersonStmt.setInt(1, person.getId());
        addPersonStmt.setString(2, person.getFirstName());
        addPersonStmt.setString(3, person.getLastName());
        addPersonStmt.setString(4, person.getJmbg());
        addPersonStmt.setString(5, person.getAddress());
        addPersonStmt.setString(6, person.getEmail());
        addPersonStmt.setInt(7, person.getLogin().getId());
        addPersonStmt.executeUpdate();
    }

    public void addStudent(Student student) {
        try {
            addPerson(student);
            addStudentStmt.setDate(1, Date.valueOf(student.getBirthDate()));
            addStudentStmt.setInt(2, student.getSemester().getId());
            addStudentStmt.setInt(3, student.getCourse().getId());
            addStudentStmt.executeUpdate();
        } catch (SQLException error) {
            showAlert("Greška", "Problem pri dodavanju: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void addCourse(Course course) {
        try {
            addCourseStmt.setInt(1, course.getId());
            addCourseStmt.setString(2, course.getName());
            addCourseStmt.executeUpdate();
        } catch (SQLException error) {
            showAlert("Greška", "Problem pri dodavanju: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void addCurriculum(Curriculum curriculum) {
        try {
            addCurriculumStmt.setInt(1, curriculum.getId());
            addCurriculumStmt.setInt(2, curriculum.getCourse().getId());
            addCurriculumStmt.setInt(3, curriculum.getSemester().getId());
            addCurriculumStmt.setInt(4, curriculum.getMainSubject().getId());
            addCurriculumStmt.setInt(5, curriculum.getSecondarySubject().getId());
            addCurriculumStmt.executeUpdate();
        } catch (SQLException error) {
            showAlert("Greška", "Problem pri dodavanju: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }
}

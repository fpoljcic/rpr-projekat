package ba.unsa.etf.rpr.projekat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class AdministratorController {
    public TableView<Subject> subjectTable;
    public TableView<Professor> professorTable;
    public TableView<Student> studentTable;
    public TableView<Course> courseTable;
    public TableView<Curriculum> curriculumTable;
    public TableColumn<Subject, Integer> subjectIdCol;
    public TableColumn<Subject, String> subjectNameCol;
    public TableColumn<Subject, String> subjectCodeCol;
    public TableColumn<Subject, Integer> subjectEctsCol;
    public TableColumn<Subject, Professor> subjectProfessorCol;
    public TableColumn<Professor, Integer> professorIdCol;
    public TableColumn<Professor, String> professorFirstNameCol;
    public TableColumn<Professor, String> professorLastNameCol;
    public TableColumn<Professor, String> professorJmbgCol;
    public TableColumn<Professor, String> professorAdressCol;
    public TableColumn<Professor, String> professorEmailCol;
    public TableColumn<Professor, String> professorTitleCol;
    public TableColumn<Student, Integer> studentIdCol;
    public TableColumn<Student, String> studentFirstNameCol;
    public TableColumn<Student, String> studentLastNameCol;
    public TableColumn<Student, String> studentJmbgCol;
    public TableColumn<Student, String> studentAdressCol;
    public TableColumn<Student, String> studentEmailCol;
    public TableColumn<Student, LocalDate> studentBirthDateCol;
    public TableColumn<Student, Semester> studentSemesterCol;
    public TableColumn<Student, Course> studentCourseCol;
    public TableColumn<Course, Integer> courseIdCol;
    public TableColumn<Course, String> courseNameCol;
    public TableColumn<Curriculum, Integer> curriculumIdCol;
    public TableColumn<Curriculum, Course> curriculumCourseCol;
    public TableColumn<Curriculum, Semester> curriculumSemesterCol;
    public TableColumn<Curriculum, Subject> curriculumSubjectCol;
    public TableColumn<Curriculum, String> curriculumRequiredSubjectCol;
    public TabPane tabPane;
    public Label noSubjectStudentField, avgSubjectGradeField, noSubjectGradedField, noSubjectNotGradedField, percentSubjectPassedField;
    private Login login;
    private BazaDAO dataBase;
    private Subject selectedSubject;
    private Professor selectedProfessor;
    private Student selectedStudent;
    private Course selectedCourse;
    private Curriculum selectedCurriculum;
    private Tab currentTab;


    public AdministratorController(Login login) {
        this.login = login;
        dataBase = BazaDAO.getInstance();
    }

    private void showAlert(String title, String headerText, Alert.AlertType type) {
        Alert error = new Alert(type);
        error.setTitle(title);
        error.setHeaderText(headerText);
        error.show();
    }

    private void fillSubjects() throws SQLException {
        subjectTable.setItems(FXCollections.observableArrayList(dataBase.subjects()));
        subjectIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        subjectNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        subjectCodeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        subjectEctsCol.setCellValueFactory(new PropertyValueFactory<>("ects"));
        subjectProfessorCol.setCellValueFactory(new PropertyValueFactory<>("professor"));
    }

    private void fillProfessors() throws SQLException {
        professorTable.setItems(FXCollections.observableArrayList(dataBase.professors()));
        professorIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        professorFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        professorLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        professorJmbgCol.setCellValueFactory(new PropertyValueFactory<>("jmbg"));
        professorAdressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        professorEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        professorTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
    }

    private void fillStudents() throws SQLException {
        studentTable.setItems(FXCollections.observableArrayList(dataBase.students()));
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        studentLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        studentJmbgCol.setCellValueFactory(new PropertyValueFactory<>("jmbg"));
        studentAdressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        studentEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        studentBirthDateCol.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        studentSemesterCol.setCellValueFactory(new PropertyValueFactory<>("semester"));
        studentCourseCol.setCellValueFactory(new PropertyValueFactory<>("course"));
    }

    private void fillCourses() throws SQLException {
        courseTable.setItems(FXCollections.observableArrayList(dataBase.courses()));
        courseIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        courseNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void fillCurriculums() throws SQLException {
        curriculumTable.setItems(FXCollections.observableArrayList(dataBase.curriculums()));
        curriculumIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        curriculumCourseCol.setCellValueFactory(new PropertyValueFactory<>("course"));
        curriculumSemesterCol.setCellValueFactory(new PropertyValueFactory<>("semester"));
        curriculumSubjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        curriculumRequiredSubjectCol.setCellValueFactory(new PropertyValueFactory<>("requiredSubject"));
    }

    private void setSelectedItems() {
        subjectTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedSubject = subjectTable.getSelectionModel().getSelectedItem();
                Thread thread = new Thread(() -> {
                    try {
                        String noSubjectStudent = String.valueOf(dataBase.getNoStudentsOnSubject(newValue));
                        String avgSubjectGrade = String.valueOf(dataBase.getAvgSubjectGrade(newValue));
                        String noSubjectGraded = String.valueOf(dataBase.getNoSubjectGraded(newValue));
                        String noSubjectNotGraded = String.valueOf(dataBase.getNoSubjectNotGraded(newValue));
                        Float percentSubjectPassed = dataBase.getPercentSubjectPassed(newValue);
                        Platform.runLater(() -> {
                            noSubjectStudentField.setText(noSubjectStudent);
                            avgSubjectGradeField.setText(avgSubjectGrade);
                            noSubjectGradedField.setText(noSubjectGraded);
                            noSubjectNotGradedField.setText(noSubjectNotGraded);
                            percentSubjectPassedField.setText(percentSubjectPassed + " %");
                        });
                    } catch (SQLException error) {
                        showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
                    }
                });
                thread.start();
            }
        });
        professorTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedProfessor = professorTable.getSelectionModel().getSelectedItem();
            }
        });
        studentTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedStudent = studentTable.getSelectionModel().getSelectedItem();
            }
        });
        courseTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedCourse = courseTable.getSelectionModel().getSelectedItem();
            }
        });
        curriculumTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedCurriculum = curriculumTable.getSelectionModel().getSelectedItem();
            }
        });
    }

    @FXML
    public void initialize() {
        try {
            fillSubjects();
            fillProfessors();
            fillStudents();
            fillCourses();
            fillCurriculums();
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
        }
        setSelectedItems();
        currentTab = tabPane.getSelectionModel().getSelectedItem();
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentTab = newValue;
            }
        });

    }

    private void editSubject(Subject subject) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addSubject.fxml"));
            loader.setController(new AddSubjectController(subject));
            Parent root = loader.load();
            Stage secondaryStage = new Stage();
            if (subject == null)
                secondaryStage.setTitle("Dodaj predmet");
            else
                secondaryStage.setTitle("Ažuriraj predmet");
            secondaryStage.getIcons().add(new Image("/img/subject.png"));
            secondaryStage.setResizable(false);
            secondaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.showAndWait();
            subjectTable.setItems(FXCollections.observableArrayList(dataBase.subjects()));
        } catch (IOException | SQLException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void addSubject(ActionEvent actionEvent) {
        editSubject(null);
    }

    public void updateSubject(ActionEvent actionEvent) {
        if (selectedSubject == null) {
            showAlert("Greška", "Prvo odaberite predmet", Alert.AlertType.ERROR);
            return;
        }
        editSubject(selectedSubject);
    }

    public void deleteSubject(ActionEvent actionEvent) {
        if (selectedSubject == null) {
            showAlert("Greška", "Prvo odaberite predmet", Alert.AlertType.ERROR);
            return;
        }
        try {
            dataBase.deleteSubject(selectedSubject);
        } catch (SQLException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        showAlert("Uspjeh", "Uspješno izbrisan predmet", Alert.AlertType.INFORMATION);
    }

    public void addProfessor(ActionEvent actionEvent) {

    }

    public void addStudent(ActionEvent actionEvent) {

    }

    public void addCourse(ActionEvent actionEvent) {

    }

    public void addCurriculum(ActionEvent actionEvent) {

    }

    public void closeClick(ActionEvent actionEvent) {
        BazaDAO.removeInstance();
        Platform.exit();
    }


    public void logOutClick(ActionEvent actionEvent) {
        BazaDAO.removeInstance();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("Prijava");
            loginStage.getIcons().add(new Image("/img/login.png"));
            loginStage.setResizable(false);
            loginStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            Stage currentStage = (Stage) tabPane.getScene().getWindow();
            currentStage.close();
            loginStage.show();
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void addClick(ActionEvent actionEvent) {
        switch (currentTab.getText()) {
            case "Predmeti":
                addSubject(null);
                break;
            case "Profesori":
                addProfessor(null);
                break;
            case "Studenti":
                addStudent(null);
                break;
            case "Smjerovi":
                addCourse(null);
                break;
            case "Programi":
                addCurriculum(null);
                break;
        }
    }

    public void updateClick(ActionEvent actionEvent) {
        switch (currentTab.getText()) {
            case "Predmeti":
                updateSubject(null);
                break;
                /*
            case "Profesori":
                updateProfessor(null);
                break;
            case "Studenti":
                updateStudent(null);
                break;
            case "Smjerovi":
                updateCourse(null);
                break;
            case "Programi":
                updateCurriculum(null);
                break;
                */
        }
    }

    public void deleteClick(ActionEvent actionEvent) {
        switch (currentTab.getText()) {
            case "Predmeti":
                deleteSubject(null);
                break;
                /*
            case "Profesori":
                deleteProfessor(null);
                break;
            case "Studenti":
                deleteStudent(null);
                break;
            case "Smjerovi":
                deleteCourse(null);
                break;
            case "Programi":
                deleteCurriculum(null);
                break;
                */
        }
    }

    public void aboutClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/about.fxml"));
            Parent root = loader.load();
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("O meni");
            secondaryStage.getIcons().add(new Image("/img/about.png"));
            secondaryStage.setResizable(false);
            secondaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.showAndWait();
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }
}

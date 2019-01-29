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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public TableColumn<Subject, Subject> subjectReqSubjectCol;
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
    public TableColumn<Student, LocalDate> studentPauseDateCol;
    public TableColumn<Course, Integer> courseIdCol;
    public TableColumn<Course, String> courseNameCol;
    public TableColumn<Curriculum, Integer> curriculumIdCol;
    public TableColumn<Curriculum, Course> curriculumCourseCol;
    public TableColumn<Curriculum, Semester> curriculumSemesterCol;
    public TableColumn<Curriculum, Subject> curriculumSubjectCol;
    public TableColumn<Curriculum, String> curriculumRequiredSubjectCol;
    public TabPane tabPane;
    public Label noSubjectStudentField, avgSubjectGradeField, noSubjectGradedField, noSubjectNotGradedField, percentSubjectPassedField;
    public Label noProfessorStudentField, avgProfessorGradeField, noProfessorGradedField, noProfessorNotGradedField, percentProfessorPassedField;
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
        subjectReqSubjectCol.setCellValueFactory(new PropertyValueFactory<>("reqSubject"));
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
        studentPauseDateCol.setCellValueFactory(new PropertyValueFactory<>("pauseDate"));
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
                        Platform.runLater(() -> showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR));
                    }
                });
                thread.start();
            }
        });
        professorTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedProfessor = professorTable.getSelectionModel().getSelectedItem();
                Thread thread = new Thread(() -> {
                    try {
                        String noProfessorStudent = String.valueOf(dataBase.getNoStudentsOnProfessor(newValue));
                        String avgProfessorGrade = String.valueOf(dataBase.getAvgProfessorGrade(newValue));
                        String noProfessorGraded = String.valueOf(dataBase.getNoProfessorGraded(newValue));
                        String noProfessorNotGraded = String.valueOf(dataBase.getNoProfessorNotGraded(newValue));
                        Float percentProfessorPassed = dataBase.getPercentProfessorPassed(newValue);
                        Platform.runLater(() -> {
                            noProfessorStudentField.setText(noProfessorStudent);
                            avgProfessorGradeField.setText(avgProfessorGrade);
                            noProfessorGradedField.setText(noProfessorGraded);
                            noProfessorNotGradedField.setText(noProfessorNotGraded);
                            percentProfessorPassedField.setText(percentProfessorPassed + " %");
                        });
                    } catch (SQLException error) {
                        Platform.runLater(() -> showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR));
                    }
                });
                thread.start();
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

    private void clearSelectedSubject() {
        subjectTable.getSelectionModel().clearSelection();
        selectedSubject = null;
    }

    private void clearSelectedProfessor() {
        professorTable.getSelectionModel().clearSelection();
        selectedProfessor = null;
    }

    private void clearSelectedStudent() {
        studentTable.getSelectionModel().clearSelection();
        selectedStudent = null;
    }

    public void addSubject(ActionEvent actionEvent) {
        clearSelectedSubject();
        editSubject(null);
    }

    public void updateSubject(ActionEvent actionEvent) {
        if (selectedSubject == null) {
            showAlert("Greška", "Prvo odaberite predmet", Alert.AlertType.ERROR);
            return;
        }
        editSubject(selectedSubject);
        clearSelectedSubject();
    }

    public void deleteSubject(ActionEvent actionEvent) {
        if (selectedSubject == null) {
            showAlert("Greška", "Prvo odaberite predmet", Alert.AlertType.ERROR);
            return;
        }
        try {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Potvrda");
            confirmationAlert.setHeaderText("Da li ste sigurni da želite izbrisati predmet '" + selectedSubject + "'?");
            confirmationAlert.showAndWait();
            if (confirmationAlert.getResult() != ButtonType.OK)
                return;
            dataBase.deleteSubject(selectedSubject);
            subjectTable.setItems(FXCollections.observableArrayList(dataBase.subjects()));
        } catch (SQLException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
            clearSelectedSubject();
            return;
        }
        showAlert("Uspjeh", "Uspješno izbrisan predmet", Alert.AlertType.INFORMATION);
        clearSelectedSubject();
    }

    private void editProfessor(Professor professor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addPerson.fxml"));
            loader.setController(new AddPersonController(selectedProfessor, "Profesor"));
            Parent root = loader.load();
            Stage secondaryStage = new Stage();
            if (professor == null)
                secondaryStage.setTitle("Dodaj profesora");
            else
                secondaryStage.setTitle("Ažuriraj profesora");
            secondaryStage.getIcons().add(new Image("/img/professor.png"));
            secondaryStage.setResizable(false);
            secondaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.showAndWait();
            professorTable.setItems(FXCollections.observableArrayList(dataBase.professors()));
        } catch (IOException | SQLException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void addProfessor(ActionEvent actionEvent) {
        clearSelectedProfessor();
        editProfessor(selectedProfessor);
    }

    public void updateProfessor(ActionEvent actionEvent) {
        if (selectedProfessor == null) {
            showAlert("Greška", "Prvo odaberite profesora", Alert.AlertType.ERROR);
            return;
        }
        editProfessor(selectedProfessor);
        clearSelectedProfessor();
    }

    public void deleteProfessor(ActionEvent actionEvent) {
        if (selectedProfessor == null) {
            showAlert("Greška", "Prvo odaberite profesora", Alert.AlertType.ERROR);
            return;
        }
        try {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Potvrda");
            confirmationAlert.setHeaderText("Da li ste sigurni da želite izbrisati profesora '" + selectedProfessor + "'?");
            confirmationAlert.showAndWait();
            if (confirmationAlert.getResult() != ButtonType.OK)
                return;
            dataBase.deleteProfessor(selectedProfessor);
            professorTable.setItems(FXCollections.observableArrayList(dataBase.professors()));
        } catch (SQLException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
            clearSelectedProfessor();
            return;
        }
        showAlert("Uspjeh", "Uspješno izbrisan profesor", Alert.AlertType.INFORMATION);
        clearSelectedProfessor();
    }

    private void editStudent(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addPerson.fxml"));
            loader.setController(new AddPersonController(selectedStudent, "Student"));
            Parent root = loader.load();
            Stage secondaryStage = new Stage();
            if (student == null)
                secondaryStage.setTitle("Dodaj studenta");
            else
                secondaryStage.setTitle("Ažuriraj studenta");
            secondaryStage.getIcons().add(new Image("/img/student.png"));
            secondaryStage.setResizable(false);
            secondaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.showAndWait();
            studentTable.setItems(FXCollections.observableArrayList(dataBase.students()));
        } catch (IOException | SQLException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void addStudent(ActionEvent actionEvent) {
        clearSelectedStudent();
        editStudent(selectedStudent);
    }

    public void updateStudent(ActionEvent actionEvent) {
        if (selectedStudent == null) {
            showAlert("Greška", "Prvo odaberite studenta", Alert.AlertType.ERROR);
            return;
        }
        editStudent(selectedStudent);
        clearSelectedStudent();
    }

    public void deleteStudent(ActionEvent actionEvent) {
        if (selectedStudent == null) {
            showAlert("Greška", "Prvo odaberite studenta", Alert.AlertType.ERROR);
            return;
        }
        try {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Potvrda");
            confirmationAlert.setHeaderText("Da li ste sigurni da želite izbrisati studenta '" + selectedStudent + "'?");
            confirmationAlert.showAndWait();
            if (confirmationAlert.getResult() != ButtonType.OK)
                return;
            dataBase.deleteStudent(selectedStudent);
            studentTable.setItems(FXCollections.observableArrayList(dataBase.students()));
        } catch (SQLException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
            clearSelectedProfessor();
            return;
        }
        showAlert("Uspjeh", "Uspješno izbrisan student", Alert.AlertType.INFORMATION);
        clearSelectedStudent();
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
            case "Profesori":
                updateProfessor(null);
                break;
            case "Studenti":
                updateStudent(null);
                break;
                /*
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
            case "Profesori":
                deleteProfessor(null);
                break;

            case "Studenti":
                deleteStudent(null);
                break;
                /*
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

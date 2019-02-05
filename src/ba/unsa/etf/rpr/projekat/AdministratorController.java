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
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class AdministratorController {
    public TableView<Subject> subjectTable;
    public TableView<Professor> professorTable;
    public TableView<Student> studentTable;
    public TableView<Course> courseTable;
    public TableView<Curriculum> curriculumTable;
    public TableView<Administrator> adminTable;
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
    public TableColumn<Administrator, Integer> adminIdCol;
    public TableColumn<Administrator, String> adminFirstNameCol;
    public TableColumn<Administrator, String> adminLastNameCol;
    public TableColumn<Administrator, String> adminJmbgCol;
    public TableColumn<Administrator, String> adminAdressCol;
    public TableColumn<Administrator, String> adminEmailCol;
    public TabPane tabPane;
    public Label noSubjectStudentField, avgSubjectGradeField, noSubjectGradedField, noSubjectNotGradedField, percentSubjectPassedField;
    public Label noProfessorStudentField, avgProfessorGradeField, noProfessorGradedField, noProfessorNotGradedField, percentProfessorPassedField;
    public ChoiceBox<String> cycleChoiceBox;
    public ChoiceBox<String> courseChoiceBox;
    public ChoiceBox<String> semeseterChoiceBox;
    public Button refreshButton;
    public Label avgStudentGradeField;
    public Label noStudentGradedField;
    public Label noStudentNotGradedField;
    public Label avgCourseGradeField;
    public Label noStudentOnCourseField;
    private Login login;
    private BazaDAO dataBase;
    private Subject selectedSubject;
    private Professor selectedProfessor;
    private Student selectedStudent;
    private Course selectedCourse;
    private Curriculum selectedCurriculum;
    private Administrator selectedAdmin;
    private Tab currentTab;
    private String selectedCycle, selectedCourseChoice, selectedSemester;


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

    private void fillAdmins() throws SQLException {
        adminTable.setItems(FXCollections.observableArrayList(dataBase.admins()));
        adminIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        adminFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        adminLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        adminJmbgCol.setCellValueFactory(new PropertyValueFactory<>("jmbg"));
        adminAdressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        adminEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
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

    private void setListenersOnSelectedItems() {
        selectedCycle = cycleChoiceBox.getValue();
        selectedCourseChoice = courseChoiceBox.getValue();
        selectedSemester = semeseterChoiceBox.getValue();
        subjectTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedSubject = subjectTable.getSelectionModel().getSelectedItem();
                Thread thread = new Thread(() -> {
                    try {
                        String avgSubjectGrade = String.valueOf(dataBase.getAvgSubjectGrade(newValue));
                        int noSubjectGradedInt = dataBase.getNoSubjectGraded(newValue);
                        String noSubjectGraded = String.valueOf(noSubjectGradedInt);
                        int noSubjectNotGradedInt = dataBase.getNoSubjectNotGraded(newValue);
                        String noSubjectNotGraded = String.valueOf(noSubjectNotGradedInt);
                        String noSubjectStudent = String.valueOf(noSubjectGradedInt + noSubjectNotGradedInt);
                        Float percentSubjectPassed = Math.round(noSubjectGradedInt * 10000f / (noSubjectGradedInt + noSubjectNotGradedInt)) / 100f;
                        Platform.runLater(() -> {
                            noSubjectStudentField.setText(noSubjectStudent);
                            avgSubjectGradeField.setText(avgSubjectGrade);
                            noSubjectGradedField.setText(noSubjectGraded);
                            noSubjectNotGradedField.setText(noSubjectNotGraded);
                            percentSubjectPassedField.setText(percentSubjectPassed + " %");
                        });
                    } catch (SQLException error) {
                        error.printStackTrace();
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
                        String avgProfessorGrade = String.valueOf(dataBase.getAvgProfessorGrade(newValue));
                        int noProfessorGradedInt = dataBase.getNoProfessorGraded(newValue);
                        String noProfessorGraded = String.valueOf(noProfessorGradedInt);
                        int noProfessorNotGradedInt = dataBase.getNoProfessorNotGraded(newValue);
                        String noProfessorNotGraded = String.valueOf(noProfessorNotGradedInt);
                        String noProfessorStudent = String.valueOf(noProfessorGradedInt + noProfessorNotGradedInt);
                        Float percentProfessorPassed = Math.round(noProfessorGradedInt * 10000f / (noProfessorGradedInt + noProfessorNotGradedInt)) / 100f;
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
                Thread thread = new Thread(() -> {
                    try {
                        String avgStudentGrade = String.valueOf(dataBase.getAvgStudentGrade(newValue));
                        String noStudentGraded = String.valueOf(dataBase.getNoStudentGraded(newValue));
                        String noStudentNotGraded = String.valueOf(dataBase.getNoStudentNotGraded(newValue));
                        Platform.runLater(() -> {
                            avgStudentGradeField.setText(avgStudentGrade);
                            noStudentGradedField.setText(noStudentGraded);
                            noStudentNotGradedField.setText(noStudentNotGraded);
                        });
                    } catch (SQLException error) {
                        Platform.runLater(() -> showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR));
                    }
                });
                thread.start();
            }
        });
        currentTab = tabPane.getSelectionModel().getSelectedItem();
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                currentTab = newValue;
        });
        courseChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                selectedCourseChoice = newValue;
        });
        cycleChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedCycle = newValue;
                if (newValue.equals("Svi ciklusi")) {
                    semeseterChoiceBox.getItems().clear();
                    semeseterChoiceBox.getItems().add("Svi semestri");
                    semeseterChoiceBox.getSelectionModel().select(0);
                    selectedSemester = semeseterChoiceBox.getValue();
                    return;
                }
                try {
                    semeseterChoiceBox.setItems(FXCollections.observableArrayList(dataBase.getSemestersOnCycle(Integer.valueOf(newValue))));
                    semeseterChoiceBox.getItems().add(0, "Svi semestri");
                    semeseterChoiceBox.getSelectionModel().select(0);
                    selectedSemester = semeseterChoiceBox.getValue();
                } catch (SQLException error) {
                    showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
        semeseterChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                selectedSemester = newValue;
        });
        courseTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedCourse = newValue;
                Thread thread = new Thread(() -> {
                    try {
                        String avgCourseGrade = String.valueOf(dataBase.getAvgCourseGrade(newValue));
                        String noStudentOnCourse = String.valueOf(dataBase.getNoStudentsOnCourse(newValue));
                        Platform.runLater(() -> {
                            avgCourseGradeField.setText(avgCourseGrade);
                            noStudentOnCourseField.setText(noStudentOnCourse);
                        });
                    } catch (SQLException error) {
                        Platform.runLater(() -> showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR));
                    }
                });
                thread.start();
            }
        });
        curriculumTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedCurriculum = newValue;
            }
        });
        adminTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                selectedAdmin = newValue;
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
            fillAdmins();
            ArrayList<String> cycles = dataBase.cycles();
            cycles.add(0, "Svi ciklusi");
            cycleChoiceBox.setItems(FXCollections.observableArrayList(cycles));
            cycleChoiceBox.getSelectionModel().select(0);
            ArrayList<String> courses = dataBase.coursesNames();
            courses.add(0, "Svi smjerovi");
            courseChoiceBox.setItems(FXCollections.observableArrayList(courses));
            courseChoiceBox.getSelectionModel().select(0);
            semeseterChoiceBox.getItems().add("Svi semestri");
            semeseterChoiceBox.getSelectionModel().select(0);
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
        }
        setListenersOnSelectedItems();
        Image refreshImage = new Image("img/refresh.png", 20, 20, true, true);
        refreshButton.setGraphic(new ImageView(refreshImage));
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

    private void clearSelectedCourse() {
        courseTable.getSelectionModel().clearSelection();
        selectedCourse = null;
    }

    private void clearSelectedCurriculum() {
        curriculumTable.getSelectionModel().clearSelection();
        selectedCurriculum = null;
    }

    private void clearSelectedAdmin() {
        adminTable.getSelectionModel().clearSelection();
        selectedAdmin = null;
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
            clearSelectedStudent();
            return;
        }
        showAlert("Uspjeh", "Uspješno izbrisan student", Alert.AlertType.INFORMATION);
        clearSelectedStudent();
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
            case "Smjerovi":
                updateCourse(null);
                break;
            case "Programi":
                updateCurriculum(null);
                break;
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
            case "Smjerovi":
                deleteCourse(null);
                break;
            case "Programi":
                deleteCurriculum(null);
                break;
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

    public void refreshStudent(ActionEvent actionEvent) {
        try {
            studentTable.setItems(FXCollections.observableArrayList(dataBase.getStudents(selectedCourseChoice, selectedCycle, selectedSemester)));
        } catch (SQLException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void addCourse(ActionEvent actionEvent) {
        clearSelectedCourse();
        editCourse(null);
    }

    public void updateCourse(ActionEvent actionEvent) {
        if (selectedCourse == null) {
            showAlert("Greška", "Prvo odaberite smjer", Alert.AlertType.ERROR);
            return;
        }
        editCourse(selectedCourse);
        clearSelectedCourse();
    }

    private void editCourse(Course course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addCourse.fxml"));
            loader.setController(new AddCourseController(selectedCourse));
            Parent root = loader.load();
            Stage secondaryStage = new Stage();
            if (course == null)
                secondaryStage.setTitle("Dodaj smjer");
            else
                secondaryStage.setTitle("Ažuriraj smjer");
            secondaryStage.getIcons().add(new Image("/img/course.png"));
            secondaryStage.setResizable(false);
            secondaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.showAndWait();
            courseTable.setItems(FXCollections.observableArrayList(dataBase.courses()));
        } catch (IOException | SQLException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }


    public void deleteCourse(ActionEvent actionEvent) {
        if (selectedCourse == null) {
            showAlert("Greška", "Prvo odaberite smjer", Alert.AlertType.ERROR);
            return;
        }
        try {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Potvrda");
            confirmationAlert.setHeaderText("Da li ste sigurni da želite izbrisati smjer '" + selectedCourse + "'?");
            confirmationAlert.showAndWait();
            if (confirmationAlert.getResult() != ButtonType.OK)
                return;
            dataBase.deleteCourse(selectedCourse);
            courseTable.setItems(FXCollections.observableArrayList(dataBase.courses()));
        } catch (SQLException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
            clearSelectedCourse();
            return;
        }
        showAlert("Uspjeh", "Uspješno izbrisan smjer", Alert.AlertType.INFORMATION);
        clearSelectedCourse();
    }


    public void addCurriculum(ActionEvent actionEvent) {
        clearSelectedCurriculum();
        editCurriculum(null);
    }


    public void updateCurriculum(ActionEvent actionEvent) {
        if (selectedCurriculum == null) {
            showAlert("Greška", "Prvo odaberite stavku", Alert.AlertType.ERROR);
            return;
        }
        editCurriculum(selectedCurriculum);
        clearSelectedCurriculum();
    }

    private void editCurriculum(Curriculum curriculum) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addCurriculum.fxml"));
            loader.setController(new AddCurriculumController(selectedCurriculum));
            Parent root = loader.load();
            Stage secondaryStage = new Stage();
            if (curriculum == null)
                secondaryStage.setTitle("Dodaj stavku");
            else
                secondaryStage.setTitle("Ažuriraj stavku");
            secondaryStage.getIcons().add(new Image("/img/curriculum.png"));
            secondaryStage.setResizable(false);
            secondaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.showAndWait();
            curriculumTable.setItems(FXCollections.observableArrayList(dataBase.curriculums()));
        } catch (IOException | SQLException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void deleteCurriculum(ActionEvent actionEvent) {
        if (selectedCurriculum == null) {
            showAlert("Greška", "Prvo odaberite stavku", Alert.AlertType.ERROR);
            return;
        }
        try {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Potvrda");
            confirmationAlert.setHeaderText("Da li ste sigurni da želite izbrisati stavku (ID: " + selectedCurriculum.getId() + ") ?");
            confirmationAlert.showAndWait();
            if (confirmationAlert.getResult() != ButtonType.OK)
                return;
            dataBase.deleteCurriculum(selectedCurriculum);
            curriculumTable.setItems(FXCollections.observableArrayList(dataBase.curriculums()));
        } catch (SQLException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
            clearSelectedCurriculum();
            return;
        }
        showAlert("Uspjeh", "Uspješno izbrisana stavka", Alert.AlertType.INFORMATION);
        clearSelectedCurriculum();
    }

    private void editAdmin(Administrator admin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addPerson.fxml"));
            loader.setController(new AddPersonController(selectedAdmin, "Administrator"));
            Parent root = loader.load();
            Stage secondaryStage = new Stage();
            if (admin == null)
                secondaryStage.setTitle("Dodaj administratora");
            else
                secondaryStage.setTitle("Ažuriraj administratora");
            secondaryStage.getIcons().add(new Image("/img/administrator.png"));
            secondaryStage.setResizable(false);
            secondaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.showAndWait();
            adminTable.setItems(FXCollections.observableArrayList(dataBase.admins()));
        } catch (IOException | SQLException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }


    public void addAdmin(ActionEvent actionEvent) {
        clearSelectedAdmin();
        editAdmin(selectedAdmin);
    }

    public void updateAdmin(ActionEvent actionEvent) {
        if (selectedAdmin == null) {
            showAlert("Greška", "Prvo odaberite administratora", Alert.AlertType.ERROR);
            return;
        }
        editAdmin(selectedAdmin);
        clearSelectedAdmin();
    }

    public void deleteAdmin(ActionEvent actionEvent) {
        if (selectedAdmin == null) {
            showAlert("Greška", "Prvo odaberite administratora", Alert.AlertType.ERROR);
            return;
        }
        try {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Potvrda");
            confirmationAlert.setHeaderText("Da li ste sigurni da želite izbrisati administratora '" + selectedAdmin + "'?");
            confirmationAlert.showAndWait();
            if (confirmationAlert.getResult() != ButtonType.OK)
                return;
            dataBase.deleteAdmin(selectedAdmin);
            adminTable.setItems(FXCollections.observableArrayList(dataBase.admins()));
        } catch (SQLException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
            clearSelectedAdmin();
            return;
        }
        showAlert("Uspjeh", "Uspješno izbrisan administrator", Alert.AlertType.INFORMATION);
        clearSelectedAdmin();
    }
}

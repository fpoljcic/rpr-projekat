package ba.unsa.etf.rpr.projekat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.io.*;
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

    public TableColumn<Subject, String> subjectNameCol;
    public TableColumn<Subject, String> subjectCodeCol;
    public TableColumn<Subject, Integer> subjectEctsCol;
    public TableColumn<Subject, Professor> subjectProfessorCol;
    public TableColumn<Subject, Subject> subjectReqSubjectCol;

    public TableColumn<Professor, String> professorFirstNameCol;
    public TableColumn<Professor, String> professorLastNameCol;
    public TableColumn<Professor, String> professorJmbgCol;
    public TableColumn<Professor, String> professorAdressCol;
    public TableColumn<Professor, String> professorEmailCol;
    public TableColumn<Professor, String> professorTitleCol;

    public TableColumn<Student, String> studentFirstNameCol;
    public TableColumn<Student, String> studentLastNameCol;
    public TableColumn<Student, String> studentJmbgCol;
    public TableColumn<Student, String> studentAdressCol;
    public TableColumn<Student, String> studentEmailCol;
    public TableColumn<Student, LocalDate> studentBirthDateCol;
    public TableColumn<Student, Semester> studentSemesterCol;
    public TableColumn<Student, Course> studentCourseCol;
    public TableColumn<Student, LocalDate> studentPauseDateCol;

    public TableColumn<Course, String> courseNameCol;

    public TableColumn<Curriculum, Integer> curriculumIdCol;
    public TableColumn<Curriculum, Course> curriculumCourseCol;
    public TableColumn<Curriculum, Semester> curriculumSemesterCol;
    public TableColumn<Curriculum, Subject> curriculumSubjectCol;
    public TableColumn<Curriculum, String> curriculumRequiredSubjectCol;

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
    public CheckMenuItem subjectsMenuItem;
    public CheckMenuItem professorsMenuItem;
    public CheckMenuItem studentsMenuItem;
    public CheckMenuItem coursesMenuItem;
    public CheckMenuItem curriculumMenuItem;
    public CheckMenuItem adminsMenuItem;
    public Tab subjectsTab;
    public Tab professorsTab;
    public Tab studentsTab;
    public Tab coursesTab;
    public Tab curriculumTab;
    public Tab adminsTab;
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
    private ObservableList<Subject> subjects;
    private ObservableList<Professor> professors;
    private ObservableList<Course> courses;
    private ObservableList<Semester> semesters;

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

    private void setRequiredData() throws SQLException {
        subjects = FXCollections.observableArrayList(dataBase.subjects());
        professors = FXCollections.observableArrayList(dataBase.professors());
        courses = FXCollections.observableArrayList(dataBase.courses());
        semesters = FXCollections.observableArrayList(dataBase.semesters());
    }

    private void fillSubjects() {
        subjectTable.setItems(subjects);
        subjectNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        subjectCodeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        subjectEctsCol.setCellValueFactory(new PropertyValueFactory<>("ects"));
        subjectProfessorCol.setCellValueFactory(new PropertyValueFactory<>("professor"));
        subjectReqSubjectCol.setCellValueFactory(new PropertyValueFactory<>("reqSubject"));
    }

    private void fillAdmins() throws SQLException {
        adminTable.setItems(FXCollections.observableArrayList(dataBase.admins()));
        adminFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        adminLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        adminJmbgCol.setCellValueFactory(new PropertyValueFactory<>("jmbg"));
        adminAdressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        adminEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void fillProfessors() {
        professorTable.setItems(professors);
        professorFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        professorLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        professorJmbgCol.setCellValueFactory(new PropertyValueFactory<>("jmbg"));
        professorAdressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        professorEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        professorTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
    }

    private void fillStudents() throws SQLException {
        studentTable.setItems(FXCollections.observableArrayList(dataBase.students()));
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

    private void fillCourses() {
        courseTable.setItems(courses);
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
                        Platform.runLater(() -> showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR));
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
                        Platform.runLater(() -> showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR));
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
                        Platform.runLater(() -> showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR));
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
                        Platform.runLater(() -> showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR));
                    }
                });
                thread.start();
            }
        });
        curriculumTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                selectedCurriculum = newValue;
        });
        adminTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                selectedAdmin = newValue;
        });
    }

    private void setViewListeners() {
        subjectsMenuItem.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setTabDisable(subjectsTab, newValue);
        });
        professorsMenuItem.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setTabDisable(professorsTab, newValue);
        });
        studentsMenuItem.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setTabDisable(studentsTab, newValue);
        });
        coursesMenuItem.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setTabDisable(coursesTab, newValue);
        });
        curriculumMenuItem.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setTabDisable(curriculumTab, newValue);
        });
        adminsMenuItem.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setTabDisable(adminsTab, newValue);
        });
    }

    private void setTabDisable(Tab tab, boolean disabled) {
        tab.setDisable(!disabled);
        if (disabled) {
            try {
                if (tab == subjectsTab)
                    fillSubjects();
                else if (tab == professorsTab)
                    fillProfessors();
                else if (tab == studentsTab)
                    fillStudents();
                else if (tab == coursesTab)
                    fillCourses();
                else if (tab == curriculumTab)
                    fillCurriculums();
                else if (tab == adminsTab)
                    fillAdmins();
            } catch (SQLException error) {
                showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public ArrayList<Boolean> getTabsConfig() {
        ArrayList<Boolean> tabsConfig = new ArrayList<>();
        tabsConfig.add(subjectsMenuItem.isSelected());
        tabsConfig.add(professorsMenuItem.isSelected());
        tabsConfig.add(studentsMenuItem.isSelected());
        tabsConfig.add(coursesMenuItem.isSelected());
        tabsConfig.add(curriculumMenuItem.isSelected());
        tabsConfig.add(adminsMenuItem.isSelected());
        return tabsConfig;
    }

    @FXML
    public void initialize() {
        try {
            setViewListeners();
            setRequiredData();
            DataInputStream input = new DataInputStream(new FileInputStream("resources/config.dat"));
            input.readBoolean(); // First value is for another form
            boolean[] tabsConfig = new boolean[6];
            for (int i = 0; i < tabsConfig.length; i++)
                tabsConfig[i] = input.readBoolean();
            input.close();
            subjectsMenuItem.setSelected(tabsConfig[0]);
            professorsMenuItem.setSelected(tabsConfig[1]);
            studentsMenuItem.setSelected(tabsConfig[2]);
            coursesMenuItem.setSelected(tabsConfig[3]);
            curriculumMenuItem.setSelected(tabsConfig[4]);
            adminsMenuItem.setSelected(tabsConfig[5]);
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
            logOutClick(null);
            return;
        } catch (IOException error) {
            showAlert("Greška", "Problem sa čitanjem config datoteke: " + error.getMessage(), Alert.AlertType.ERROR);
            logOutClick(null);
            return;
        }
        setListenersOnSelectedItems();
        Image refreshImage = new Image("img/refresh.png", 20, 20, true, true);
        refreshButton.setGraphic(new ImageView(refreshImage));
    }


    private void refreshSubjectTable() {
        Thread thread = new Thread(() -> {
            try {
                subjects = FXCollections.observableArrayList(dataBase.subjects());
                Platform.runLater(() -> subjectTable.setItems(FXCollections.observableArrayList(subjects)));
                Platform.runLater(() -> subjectTable.refresh());
            } catch (SQLException error) {
                Platform.runLater(() -> showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR));
            }
        });
        thread.start();
    }

    private void refreshProfessorTable() {
        Thread thread = new Thread(() -> {
            try {
                professors = FXCollections.observableArrayList(dataBase.professors());
                Platform.runLater(() -> professorTable.setItems(FXCollections.observableArrayList(professors)));
                Platform.runLater(() -> professorTable.refresh());
            } catch (SQLException error) {
                Platform.runLater(() -> showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR));
            }
        });
        thread.start();
    }

    private void refreshStudentTable() {
        Thread thread = new Thread(() -> {
            try {
                ArrayList<Student> students = dataBase.students();
                Platform.runLater(() -> studentTable.setItems(FXCollections.observableArrayList(students)));
                Platform.runLater(() -> studentTable.refresh());
            } catch (SQLException error) {
                Platform.runLater(() -> showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR));
            }
        });
        thread.start();
    }

    private void refreshCourseTable() {
        Thread thread = new Thread(() -> {
            try {
                courses = FXCollections.observableArrayList(dataBase.courses());
                Platform.runLater(() -> courseTable.setItems(FXCollections.observableArrayList(courses)));
                Platform.runLater(() -> courseTable.refresh());
            } catch (SQLException error) {
                Platform.runLater(() -> showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR));
            }
        });
        thread.start();
    }

    private void refreshCurriculumTable() {
        Thread thread = new Thread(() -> {
            try {
                ArrayList<Curriculum> curriculums = dataBase.curriculums();
                Platform.runLater(() -> curriculumTable.setItems(FXCollections.observableArrayList(curriculums)));
                Platform.runLater(() -> curriculumTable.refresh());
            } catch (SQLException error) {
                Platform.runLater(() -> showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR));
            }
        });
        thread.start();
    }

    private void refreshAdminTable() {
        Thread thread = new Thread(() -> {
            try {
                ArrayList<Administrator> admins = dataBase.admins();
                Platform.runLater(() -> adminTable.setItems(FXCollections.observableArrayList(admins)));
                Platform.runLater(() -> adminTable.refresh());
            } catch (SQLException error) {
                Platform.runLater(() -> showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR));
            }
        });
        thread.start();
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

    private boolean editSubject(Subject subject) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addSubject.fxml"));
            AddSubjectController controller = new AddSubjectController(subject, professors, subjects);
            loader.setController(controller);
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
            return controller.isOkClicked();
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
    }

    public void addSubject(ActionEvent actionEvent) {
        boolean okClicked = editSubject(null);
        if (okClicked) {
            clearSelectedSubject();
            refreshSubjectTable();
        }
    }

    public void updateSubject(ActionEvent actionEvent) {
        if (selectedSubject == null) {
            showAlert("Greška", "Prvo odaberite predmet", Alert.AlertType.ERROR);
            return;
        }
        boolean okClicked = editSubject(selectedSubject);
        if (okClicked) {
            clearSelectedSubject();
            refreshSubjectTable();
        }
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
            refreshSubjectTable();
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
            clearSelectedSubject();
            return;
        }
        showAlert("Uspjeh", "Uspješno izbrisan predmet", Alert.AlertType.INFORMATION);
        clearSelectedSubject();
    }

    private boolean editProfessor(Professor professor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addPerson.fxml"));
            AddPersonController controller = new AddPersonController(professor, "Profesor", semesters, courses);
            loader.setController(controller);
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
            return controller.isOkClicked();
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
    }

    public void addProfessor(ActionEvent actionEvent) {
        boolean okClicked = editProfessor(null);
        if (okClicked) {
            clearSelectedProfessor();
            refreshProfessorTable();
        }
    }

    public void updateProfessor(ActionEvent actionEvent) {
        if (selectedProfessor == null) {
            showAlert("Greška", "Prvo odaberite profesora", Alert.AlertType.ERROR);
            return;
        }
        boolean okClicked = editProfessor(selectedProfessor);
        if (okClicked) {
            clearSelectedProfessor();
            refreshProfessorTable();
        }
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
            refreshProfessorTable();
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
            clearSelectedProfessor();
            return;
        }
        showAlert("Uspjeh", "Uspješno izbrisan profesor", Alert.AlertType.INFORMATION);
        clearSelectedProfessor();
    }

    private boolean editStudent(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addPerson.fxml"));
            AddPersonController controller = new AddPersonController(student, "Student", semesters, courses);
            loader.setController(controller);
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
            return controller.isOkClicked();
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
    }

    public void addStudent(ActionEvent actionEvent) {
        boolean okClicked = editStudent(null);
        if (okClicked) {
            clearSelectedStudent();
            refreshStudentTable();
        }
    }

    public void updateStudent(ActionEvent actionEvent) {
        if (selectedStudent == null) {
            showAlert("Greška", "Prvo odaberite studenta", Alert.AlertType.ERROR);
            return;
        }
        boolean okClicked = editStudent(selectedStudent);
        if (okClicked) {
            clearSelectedStudent();
            refreshStudentTable();
        }
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
            refreshStudentTable();
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
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
        if (currentTab.isDisabled())
            return;
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
            case "Plan i program":
                addCurriculum(null);
                break;
            case "Administratori":
                addAdmin(null);
                break;
        }
    }

    public void updateClick(ActionEvent actionEvent) {
        if (currentTab.isDisabled())
            return;
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
            case "Plan i program":
                updateCurriculum(null);
                break;
            case "Administratori":
                updateAdmin(null);
                break;
        }
    }

    public void deleteClick(ActionEvent actionEvent) {
        if (currentTab.isDisabled())
            return;
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
            case "Plan i program":
                deleteCurriculum(null);
                break;
            case "Administratori":
                deleteAdmin(null);
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
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void addCourse(ActionEvent actionEvent) {
        boolean okClicked = editCourse(null);
        if (okClicked) {
            clearSelectedCourse();
            refreshCourseTable();
        }
    }

    public void updateCourse(ActionEvent actionEvent) {
        if (selectedCourse == null) {
            showAlert("Greška", "Prvo odaberite smjer", Alert.AlertType.ERROR);
            return;
        }
        boolean okClicked = editCourse(selectedCourse);
        if (okClicked) {
            clearSelectedCourse();
            refreshCourseTable();
        }
    }

    private boolean editCourse(Course course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addCourse.fxml"));
            AddCourseController controller = new AddCourseController(course);
            loader.setController(controller);
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
            return controller.isOkClicked();
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
            return false;
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
            refreshCourseTable();
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
            clearSelectedCourse();
            return;
        }
        showAlert("Uspjeh", "Uspješno izbrisan smjer", Alert.AlertType.INFORMATION);
        clearSelectedCourse();
    }

    public void addCurriculum(ActionEvent actionEvent) {
        boolean okClicked = editCurriculum(null);
        if (okClicked) {
            clearSelectedCurriculum();
            refreshCurriculumTable();
        }
    }

    public void updateCurriculum(ActionEvent actionEvent) {
        if (selectedCurriculum == null) {
            showAlert("Greška", "Prvo odaberite stavku", Alert.AlertType.ERROR);
            return;
        }
        boolean okClicked = editCurriculum(selectedCurriculum);
        if (okClicked) {
            clearSelectedCurriculum();
            refreshCurriculumTable();
        }
    }

    private boolean editCurriculum(Curriculum curriculum) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addCurriculum.fxml"));
            AddCurriculumController controller = new AddCurriculumController(curriculum, subjects, semesters, courses);
            loader.setController(controller);
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
            return controller.isOkClicked();
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
            return false;
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
            refreshCurriculumTable();
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
            clearSelectedCurriculum();
            return;
        }
        showAlert("Uspjeh", "Uspješno izbrisana stavka", Alert.AlertType.INFORMATION);
        clearSelectedCurriculum();
    }

    private boolean editAdmin(Administrator admin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addPerson.fxml"));
            AddPersonController controller = new AddPersonController(admin, "Administrator", semesters, courses);
            loader.setController(controller);
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
            return controller.isOkClicked();
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
    }

    public void addAdmin(ActionEvent actionEvent) {
        boolean okClicked = editAdmin(null);
        if (okClicked) {
            clearSelectedAdmin();
            refreshAdminTable();
        }
    }

    public void updateAdmin(ActionEvent actionEvent) {
        if (selectedAdmin == null) {
            showAlert("Greška", "Prvo odaberite administratora", Alert.AlertType.ERROR);
            return;
        }
        boolean okClicked = editAdmin(selectedAdmin);
        if (okClicked) {
            clearSelectedAdmin();
            refreshAdminTable();
        }
    }

    public void deleteAdmin(ActionEvent actionEvent) {
        if (selectedAdmin == null) {
            showAlert("Greška", "Prvo odaberite administratora", Alert.AlertType.ERROR);
            return;
        }
        if (selectedAdmin.getLogin().getId() == login.getId()) {
            showAlert("Greška", "Ne možete izbrisati samog sebe", Alert.AlertType.ERROR);
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
            refreshAdminTable();
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
            clearSelectedAdmin();
            return;
        }
        showAlert("Uspjeh", "Uspješno izbrisan administrator", Alert.AlertType.INFORMATION);
        clearSelectedAdmin();
    }

    public void nextYearClick(ActionEvent actionEvent) {
        try {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Potvrda");
            confirmationAlert.setHeaderText("Da li ste sigurni da želite preći na sljedeću godinu?");
            confirmationAlert.showAndWait();
            if (confirmationAlert.getResult() != ButtonType.OK)
                return;
            dataBase.advanceYear();
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        showAlert("Uspjeh", "Uspješan prelazak na sljedeću godinu", Alert.AlertType.INFORMATION);
    }
}

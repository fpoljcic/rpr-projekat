package ba.unsa.etf.rpr.projekat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class ProfessorController {
    public TableView<SubjectWrapper> subjectTable;
    public TableColumn<SubjectWrapper, String> subjectNameCol;
    public TableColumn<SubjectWrapper, String> subjectCodeCol;
    public TableColumn<SubjectWrapper, Integer> subjectEctsCol;
    public TableColumn<SubjectWrapper, SubjectWrapper> subjectReqSubjectCol;
    public TableColumn<SubjectWrapper, Integer> subjectNoStudentsCol;
    public TableColumn<SubjectWrapper, Integer> subjectNoStudentsGradedCol;
    public TableColumn<SubjectWrapper, Integer> subjectNoStudentsNotGradedCol;
    public TableColumn<SubjectWrapper, Float> subjectAvgGradeCol;
    public TableColumn<SubjectWrapper, String> subjectPercentPassedCol;

    public TableView<StudentGradeSubjectWrapper> studentsOnSubjectTable;
    public TableColumn<StudentGradeSubjectWrapper, String> studentFirstNameCol;
    public TableColumn<StudentGradeSubjectWrapper, String> studentLastNameCol;
    public TableColumn<StudentGradeSubjectWrapper, String> studentJmbgCol;
    public TableColumn<StudentGradeSubjectWrapper, String> studentAdressCol;
    public TableColumn<StudentGradeSubjectWrapper, String> studentEmailCol;
    public TableColumn<StudentGradeSubjectWrapper, LocalDate> studentBirthDateCol;
    public TableColumn<StudentGradeSubjectWrapper, Semester> studentSemesterCol;
    public TableColumn<StudentGradeSubjectWrapper, Course> studentCourseCol;
    public TableColumn<StudentGradeSubjectWrapper, LocalDate> studentPauseDateCol;
    public TableColumn<StudentGradeSubjectWrapper, Float> studentPointsCol;
    public TableColumn<StudentGradeSubjectWrapper, String> studentSubjectCol;
    public Label loginLabel;
    public Label titleLabel;
    public Label jmbgLabel;
    public Label addressLabel;
    public Label emailLabel;
    private BazaDAO dataBase;
    private Professor professor;
    private Student selectedStudent;
    private Grade selectedGrade;
    private Login login;

    public ProfessorController(Login login) {
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
        subjectTable.setItems(FXCollections.observableArrayList(dataBase.subjects(professor)));
        subjectNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        subjectCodeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        subjectEctsCol.setCellValueFactory(new PropertyValueFactory<>("ects"));
        subjectReqSubjectCol.setCellValueFactory(new PropertyValueFactory<>("reqSubject"));
        subjectNoStudentsGradedCol.setCellValueFactory(new PropertyValueFactory<>("noStudentsGraded"));
        subjectNoStudentsNotGradedCol.setCellValueFactory(new PropertyValueFactory<>("noStudentsNotGraded"));
        subjectNoStudentsCol.setCellValueFactory(new PropertyValueFactory<>("noStudents"));
        subjectAvgGradeCol.setCellValueFactory(new PropertyValueFactory<>("avgGrade"));
        subjectPercentPassedCol.setCellValueFactory(new PropertyValueFactory<>("percentPassed"));
    }

    private void fillStudentsOnSubject() throws SQLException {
        studentsOnSubjectTable.setItems(FXCollections.observableArrayList(dataBase.students(professor)));
        studentFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        studentLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        studentJmbgCol.setCellValueFactory(new PropertyValueFactory<>("jmbg"));
        studentAdressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        studentEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        studentBirthDateCol.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        studentSemesterCol.setCellValueFactory(new PropertyValueFactory<>("semester"));
        studentCourseCol.setCellValueFactory(new PropertyValueFactory<>("course"));
        studentPauseDateCol.setCellValueFactory(new PropertyValueFactory<>("pauseDate"));
        studentPointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));
        studentSubjectCol.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
    }

    private void addListeners() {
        studentsOnSubjectTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedStudent = newValue.getStudent();
                selectedGrade = newValue.getGrade();
            }
        });
    }

    @FXML
    public void initialize() {
        professor = dataBase.getProfessor(login);
        try {
            fillSubjects();
            fillStudentsOnSubject();
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
        }
        addListeners();
        loginLabel.setText(login.getUsername() + " (" + professor.getFirstName() + " " + professor.getLastName() + ")");
        jmbgLabel.setText(professor.getJmbg());
        addressLabel.setText(professor.getAddress());
        emailLabel.setText(professor.getEmail());
        titleLabel.setText(professor.getTitle());
    }


    public void updateInfoClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addPerson.fxml"));
            loader.setController(new AddPersonController(professor, "Profesor"));
            Parent root = loader.load();
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Ažuriraj profesora");
            secondaryStage.getIcons().add(new Image("/img/professor.png"));
            secondaryStage.setResizable(false);
            secondaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.showAndWait();
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
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
            Stage currentStage = (Stage) loginLabel.getScene().getWindow();
            currentStage.close();
            loginStage.show();
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void closeClick(ActionEvent actionEvent) {
        BazaDAO.removeInstance();
        Platform.exit();
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

    private void clearSelectedStudent() {
        studentsOnSubjectTable.getSelectionModel().clearSelection();
        selectedStudent = null;
    }

    private void refreshStudentTable() {
        Thread thread = new Thread(() -> {
            try {
                ArrayList<StudentGradeSubjectWrapper> students = dataBase.students(professor);
                Platform.runLater(() -> studentsOnSubjectTable.setItems(FXCollections.observableArrayList(students)));
                Platform.runLater(() -> studentsOnSubjectTable.refresh());
            } catch (SQLException error) {
                Platform.runLater(() -> showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR));
            }
        });
        thread.start();
    }

    public void updateStudentClick(ActionEvent actionEvent) {
        if (selectedStudent == null) {
            showAlert("Greška", "Prvo odaberite studenta", Alert.AlertType.ERROR);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gradeStudent.fxml"));
            GradeStudentController controller = new GradeStudentController(selectedStudent, selectedGrade, professor);
            loader.setController(controller);
            Parent root = loader.load();
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Ažuriraj studenta");
            secondaryStage.getIcons().add(new Image("/img/student.png"));
            secondaryStage.setResizable(false);
            secondaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.showAndWait();
            if (controller.isOkClicked()) {
                clearSelectedStudent();
                refreshStudentTable();
            }
        } catch (IOException error) {
            error.printStackTrace();
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }
}

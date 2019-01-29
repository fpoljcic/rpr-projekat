package ba.unsa.etf.rpr.projekat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class StudentController {

    public TableView<SubjectGradeWrapper> subjectTable;
    public TableColumn<SubjectGradeWrapper, String> subjectNameCol;
    public TableColumn<SubjectGradeWrapper, String> subjectCodeCol;
    public TableColumn<SubjectGradeWrapper, Integer> subjectEctsCol;
    public TableColumn<SubjectGradeWrapper, Professor> subjectProfessorCol;
    public TableColumn<SubjectGradeWrapper, Float> subjectPointsCol;

    public TableView<SubjectGradeWrapper> subjectArchiveTable;
    public TableColumn<SubjectGradeWrapper, String> subjectArchiveNameCol;
    public TableColumn<SubjectGradeWrapper, String> subjectArchiveCodeCol;
    public TableColumn<SubjectGradeWrapper, Integer> subjectArchiveEctsCol;
    public TableColumn<SubjectGradeWrapper, Professor> subjectArchiveProfessorCol;
    public TableColumn<SubjectGradeWrapper, Float> subjectArchivePointsCol;
    public TableColumn<SubjectGradeWrapper, LocalDate> subjectArchiveGradeDateCol;
    public TableColumn<SubjectGradeWrapper, Integer> subjectArchiveScoreCol;

    public Label loginLabel;
    public Label semesterLabel;
    public Label courseLabel;
    public Label birthDateLabel;
    public Label jmbgLabel;
    public Label addressLabel;
    public Label emailLabel;
    public Login login;
    private BazaDAO dataBase;
    private Student student;

    public StudentController(Login login) {
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
        ObservableList<SubjectGradeWrapper> testinggg = FXCollections.observableArrayList(dataBase.subjects(student));
        subjectTable.setItems(testinggg);
        subjectNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        subjectCodeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        subjectEctsCol.setCellValueFactory(new PropertyValueFactory<>("ects"));
        subjectProfessorCol.setCellValueFactory(new PropertyValueFactory<>("professor"));
        subjectPointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));
    }

    private void fillArchiveSubjects() throws SQLException {
        subjectArchiveTable.setItems(FXCollections.observableArrayList(dataBase.subjectsPassed(student)));
        subjectArchiveNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        subjectArchiveCodeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        subjectArchiveEctsCol.setCellValueFactory(new PropertyValueFactory<>("ects"));
        subjectArchiveProfessorCol.setCellValueFactory(new PropertyValueFactory<>("professorGrade"));
        subjectArchivePointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));
        subjectArchiveGradeDateCol.setCellValueFactory(new PropertyValueFactory<>("gradeDate"));
        subjectArchiveScoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
    }

    @FXML
    public void initialize() {
        student = dataBase.getStudent(login);
        try {
            fillSubjects();
            fillArchiveSubjects();
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
        }
        loginLabel.setText(login.getUsername() + " (" + student.getFirstName() + " " + student.getLastName() + ")");
        semesterLabel.setText(student.getSemester().toString());
        courseLabel.setText(student.getCourse().toString());
        birthDateLabel.setText(student.getBirthDate().toString());
        jmbgLabel.setText(student.getJmbg());
        addressLabel.setText(student.getAddress());
        emailLabel.setText(student.getEmail());
    }

    public void updateInfoClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addPerson.fxml"));
            loader.setController(new AddPersonController(student, "Student"));
            Parent root = loader.load();
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Ažuriraj studenta");
            secondaryStage.getIcons().add(new Image("/img/student.png"));
            secondaryStage.setResizable(false);
            secondaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.showAndWait();
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void pauseClick(ActionEvent actionEvent) {

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

    public void closeClick(ActionEvent actionEvent) {
        BazaDAO.removeInstance();
        Platform.exit();
    }
}

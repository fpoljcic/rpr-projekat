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

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
    public Label statusLabel;
    public Button updateButton;
    public Button pauseButton;
    public MenuItem updateMenuItem;
    public MenuItem pauseMenuItem;
    public CheckMenuItem archiveSubjectsMenuItem;
    public Tab archiveSubjectsTab;
    public Login login;
    private BazaDAO dataBase;
    private Student student;
    private LocalDate pauseDate;

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
        subjectTable.setItems(FXCollections.observableArrayList(dataBase.subjects(student)));
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

    private void addListeners() {
        statusLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.equals("Aktivan")) {
                    statusLabel.getStyleClass().removeAll("redText", "blueText");
                    statusLabel.getStyleClass().add("greenText");
                } else if (newValue.equals("Neaktivan")) {
                    statusLabel.getStyleClass().removeAll("greenText", "blueText");
                    statusLabel.getStyleClass().add("redText");
                } else {
                    statusLabel.getStyleClass().removeAll("redText", "greenText");
                    statusLabel.getStyleClass().add("blueText");
                }
            }
        });
    }

    private void setViewListener() {
        archiveSubjectsMenuItem.selectedProperty().addListener((observable, oldValue, newValue) -> {
            archiveSubjectsTab.setDisable(!newValue);
            if (newValue) {
                try {
                    fillArchiveSubjects();
                } catch (SQLException error) {
                    showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    @FXML
    public void initialize() {
        student = dataBase.getStudent(login);
        pauseDate = student.getPauseDate();
        setViewListener();
        try {
            DataInputStream input = new DataInputStream(new FileInputStream("resources/config.dat"));
            boolean tabConfig = input.readBoolean();
            input.close();
            archiveSubjectsMenuItem.setSelected(tabConfig);
            fillSubjects();
            if (subjectTable.getItems().isEmpty()) {
                if (!archiveSubjectsMenuItem.isSelected())
                    fillArchiveSubjects();
                if (advanceStudent())
                    fillSubjects();
            }
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
            return;
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        loginLabel.setText(login.getUsername() + " (" + student.getFirstName() + " " + student.getLastName() + ")");
        semesterLabel.setText(student.getSemester().toString());
        courseLabel.setText(student.getCourse().toString());
        birthDateLabel.setText(student.getBirthDate().toString());
        jmbgLabel.setText(student.getJmbg());
        addressLabel.setText(student.getAddress());
        emailLabel.setText(student.getEmail());
        addListeners();
        if (pauseDate != null) {
            disableUI(true);
            if (ChronoUnit.YEARS.between(pauseDate, LocalDate.now()) >= 1) {
                pauseButton.setDisable(true);
                pauseMenuItem.setDisable(true);
                statusLabel.setText("Neaktivan");
            }
        } else
            statusLabel.setText("Aktivan");
    }

    private boolean advanceStudent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/advanceStudent.fxml"));
            AdvanceStudentController controller = new AdvanceStudentController(student, subjectArchiveTable.getItems());
            loader.setController(controller);
            Parent root = loader.load();
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Čestitamo");
            secondaryStage.getIcons().add(new Image("/img/advance.png"));
            secondaryStage.setResizable(false);
            secondaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
        return false;
    }

    private void disableUI(boolean isPaused) {
        if (isPaused) {
            pauseButton.setText("Odledi godinu");
            pauseMenuItem.setText("Continue year");
            statusLabel.setText("Zaleđen");
        } else {
            pauseButton.setText("Zaledi godinu");
            pauseMenuItem.setText("Pause year");
            statusLabel.setText("Aktivan");
        }
        subjectTable.setDisable(isPaused);
        subjectNameCol.setEditable(isPaused);
        subjectArchiveTable.setDisable(isPaused);
        updateButton.setDisable(isPaused);
        updateMenuItem.setDisable(isPaused);
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
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Potvrda");
        if (pauseDate == null)
            confirmationAlert.setHeaderText("Da li ste sigurni da želite zalediti godinu ?");
        else
            confirmationAlert.setHeaderText("Da li ste sigurni da želite odlediti godinu ?");
        confirmationAlert.showAndWait();
        if (confirmationAlert.getResult() != ButtonType.OK)
            return;
        if (pauseDate == null)
            student.setPauseDate(LocalDate.now());
        else
            student.setPauseDate(null);
        try {
            dataBase.updateStudent(student);
        } catch (SQLException error) {
            student.setPauseDate(pauseDate);
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        if (pauseDate == null)
            disableUI(true);
        else
            disableUI(false);
        pauseDate = student.getPauseDate();
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

    public boolean getTabConfig() {
        return archiveSubjectsMenuItem.isSelected();
    }
}

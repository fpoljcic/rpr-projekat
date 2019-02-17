package ba.unsa.etf.rpr.projekat;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddCurriculumController {
    public ChoiceBox<Course> courseChoiceBox;
    public ChoiceBox<Semester> semesterChoiceBox;
    public ChoiceBox<Subject> subjectChoiceBox;
    public CheckBox reqSubjectCheckBox;
    private String reqSubject;
    private Curriculum curriculum;
    private BazaDAO dataBase;
    private boolean okClicked;
    private ObservableList<Subject> subjects;
    private ObservableList<Semester> semesters;
    private ObservableList<Course> courses;

    public AddCurriculumController(Curriculum curriculum, ObservableList<Subject> subjects, ObservableList<Semester> semesters, ObservableList<Course> courses) {
        this.curriculum = curriculum;
        dataBase = BazaDAO.getInstance();
        reqSubject = "Ne";
        this.subjects = subjects;
        this.semesters = semesters;
        this.courses = courses;
    }

    @FXML
    public void initialize() {
        reqSubjectCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                reqSubject = "Da";
            else
                reqSubject = "Ne";
        });
        Thread thread = new Thread(() -> {
            Platform.runLater(() -> {
                courseChoiceBox.setItems(courses);
                if (curriculum != null)
                    courseChoiceBox.setValue(curriculum.getCourse());
                else
                    courseChoiceBox.getSelectionModel().select(0);
            });
            Platform.runLater(() -> {
                semesterChoiceBox.setItems(semesters);
                if (curriculum != null)
                    semesterChoiceBox.setValue(curriculum.getSemester());
                else
                    semesterChoiceBox.getSelectionModel().select(0);
            });
            Platform.runLater(() -> {
                subjectChoiceBox.setItems(subjects);
                if (curriculum != null)
                    subjectChoiceBox.setValue(curriculum.getSubject());
                else
                    subjectChoiceBox.getSelectionModel().select(0);
            });
        });
        thread.start();
        if (curriculum != null && curriculum.getRequiredSubject().equals("Da"))
            reqSubjectCheckBox.setSelected(true);
    }

    public void cancelClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) courseChoiceBox.getScene().getWindow();
        currentStage.close();
    }

    private void showAlert(String title, String headerText, Alert.AlertType type) {
        Alert error = new Alert(type);
        error.setTitle(title);
        error.setHeaderText(headerText);
        error.show();
    }

    public void okClick(ActionEvent actionEvent) {
        if (this.curriculum == null) {
            try {
                addCurriculum();
            } catch (SQLException error) {
                showAlert("Greška", "Problem pri dodavanju: " + error.getMessage(), Alert.AlertType.ERROR);
                return;
            }
        } else {
            try {
                updateCurriculum();
            } catch (SQLException error) {
                showAlert("Greška", "Problem pri ažuriranju: " + error.getMessage(), Alert.AlertType.ERROR);
                return;
            }
        }
        okClicked = true;
        Stage currentStage = (Stage) courseChoiceBox.getScene().getWindow();
        currentStage.close();
    }

    private void addCurriculum() throws SQLException {
        Curriculum curriculum = new Curriculum();
        curriculum.setCourse(courseChoiceBox.getValue());
        curriculum.setSemester(semesterChoiceBox.getValue());
        curriculum.setSubject(subjectChoiceBox.getValue());
        curriculum.setRequiredSubject(reqSubject);
        dataBase.addCurriculum(curriculum);
        showAlert("Uspjeh", "Uspješno dodata stavka", Alert.AlertType.INFORMATION);
    }

    private void updateCurriculum() throws SQLException {
        curriculum.setCourse(courseChoiceBox.getValue());
        curriculum.setSemester(semesterChoiceBox.getValue());
        curriculum.setSubject(subjectChoiceBox.getValue());
        curriculum.setRequiredSubject(reqSubject);
        dataBase.updateCurriculum(curriculum);
        showAlert("Uspjeh", "Uspješno ažurirana stavka", Alert.AlertType.INFORMATION);
    }

    public boolean isOkClicked() {
        return okClicked;
    }
}

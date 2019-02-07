package ba.unsa.etf.rpr.projekat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
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

    public AddCurriculumController(Curriculum curriculum) {
        this.curriculum = curriculum;
        dataBase = BazaDAO.getInstance();
        reqSubject = "Ne";
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
            try {
                var courses = FXCollections.observableArrayList(dataBase.courses());
                var semesters = FXCollections.observableArrayList(dataBase.semesters());
                var subjects = FXCollections.observableArrayList(dataBase.subjects());
                Platform.runLater(() -> courseChoiceBox.setItems(courses));
                Platform.runLater(() -> semesterChoiceBox.setItems(semesters));
                Platform.runLater(() -> subjectChoiceBox.setItems(subjects));
            } catch (SQLException error) {
                showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
            }
        });
        thread.start();
        if (curriculum != null) {
            courseChoiceBox.setValue(curriculum.getCourse());
            semesterChoiceBox.setValue(curriculum.getSemester());
            subjectChoiceBox.setValue(curriculum.getSubject());
            if (curriculum.getRequiredSubject().equals("Da"))
                reqSubjectCheckBox.setSelected(true);
        }
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

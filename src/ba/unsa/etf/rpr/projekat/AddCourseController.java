package ba.unsa.etf.rpr.projekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddCourseController {
    public TextField nameField;
    private Course course;
    private boolean validField;
    private BazaDAO dataBase;
    private boolean okClicked;

    public AddCourseController(Course course) {
        this.course = course;
        dataBase = BazaDAO.getInstance();
    }


    private void addColor(TextField textField, boolean valid) {
        if (textField.getText().isEmpty()) {
            textField.getStyleClass().removeAll("invalidField", "validField");
            textField.getStyleClass().add("blankField");
        } else if (valid) {
            textField.getStyleClass().removeAll("invalidField", "blankField");
            textField.getStyleClass().add("validField");
        } else {
            textField.getStyleClass().removeAll("validField", "blankField");
            textField.getStyleClass().add("invalidField");
        }
    }

    @FXML
    public void initialize() {
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.length() <= 3 || newValue.length() >= 60) {
                    addColor(nameField, false);
                    validField = false;
                } else {
                    addColor(nameField, true);
                    validField = true;
                }
            }
        });
        if (course != null)
            nameField.setText(course.getName());
    }

    public void cancelClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) nameField.getScene().getWindow();
        currentStage.close();
    }

    private void showAlert(String title, String headerText, Alert.AlertType type) {
        Alert error = new Alert(type);
        error.setTitle(title);
        error.setHeaderText(headerText);
        error.show();
    }

    public void okClick(ActionEvent actionEvent) {
        if (!validField)
            return;
        if (this.course == null) {
            try {
                addCourse();
            } catch (SQLException error) {
                showAlert("Greška", "Problem pri dodavanju: " + error.getMessage(), Alert.AlertType.ERROR);
                return;
            }
        } else {
            try {
                updateCourse();
            } catch (SQLException error) {
                showAlert("Greška", "Problem pri ažuriranju: " + error.getMessage(), Alert.AlertType.ERROR);
                return;
            }
        }
        okClicked = true;
        Stage currentStage = (Stage) nameField.getScene().getWindow();
        currentStage.close();
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    private void updateCourse() throws SQLException {
        course.setName(nameField.getText());
        dataBase.updateCourse(course);
        showAlert("Uspjeh", "Uspješno ažuriran smjer", Alert.AlertType.INFORMATION);
    }

    private void addCourse() throws SQLException {
        Course course = new Course();
        course.setName(nameField.getText());
        dataBase.addCourse(course);
        showAlert("Uspjeh", "Uspješno dodat smjer", Alert.AlertType.INFORMATION);
    }
}

package ba.unsa.etf.rpr.projekat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddSubjectController {
    public TextField nameField;
    public TextField codeField;
    public ChoiceBox<Professor> professorChoiceBox;
    public Spinner<Integer> ectsSpinner;
    public ChoiceBox<Subject> reqSubjectChoiceBox;
    private boolean[] validField;
    private BazaDAO dataBase;
    private Subject subject;

    public AddSubjectController(Subject subject) {
        validField = new boolean[2];
        dataBase = BazaDAO.getInstance();
        this.subject = subject;
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
        Thread thread = new Thread(() -> {
            try {
                var professors = FXCollections.observableArrayList(dataBase.professors());
                var subjects = FXCollections.observableArrayList(dataBase.subjects());
                Platform.runLater(() -> {
                    professorChoiceBox.setItems(professors);
                    reqSubjectChoiceBox.setItems(subjects);
                });
            } catch (SQLException error) {
                showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
            }
        });
        thread.start();
        ectsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1, 1));
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.length() <= 3 || newValue.length() >= 50) {
                    addColor(nameField, false);
                    validField[0] = false;
                } else {
                    addColor(nameField, true);
                    validField[0] = true;
                }
            }
        });

        codeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.length() <= 1 || newValue.length() >= 20) {
                    addColor(codeField, false);
                    validField[1] = false;
                } else {
                    addColor(codeField, true);
                    validField[1] = true;
                }
            }
        });

        if (subject != null) {
            nameField.setText(subject.getName());
            codeField.setText(subject.getCode());
            ectsSpinner.getValueFactory().setValue(subject.getEcts());
            professorChoiceBox.setValue(subject.getProfessor());
            reqSubjectChoiceBox.setValue(subject.getReqSubject());
        }
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

    private void addSubject() throws SQLException {
        Subject subject = new Subject();
        subject.setName(nameField.getText());
        subject.setCode(codeField.getText());
        subject.setEcts(ectsSpinner.getValue());
        subject.setProfessor(professorChoiceBox.getValue());
        dataBase.addSubject(subject);
        showAlert("Uspjeh", "Uspješno dodat predmet", Alert.AlertType.INFORMATION);
    }

    private void updateSubject() throws SQLException {
        subject.setName(nameField.getText());
        subject.setCode(codeField.getText());
        subject.setEcts(ectsSpinner.getValue());
        subject.setProfessor(professorChoiceBox.getValue());
        subject.setReqSubject(reqSubjectChoiceBox.getValue());
        dataBase.updateSubject(subject);
        showAlert("Uspjeh", "Uspješno ažuriran predmet", Alert.AlertType.INFORMATION);
    }


    public void okClick(ActionEvent actionEvent) {
        if (!validField[0] || !validField[1])
            return;
        if (this.subject == null) {
            try {
                addSubject();
            } catch (SQLException error) {
                showAlert("Greška", "Problem pri dodavanju: " + error.getMessage(), Alert.AlertType.ERROR);
                return;
            }
        } else {
            try {
                updateSubject();
            } catch (SQLException error) {
                showAlert("Greška", "Problem pri ažuriranju: " + error.getMessage(), Alert.AlertType.ERROR);
                return;
            }
        }
        Stage currentStage = (Stage) nameField.getScene().getWindow();
        currentStage.close();
    }

    public void spinnerStep(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.UP)
            ectsSpinner.increment();
        else if (keyEvent.getCode() == KeyCode.DOWN)
            ectsSpinner.decrement();
    }
}

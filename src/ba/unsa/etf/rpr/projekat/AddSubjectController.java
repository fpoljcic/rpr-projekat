package ba.unsa.etf.rpr.projekat;

import javafx.application.Platform;
import javafx.collections.ObservableList;
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
    private boolean okClicked;
    private ObservableList<Professor> professors;
    private ObservableList<Subject> subjects;

    public AddSubjectController(Subject subject, ObservableList<Professor> professors, ObservableList<Subject> subjects) {
        validField = new boolean[2];
        dataBase = BazaDAO.getInstance();
        this.subject = subject;
        this.professors = professors;
        this.subjects = subjects;
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
            Platform.runLater(() -> {
                professorChoiceBox.setItems(professors);
                if (this.subject == null)
                    professorChoiceBox.getSelectionModel().select(0);
                else
                    professorChoiceBox.getSelectionModel().select(this.subject.getProfessor());
            });
            Platform.runLater(() -> {
                Subject subject = new Subject();
                subject.setName("Bez uslovnog predmeta");
                reqSubjectChoiceBox.getItems().add(subject);
                reqSubjectChoiceBox.getItems().addAll(subjects);
                if (this.subject == null)
                    reqSubjectChoiceBox.getSelectionModel().select(0);
                else if (this.subject.getReqSubject() == null)
                    reqSubjectChoiceBox.getSelectionModel().select(0);
                else
                    reqSubjectChoiceBox.getSelectionModel().select(this.subject.getReqSubject());
            });
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
        editSubject(subject);
        dataBase.addSubject(subject);
        showAlert("Uspjeh", "Uspješno dodat predmet", Alert.AlertType.INFORMATION);
    }

    private void updateSubject() throws SQLException {
        editSubject(subject);
        dataBase.updateSubject(subject);
        showAlert("Uspjeh", "Uspješno ažuriran predmet", Alert.AlertType.INFORMATION);
    }

    private void editSubject(Subject subject) {
        subject.setName(nameField.getText());
        subject.setCode(codeField.getText());
        subject.setEcts(ectsSpinner.getValue());
        subject.setProfessor(professorChoiceBox.getValue());
        if (reqSubjectChoiceBox.getValue().getName().equals("Bez uslovnog predmeta"))
            subject.setReqSubject(null);
        else
            subject.setReqSubject(reqSubjectChoiceBox.getValue());
    }


    public void okClick(ActionEvent actionEvent) {
        if (!validField[0] || !validField[1])
            return;
        if (nameField.getText().equals(reqSubjectChoiceBox.getValue().getName())) {
            showAlert("Greška", "Ne možete postaviti dati uslovni predmet", Alert.AlertType.ERROR);
            return;
        }
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
        okClicked = true;
        Stage currentStage = (Stage) nameField.getScene().getWindow();
        currentStage.close();
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void spinnerStep(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.UP)
            ectsSpinner.increment();
        else if (keyEvent.getCode() == KeyCode.DOWN)
            ectsSpinner.decrement();
    }
}

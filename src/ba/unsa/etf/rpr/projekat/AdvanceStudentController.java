package ba.unsa.etf.rpr.projekat;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class AdvanceStudentController {
    public ListView<Subject> optionalSubjectsList;
    public ListView<Subject> requiredSubjectsList;
    public Label welcomeLabel;
    public Label totalEcts;
    private Subject selectedOptSubject;
    private Subject selectedReqSubject;
    private Student student;
    private BazaDAO dataBase;
    private SimpleStringProperty ectsSum;
    private boolean okClicked;
    private Semester nextSemester;
    private int noReqSubjects;
    private ArrayList<Subject> subjectsPassed;

    public AdvanceStudentController(Student student, ObservableList<SubjectGradeWrapper> subjects) {
        this.student = student;
        subjectsPassed = new ArrayList<>();
        for (SubjectGradeWrapper subjectGradeWrapper : subjects)
            subjectsPassed.add(subjectGradeWrapper.getSubject());
        dataBase = BazaDAO.getInstance();
        ectsSum = new SimpleStringProperty("0");
    }

    @FXML
    public void initialize() {
        try {
            nextSemester = dataBase.getSemester(student.getSemester().getId() + 1);
            ArrayList<Subject> reqSubjects = dataBase.getSubjects(nextSemester, student.getCourse(), true);
            noReqSubjects = reqSubjects.size();
            int ectsSum = 0;
            for (Subject reqSubject : reqSubjects)
                ectsSum += reqSubject.getEcts();
            this.ectsSum.set(String.valueOf(ectsSum));
            ArrayList<Subject> optSubjects = dataBase.getSubjects(nextSemester, student.getCourse(), false);
            requiredSubjectsList.setItems(FXCollections.observableArrayList(reqSubjects));
            optionalSubjectsList.setItems(FXCollections.observableArrayList(optSubjects));
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
            logOut();
            return;
        }
        setListeners();
        welcomeLabel.setText("Ostvarili ste uslove za prelazak na sljedeći semestar.\n" +
                "Za upis u sljedeći semestar vam je potrebno " + nextSemester.getEcts() + " ECTS bodova.\n" +
                "Odaberite željene izborne predmete koristeći opcije navedene ispod.");
    }

    private void logOut() {
        BazaDAO.removeInstance();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("Prijava");
            loginStage.getIcons().add(new Image("/img/login.png"));
            loginStage.setResizable(false);
            loginStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            Stage currentStage = (Stage) welcomeLabel.getScene().getWindow();
            currentStage.close();
            loginStage.show();
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void setListeners() {
        requiredSubjectsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (requiredSubjectsList.getItems().indexOf(newValue) >= noReqSubjects)
                selectedReqSubject = newValue;
            else
                selectedReqSubject = null;
        });
        optionalSubjectsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedOptSubject = newValue);
        totalEcts.textProperty().bindBidirectional(ectsSum);
    }

    private void addEcts(int ects) {
        ectsSum.set(String.valueOf(Integer.valueOf(ectsSum.get()) + ects));
    }

    private void removeEcts(int ects) {
        ectsSum.set(String.valueOf(Integer.valueOf(ectsSum.get()) - ects));
    }

    public void okClick(ActionEvent actionEvent) {
        // Provjera ukupnog broja ects bodova
        if (Integer.valueOf(ectsSum.get()) < nextSemester.getEcts()) {
            showAlert("Greška", "Potrebno vam je minimalno " + totalEcts.getText() + " ECTS bodova za sljedeći semestar", Alert.AlertType.ERROR);
            return;
        }
        // Provjera da li je student polozio sve uslovne predmete za odabrane predmete
        for (Subject subject : requiredSubjectsList.getItems()) {
            if (subject.getReqSubject() != null && !subjectsPassed.contains(subject.getReqSubject())) {
                showAlert("Greška", "Za predmet " + subject + " potrebno je da položite " + subject.getReqSubject(), Alert.AlertType.ERROR);
                return;
            }
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Potvrda");
        confirmationAlert.setHeaderText("Da li ste sigurni sa datim izborom ?");
        confirmationAlert.showAndWait();
        if (confirmationAlert.getResult() != ButtonType.OK)
            return;
        student.setSemester(nextSemester);
        try {
            dataBase.updateStudent(student);
            dataBase.advanceStudent(student, requiredSubjectsList.getItems());
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        okClicked = true;
        Stage currentStage = (Stage) optionalSubjectsList.getScene().getWindow();
        currentStage.close();
    }

    public void cancelClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) optionalSubjectsList.getScene().getWindow();
        currentStage.close();
    }

    public void addSubject(ActionEvent actionEvent) {
        if (selectedOptSubject != null) {
            requiredSubjectsList.getItems().add(selectedOptSubject);
            addEcts(selectedOptSubject.getEcts());
            optionalSubjectsList.getItems().remove(selectedOptSubject);
            optionalSubjectsList.getSelectionModel().clearSelection();
        }
    }

    public void removeSubject(ActionEvent actionEvent) {
        if (selectedReqSubject != null) {
            optionalSubjectsList.getItems().add(selectedReqSubject);
            removeEcts(selectedReqSubject.getEcts());
            requiredSubjectsList.getItems().remove(selectedReqSubject);
            requiredSubjectsList.getSelectionModel().clearSelection();
        }
    }

    private void showAlert(String title, String headerText, Alert.AlertType type) {
        Alert error = new Alert(type);
        error.setTitle(title);
        error.setHeaderText(headerText);
        error.show();
    }

    public boolean isOkClicked() {
        return okClicked;
    }
}

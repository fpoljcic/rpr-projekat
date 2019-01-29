package ba.unsa.etf.rpr.projekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class LoginController {
    public TextField passwordField;
    public TextField usernameField;
    public RadioButton studentRadioBtn;
    public RadioButton professorRadioBtn;
    public RadioButton adminRadioBtn;
    private String selectedUser;
    private BazaDAO dataBase;
    private ToggleGroup toggleGroup;
    private Login login;

    @FXML
    public void initialize() {
        toggleGroup = new ToggleGroup();
        studentRadioBtn.setToggleGroup(toggleGroup);
        professorRadioBtn.setToggleGroup(toggleGroup);
        adminRadioBtn.setToggleGroup(toggleGroup);
        adminRadioBtn.setSelected(true);
        selectedUser = adminRadioBtn.getText();
        studentRadioBtn.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                selectedUser = studentRadioBtn.getText();
        });
        professorRadioBtn.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                selectedUser = professorRadioBtn.getText();
        });
        adminRadioBtn.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                selectedUser = adminRadioBtn.getText();
        });
        dataBase = BazaDAO.getInstance();
    }

    private void showAlert(String title, String headerText, Alert.AlertType type) {
        Alert error = new Alert(type);
        error.setTitle(title);
        error.setHeaderText(headerText);
        error.show();
    }

    public void loginClick(ActionEvent actionEvent) {
        if (usernameField.getText().isEmpty()) {
            showAlert("Greška", "Unesite korisničko ime", Alert.AlertType.ERROR);
            return;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert("Greška", "Unesite šifru", Alert.AlertType.ERROR);
            return;
        }
        login = dataBase.getLogin(usernameField.getText().trim(), passwordField.getText().trim(), selectedUser);
        if (login == null) {
            showAlert("Greška", "Ne postoji korisnik sa datim podacima", Alert.AlertType.ERROR);
            return;
        }
        RadioButton selectedToggle = (RadioButton) toggleGroup.getSelectedToggle();
        Stage mainStage = getNewStage(selectedToggle.getText());
        if (mainStage == null) {
            showAlert("Greška", "Nepoznata greška", Alert.AlertType.ERROR);
            return;
        }
        login.setLastLoginDate(LocalDate.now());
        try {
            dataBase.updateLogin(login);
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        Stage currentStage = (Stage) usernameField.getScene().getWindow();
        currentStage.close();
        mainStage.show();
    }

    private Stage getNewStage(String stageName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + stageName.toLowerCase() + ".fxml"));
            switch (stageName) {
                case "Administrator":
                    loader.setController(new AdministratorController(login));
                    break;
                case "Student":
                    loader.setController(new StudentController(login));
                    break;
                case "Professor":
                    loader.setController(new ProfessorController(login));
                    break;
            }
            Parent root = loader.load();
            Stage mainStage = new Stage();
            mainStage.setTitle(stageName);
            mainStage.getIcons().add(new Image("/img/" + stageName.toLowerCase() + ".png"));
            mainStage.setResizable(false);
            mainStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            return mainStage;
        } catch (IOException ignored) {
            ignored.printStackTrace();
            return null;
        }
    }

    public void keyEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            loginClick(null);
    }
}

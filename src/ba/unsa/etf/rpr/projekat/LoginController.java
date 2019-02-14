package ba.unsa.etf.rpr.projekat;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class LoginController {
    public TextField passwordField;
    public TextField usernameField;
    public RadioButton studentRadioBtn;
    public RadioButton professorRadioBtn;
    public RadioButton adminRadioBtn;
    public Button passForgotButton;
    private BazaDAO dataBase;
    private ToggleGroup toggleGroup;
    private Login login;
    private SimpleStringProperty username, password;
    private SimpleIntegerProperty errorCount;

    public LoginController() {
        dataBase = BazaDAO.getInstance();
        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
        errorCount = new SimpleIntegerProperty(0);
    }

    private String getUsername() {
        return username.get();
    }

    private String getPassword() {
        return password.get();
    }

    @FXML
    public void initialize() {
        passForgotButton.setVisible(false);
        toggleGroup = new ToggleGroup();
        studentRadioBtn.setToggleGroup(toggleGroup);
        professorRadioBtn.setToggleGroup(toggleGroup);
        adminRadioBtn.setToggleGroup(toggleGroup);
        adminRadioBtn.setSelected(true);
        errorCount.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() >= 2)
                passForgotButton.setVisible(true);
            else
                passForgotButton.setVisible(false);
        });
        usernameField.textProperty().bindBidirectional(username);
        passwordField.textProperty().bindBidirectional(password);
    }

    public void passForgotClick(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/passRecovery.fxml"));
            Stage recoveryStage = new Stage();
            recoveryStage.setTitle("Resetuj šifru");
            recoveryStage.getIcons().add(new Image("/img/password.png"));
            recoveryStage.setResizable(false);
            recoveryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            recoveryStage.initModality(Modality.APPLICATION_MODAL);
            recoveryStage.showAndWait();
            errorCount.set(0);
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String headerText, Alert.AlertType type) {
        Alert error = new Alert(type);
        error.setTitle(title);
        error.setHeaderText(headerText);
        error.show();
    }

    public void loginClick(ActionEvent actionEvent) {
        if (getUsername().isEmpty() || getUsername().length() < 3) {
            showAlert("Greška", "Unesite korisničko ime", Alert.AlertType.ERROR);
            return;
        }
        if (getPassword().isEmpty() || getPassword().length() < 4) {
            showAlert("Greška", "Unesite šifru", Alert.AlertType.ERROR);
            return;
        }
        RadioButton selectedToggle = (RadioButton) toggleGroup.getSelectedToggle();
        login = dataBase.getLogin(getUsername().trim(), selectedToggle.getText());
        if (login == null) {
            showAlert("Greška", "Ne postoji korisnik sa datim podacima", Alert.AlertType.ERROR);
            return;
        }
        if (!checkPasswordMatch(getUsername(), getPassword())) {
            showAlert("Greška", "Ne postoji korisnik sa datim podacima", Alert.AlertType.ERROR);
            errorCount.set(errorCount.get() + 1);
            return;
        }
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
            FXMLLoader loader;
            Stage mainStage = new Stage();
            switch (stageName) {
                case "Administrator":
                    loader = new FXMLLoader(getClass().getResource("/fxml/administrator.fxml"));
                    mainStage.getIcons().add(new Image("/img/administrator.png"));
                    AdministratorController controller = new AdministratorController(login);
                    loader.setController(controller);
                    mainStage.setOnHidden(event -> {
                        try {
                            DataOutputStream output = new DataOutputStream(new FileOutputStream("resources/config.dat"));
                            ArrayList<Boolean> tabsConfig = controller.getTabsConfig();
                            for (Boolean value : tabsConfig)
                                output.writeBoolean(value);
                            output.close();
                        } catch (IOException error) {
                            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
                        }
                    });
                    break;
                case "Student":
                    loader = new FXMLLoader(getClass().getResource("/fxml/student.fxml"));
                    mainStage.getIcons().add(new Image("/img/student.png"));
                    loader.setController(new StudentController(login));
                    break;
                case "Profesor":
                    loader = new FXMLLoader(getClass().getResource("/fxml/professor.fxml"));
                    mainStage.getIcons().add(new Image("/img/professor.png"));
                    loader.setController(new ProfessorController(login));
                    break;
                default:
                    return null;
            }
            Parent root = loader.load();
            mainStage.setTitle(stageName);
            mainStage.setResizable(false);
            mainStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            return mainStage;
        } catch (IOException ignored) {
            return null;
        }
    }

    public void keyEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            loginClick(null);
    }

    private boolean checkPasswordMatch(String username, String password) {
        String pass = getEncodedPassword(username, password);
        return pass.equals(login.getPassword());
    }

    public static String getEncodedPassword(String user, String pass) {
        return encodePassword(user) + encodePassword(pass);
    }

    private static String encodePassword(String pass) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException ignored) {
        }
        return generatedPassword;
    }
}

package ba.unsa.etf.rpr.projekat;

import javafx.beans.property.SimpleStringProperty;
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

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
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
    private String selectedUser;
    private BazaDAO dataBase;
    private ToggleGroup toggleGroup;
    private Login login;
    private SimpleStringProperty username, password;


    public LoginController() {
        dataBase = BazaDAO.getInstance();
        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
    }

    private String getUsername() {
        return username.get();
    }

    private String getPassword() {
        return password.get();
    }

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
        usernameField.textProperty().bindBidirectional(username);
        passwordField.textProperty().bindBidirectional(password);
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
        login = dataBase.getLogin(getUsername().trim(), selectedUser);
        if (login == null || !checkPasswordMatch(login.getPassword())) {
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

    private static String encodePassword(String user, String pass) {
        String encodedPassword = "";
        var usernameChars = getUsernameChars(user);
        var passwordChars = getPasswordChars(pass);
        for (var symbol : usernameChars)
            encodedPassword += symbol;
        for (var symbol : passwordChars)
            encodedPassword += symbol;
        return encodedPassword;
    }

    private static char[] getPasswordChars(String pass) {
        char[] chars = pass.toCharArray();
        int[] ints = new int[chars.length];
        for (int i = 0; i < ints.length; i++)
            ints[i] = chars[i];
        for (int i = 0; i < ints.length; i++)
            ints[i] = (ints[i] * (i + 1)) % 35;
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] <= 9)
                ints[i] += 48;
            else
                ints[i] += 87;
        }
        for (int i = 0; i < ints.length; i++)
            chars[i] = (char) ints[i];
        return chars;
    }

    private static char[] getUsernameChars(String user) {
        char[] chars = new char[3];
        int[] ints = new int[3];
        ints[0] = user.charAt(0);
        ints[1] = user.charAt((user.length() - 1) / 2);
        ints[2] = user.charAt(user.length() - 1);
        for (int i = 0; i < 3; i++)
            ints[i] = (ints[i] * (i + 1)) % 35;
        for (int i = 0; i < 3; i++) {
            if (ints[i] <= 9)
                ints[i] += 48;
            else
                ints[i] += 87;
        }
        for (int i = 0; i < 3; i++)
            chars[i] = (char) ints[i];
        return chars;
    }

    private boolean checkPasswordMatch(String actualPass) {
        String pass = encodePassword(getUsername(), getPassword());
        int j = 1;
        boolean[] dontCheckUser = new boolean[3];
        for (int i = 3; i < pass.length(); i++) {
            if (j == 0)
                dontCheckUser[0] = true;
            else if (j == 26)
                dontCheckUser[1] = true;
            else if (j == 48)
                dontCheckUser[2] = true;
            if (actualPass.charAt(j) != pass.charAt(i))
                return false;
            j += 2;
            if (i == 25)
                j = 2;
        }
        return (dontCheckUser[0] || actualPass.charAt(0) == pass.charAt(0)) && (dontCheckUser[1] || actualPass.charAt(26) == pass.charAt(1)) && (dontCheckUser[2] || actualPass.charAt(48) == pass.charAt(2));
    }


    private static String generateRandomString(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[length];
        secureRandom.nextBytes(token);
        return new BigInteger(1, token).toString(35).substring(0, length); //hex encoding
    }

    public static String getEncodedPassword(String user, String pass) {
        StringBuilder encodedPass = new StringBuilder(generateRandomString(50));
        String encodedPassword = encodePassword(user, pass);
        encodedPass.setCharAt(0, encodedPassword.charAt(0));
        encodedPass.setCharAt(26, encodedPassword.charAt(1));
        encodedPass.setCharAt(48, encodedPassword.charAt(2));
        int j = 1;
        for (int i = 3; i < encodedPassword.length(); i++) {
            encodedPass.setCharAt(j, encodedPassword.charAt(i));
            j += 2;
            if (i == 25)
                j = 2;
        }
        return encodedPass.toString();
    }
}

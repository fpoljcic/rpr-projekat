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
        toggleGroup = new ToggleGroup();
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
        if (getUsername() == null || getUsername().isEmpty() || getUsername().length() < 3) {
            showAlert("Greška", "Unesite korisničko ime", Alert.AlertType.ERROR);
            return;
        }
        if (getPassword() == null || getPassword().isEmpty() || getPassword().length() < 4) {
            showAlert("Greška", "Unesite šifru", Alert.AlertType.ERROR);
            return;
        }
        RadioButton selectedToggle = (RadioButton) toggleGroup.getSelectedToggle();
        login = dataBase.getLogin(getUsername().trim(), selectedToggle.getText());
        if (login == null) {
            showAlert("Greška", "Ne postoji korisnik sa datim podacima", Alert.AlertType.ERROR);
            return;
        }
        if (!checkPasswordMatch(getUsername(), getPassword(), login.getId())) {
            showAlert("Greška", "Ne postoji korisnik sa datim podacima", Alert.AlertType.ERROR);
            errorCount.set(errorCount.get() + 1);
            return;
        }
        Stage mainStage = getNewStage(selectedToggle.getText());
        if (mainStage == null)
            return;
        LocalDate lastLoginDate = login.getLastLoginDate();
        login.setLastLoginDate(LocalDate.now());
        try {
            dataBase.updateLogin(login);
        } catch (SQLException error) {
            login.setLastLoginDate(lastLoginDate);
            showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        Stage currentStage = (Stage) usernameField.getScene().getWindow();
        currentStage.close();
        mainStage.show();
    }

    private void writeAdminView(ArrayList<Boolean> tabsConfig) {
        try {
            boolean studentView = readStudentView();
            tabsConfig.add(0, studentView);
            writeBoolArray(tabsConfig);
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean readStudentView() {
        boolean tabConfig = true;
        try {
            DataInputStream input = new DataInputStream(new FileInputStream("resources/config.dat"));
            tabConfig = input.readBoolean();
            input.close();
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
        return tabConfig;
    }

    private ArrayList<Boolean> readAdminView() {
        ArrayList<Boolean> tabsConfig = new ArrayList<>();
        try {
            DataInputStream input = new DataInputStream(new FileInputStream("resources/config.dat"));
            input.readBoolean();
            for (int i = 0; i < 6; i++)
                tabsConfig.add(input.readBoolean());
            input.close();
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
        return tabsConfig;
    }

    private void writeStudentView(boolean tabConfig) {
        try {
            ArrayList<Boolean> adminView = readAdminView();
            adminView.add(0, tabConfig);
            writeBoolArray(adminView);
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void writeBoolArray(ArrayList<Boolean> values) throws IOException {
        DataOutputStream output = new DataOutputStream(new FileOutputStream("resources/config.dat"));
        for (Boolean bool : values)
            output.writeBoolean(bool);
        output.close();
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
                    mainStage.setOnHidden(event -> writeAdminView(controller.getTabsConfig()));
                    break;
                case "Student":
                    loader = new FXMLLoader(getClass().getResource("/fxml/student.fxml"));
                    mainStage.getIcons().add(new Image("/img/student.png"));
                    StudentController studentController = new StudentController(login);
                    loader.setController(studentController);
                    mainStage.setOnHidden(event -> writeStudentView(studentController.getTabConfig()));
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

    private static String formPasswordId(int id, String password) {
        StringBuilder stringBuilder = new StringBuilder(password);
        stringBuilder.insert(id % password.length(), id);
        return stringBuilder.toString();
    }

    private boolean checkPasswordMatch(String username, String password, int id) {
        String pass = getEncodedPassword(username, password, id);
        return pass.equals(login.getPassword());
    }

    public static String getEncodedPassword(String user, String pass, int id) {
        return encodePassword(user) + encodePassword(formPasswordId(id, pass));
    }

    private static String encodePassword(String pass) {
        String generatedPassword;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes)
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException ignored) {
            return null;
        }
        return generatedPassword;
    }
}

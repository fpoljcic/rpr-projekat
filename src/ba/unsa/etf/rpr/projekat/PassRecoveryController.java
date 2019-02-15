package ba.unsa.etf.rpr.projekat;

import javafx.application.Platform;
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

import javax.mail.MessagingException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class PassRecoveryController {
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField emailField;
    public RadioButton studentRadioBtn;
    public RadioButton professorRadioBtn;
    public RadioButton adminRadioBtn;
    private Email email;
    private BazaDAO dataBase;
    private ToggleGroup toggleGroup;
    private String code;


    public PassRecoveryController() {
        email = new Email();
        email.setUserName("eindexfp");
        email.setPassword("etf18120FP");
        dataBase = BazaDAO.getInstance();
    }

    @FXML
    public void initialize() {
        toggleGroup = new ToggleGroup();
        studentRadioBtn.setToggleGroup(toggleGroup);
        professorRadioBtn.setToggleGroup(toggleGroup);
        adminRadioBtn.setToggleGroup(toggleGroup);
        adminRadioBtn.setSelected(true);
    }

    public void keyEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            recoverClick(null);
    }

    private String getFirstName() {
        return firstNameField.getText();
    }

    private String getLastName() {
        return lastNameField.getText();
    }

    private String getEmail() {
        return emailField.getText();
    }

    private String getCode() {
        return code;
    }

    private boolean validEmail() {
        // Prepisano sa StackOverflow
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(getEmail());
        return m.matches();
    }

    public void recoverClick(ActionEvent actionEvent) {
        if (getFirstName().length() < 2) {
            showAlert("Greška", "Unesite ime", Alert.AlertType.ERROR);
            return;
        }
        if (getLastName().length() < 2) {
            showAlert("Greška", "Unesite prezime", Alert.AlertType.ERROR);
            return;
        }
        if (!validEmail()) {
            showAlert("Greška", "Unesite ispravan email", Alert.AlertType.ERROR);
            return;
        }
        int id;
        try {
            RadioButton selectedToggle = (RadioButton) toggleGroup.getSelectedToggle();
            id = dataBase.getLoginId(getFirstName(), getLastName(), getEmail(), selectedToggle.getText());
            if (id == -1) {
                showAlert("Greška", "Ne postoji korisnik sa datim podacima", Alert.AlertType.ERROR);
                return;
            }
            generateCode();
            email.setSubject("Zahtjev za resetovanje šifre");
            setEmailBody(email);
            email.addRecipient(getEmail());
            Thread thread = new Thread(() -> {
                try {
                    email.send();
                } catch (WrongEmailException | MessagingException error) {
                    Platform.runLater(() -> showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR));
                }
            });
            thread.start();
        } catch (SQLException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/passCode.fxml"));
            PassCodeController controller = new PassCodeController(getCode());
            loader.setController(controller);
            Parent root = loader.load();
            Stage codeValidationForm = new Stage();
            codeValidationForm.setTitle("Unesite kontrolni kod");
            codeValidationForm.getIcons().add(new Image("/img/shield.png"));
            codeValidationForm.setResizable(false);
            codeValidationForm.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            codeValidationForm.initModality(Modality.APPLICATION_MODAL);
            codeValidationForm.showAndWait();
            if (!controller.isValid())
                cancelClick(null);
            else {
                showNewPassForm(id);
                cancelClick(null);
            }
        } catch (IOException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void setEmailBody(Email email) {
        String html = "Poštovanje,<br/><br/>" +
                "Primili smo zahtjev za pristup vašem računu " + getAccountInfo() +
                " putem aplikacije E-index. Ispod se nalazi vaš kontrolni kod:<br/><br/>" +
                "<div style=\"text-align:center\"><strong style=\"font-size:24px; font-weight:bold\">" + getCode() +
                "</strong></div>" +
                "<br/><br/>Ako niste zatražili ovaj kod, možda netko drugi pokušava pristupiti vašem računu. Ne prosljeđujte ovaj kod i ne otkrivajte ga nikome.<br/><br/>" +
                "Lijep pozdrav,<br/>" +
                "Faris Poljčić";
        email.setBody(html);
    }

    private String getAccountInfo() {
        return "(" + getFirstName() + " " + getLastName() + " - " + getEmail() + ")";
    }

    private void showNewPassForm(int id) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/passNew.fxml"));
        PassNewController controller = new PassNewController(id);
        loader.setController(controller);
        Parent root = loader.load();
        Stage newPassForm = new Stage();
        newPassForm.setTitle("Unesite novu šifru");
        newPassForm.getIcons().add(new Image("/img/key.png"));
        newPassForm.setResizable(false);
        newPassForm.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        newPassForm.initModality(Modality.APPLICATION_MODAL);
        newPassForm.showAndWait();
    }

    public void cancelClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) firstNameField.getScene().getWindow();
        currentStage.close();
    }

    private void generateCode() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[6];
        secureRandom.nextBytes(token);
        code = new BigInteger(1, token).toString(32).substring(0, 6); //hex encoding
    }

    private void showAlert(String title, String headerText, Alert.AlertType type) {
        Alert error = new Alert(type);
        error.setTitle(title);
        error.setHeaderText(headerText);
        error.show();
    }
}

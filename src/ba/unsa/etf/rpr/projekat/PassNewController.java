package ba.unsa.etf.rpr.projekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class PassNewController {
    public TextField passField;
    private BazaDAO dataBase;
    private Login login;

    public PassNewController(int id) {
        dataBase = BazaDAO.getInstance();
        login = dataBase.getLogin(id);
    }

    @FXML
    public void initialize() {
        passField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                passField.getStyleClass().removeAll("invalidField", "validField");
                passField.getStyleClass().add("blankField");
            } else if (newValue.length() > 50 || newValue.length() < 4) {
                passField.getStyleClass().removeAll("validField", "blankField");
                passField.getStyleClass().add("invalidField");
            } else {
                passField.getStyleClass().removeAll("invalidField", "blankField");
                passField.getStyleClass().add("validField");
            }
        });
    }

    public void cancelClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) passField.getScene().getWindow();
        currentStage.close();
    }

    public void okClick(ActionEvent actionEvent) throws SQLException {
        if (passField.getStyleClass().contains("validField")) {
            login.setPassword(LoginController.getEncodedPassword(login.getUsername(), passField.getText(), login.getId()));
            dataBase.updateLogin(login);
            showAlert("Uspjeh", "Uspješno izmijenjena šifra", Alert.AlertType.INFORMATION);
            cancelClick(null);
        }
    }

    private void showAlert(String title, String headerText, Alert.AlertType type) {
        Alert error = new Alert(type);
        error.setTitle(title);
        error.setHeaderText(headerText);
        error.show();
    }
}

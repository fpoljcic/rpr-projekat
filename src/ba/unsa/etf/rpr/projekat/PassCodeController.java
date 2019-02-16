package ba.unsa.etf.rpr.projekat;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PassCodeController {
    public TextField codeField;
    private String originalCode;
    private boolean valid;
    private SimpleIntegerProperty errorCount;

    public PassCodeController(String originalCode) {
        this.originalCode = originalCode;
        errorCount = new SimpleIntegerProperty(0);
    }

    @FXML
    public void initialize() {
        errorCount.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 3)
                cancelClick(null);
        });
    }

    public void okClick(ActionEvent actionEvent) {
        if (!codeField.getText().equals(originalCode)) {
            if (errorCount.get() == 2)
                showAlert("Greška", "Nema te više preostalih pokušaja", Alert.AlertType.ERROR);
            else
                showAlert("Greška", "Dati kod nije validan (preostalo " + (2 - errorCount.get()) + " pokušaja)", Alert.AlertType.ERROR);
            errorCount.set(errorCount.get() + 1);
            return;
        }
        cancelClick(null);
        valid = true;
    }

    public void cancelClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) codeField.getScene().getWindow();
        currentStage.close();
    }

    public boolean isValid() {
        return valid;
    }

    private void showAlert(String title, String headerText, Alert.AlertType type) {
        Alert error = new Alert(type);
        error.setTitle(title);
        error.setHeaderText(headerText);
        error.show();
    }
}

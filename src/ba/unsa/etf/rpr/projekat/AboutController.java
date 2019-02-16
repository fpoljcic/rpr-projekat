package ba.unsa.etf.rpr.projekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class AboutController {
    public Button buttonOk;
    public ImageView imageView;

    @FXML
    public void initialize() {
        imageView.setImage(new Image("/img/user.png"));
    }

    public void buttonOkClick(ActionEvent actionEvent) {
        Stage stage = (Stage) buttonOk.getScene().getWindow();
        stage.close();
    }
}


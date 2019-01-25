package ba.unsa.etf.rpr.projekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class AboutController {

    public Label aboutText;
    public Button buttonOk;
    public ImageView imageView;

    @FXML
    public void initialize() {
        //File file = new File("about.png");
        //Image image = new Image(file.toURI().toString());
        //imageView.setImage(image);
        imageView.setImage(new Image("/img/about.png"));
        aboutText.setText("Naziv: E-index\nAutor: Faris Poljčić");
    }

    public void buttonOkClick(ActionEvent actionEvent) {
        Stage stage = (Stage) buttonOk.getScene().getWindow();
        stage.close();
    }
}


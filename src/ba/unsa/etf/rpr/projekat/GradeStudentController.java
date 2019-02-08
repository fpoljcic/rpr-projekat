package ba.unsa.etf.rpr.projekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.text.ParsePosition;

public class GradeStudentController {
    public Label studentLabel;
    public Spinner<Double> pointsSpinner;
    public Spinner<Integer> scoreSpinner;
    public RadioButton gradeRadioBtn;
    public RadioButton noGradeRadioBtn;
    private Student student;
    private Grade grade;
    private Integer calculatedGrade;
    private boolean okClicked;
    private BazaDAO dataBase;

    public GradeStudentController(Student student, Grade grade) {
        this.student = student;
        this.grade = grade;
        dataBase = BazaDAO.getInstance();
    }


    private void addColor(TextField textField, boolean valid) {
        if (textField.getText().isEmpty()) {
            textField.getStyleClass().removeAll("invalidField", "validField");
            textField.getStyleClass().add("blankField");
        } else if (valid) {
            textField.getStyleClass().removeAll("invalidField", "blankField");
            textField.getStyleClass().add("validField");
        } else {
            textField.getStyleClass().removeAll("validField", "blankField");
            textField.getStyleClass().add("invalidField");
        }
    }

    @FXML
    public void initialize() {
        ToggleGroup toggleGroup = new ToggleGroup();
        gradeRadioBtn.setToggleGroup(toggleGroup);
        noGradeRadioBtn.setToggleGroup(toggleGroup);
        noGradeRadioBtn.setSelected(true);
        pointsSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                /*
                if (newValue.length() == 0 || newValue.length() > 3 || Float.valueOf(newValue) > 110) {
                    addColor(pointsSpinner.getEditor(), false);
                    return;
                }
                */
                Float value = Float.valueOf(newValue);
                if (value < 55)
                    calculatedGrade = 5;
                else if (value >= 55 && value < 65)
                    calculatedGrade = 6;
                else if (value >= 65 && value < 75)
                    calculatedGrade = 7;
                else if (value >= 75 && value < 85)
                    calculatedGrade = 8;
                else if (value >= 85 && value < 95)
                    calculatedGrade = 9;
                else if (value >= 95)
                    calculatedGrade = 10;
                scoreSpinner.getEditor().setText(String.valueOf(calculatedGrade));
            }
        });
        DecimalFormat format = new DecimalFormat("#.0");
        pointsSpinner.getEditor().setTextFormatter(new TextFormatter<>((c -> {
            if (c.getControlNewText().isEmpty())
                return c;
            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(c.getControlNewText(), parsePosition);
            if (object == null || parsePosition.getIndex() < c.getControlNewText().length())
                return null;
            else
                return c;
        })));

        pointsSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(grade.getPoints(), 110, grade.getPoints(), 0.1));
        scoreSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 10, calculatedGrade, 1));
    }


    public void okClick(ActionEvent actionEvent) {
        grade.setPoints(pointsSpinner.getValue().floatValue());
        if (gradeRadioBtn.isSelected())
            grade.setScore(Integer.valueOf(scoreSpinner.getEditor().getText()));
        else
            grade.setScore(0);
        /*
        try {
            dataBase.updateGrade(grade);
        } catch (SQLException error) {
            showAlert("Greška", "Problem: " + error.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        okClicked = true;
        Stage currentStage = (Stage) pointsSpinner.getScene().getWindow();
        currentStage.close();
        */
        System.out.println(grade.getPoints());
        System.out.println(grade.getScore());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    private void showAlert(String title, String headerText, Alert.AlertType type) {
        Alert error = new Alert(type);
        error.setTitle(title);
        error.setHeaderText(headerText);
        error.show();
    }

    public void cancelClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) pointsSpinner.getScene().getWindow();
        currentStage.close();
    }
}

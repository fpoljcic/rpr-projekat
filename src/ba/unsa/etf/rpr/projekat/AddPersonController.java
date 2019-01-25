package ba.unsa.etf.rpr.projekat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class AddPersonController {
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField jmbgField;
    public TextField addressField;
    public TextField emailField;
    public TextField usernameField;
    public PasswordField passwordField;
    public ChoiceBox<String> userTypeChoiceBox;
    public DatePicker birthDatePicker;
    public ChoiceBox<Semester> semesterChoiceBox;
    public ChoiceBox<Course> courseChoiceBox;
    public TextField titleField;
    public GridPane professorGridPane;
    public GridPane studentGridPane;
    private boolean[] validField;
    private BazaDAO dataBase;
    private Person person;

    public AddPersonController(Person person) {
        validField = new boolean[8];
        if (person instanceof Administrator)
            validField[7] = true;
        dataBase = BazaDAO.getInstance();
        this.person = person;
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
        Thread thread = new Thread(() -> {
            try {
                ArrayList<String> userTypes = new ArrayList<>();
                userTypes.add("Administrator");
                userTypes.add("Student");
                userTypes.add("Profesor");
                var semesters = FXCollections.observableArrayList(dataBase.semesters());
                var courses = FXCollections.observableArrayList(dataBase.courses());
                Platform.runLater(() -> {
                    userTypeChoiceBox.setItems(FXCollections.observableArrayList(userTypes));
                    semesterChoiceBox.setItems(semesters);
                    courseChoiceBox.setItems(courses);
                });
            } catch (SQLException error) {
                showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
            }
        });
        thread.start();
        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.length() <= 3 || newValue.length() >= 50) {
                    addColor(firstNameField, false);
                    validField[0] = false;
                } else {
                    addColor(firstNameField, true);
                    validField[0] = true;
                }
            }
        });

        if (person != null) {
            usernameField.setText(person.getLogin().getUsername());
            userTypeChoiceBox.setValue(person.getClass().getName());

            firstNameField.setText(person.getFirstName());
            lastNameField.setText(person.getLastName());
            jmbgField.setText(person.getJmbg());
            addressField.setText(person.getAddress());
            emailField.setText(person.getEmail());

            if (person instanceof Administrator) {
                studentGridPane.setDisable(true);
                professorGridPane.setDisable(true);
            } else if (person instanceof Professor)
                studentGridPane.setDisable(true);
            else if (person instanceof Student)
                professorGridPane.setDisable(true);
        }
    }

    public void cancelClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) firstNameField.getScene().getWindow();
        currentStage.close();
    }

    private void showAlert(String title, String headerText, Alert.AlertType type) {
        Alert error = new Alert(type);
        error.setTitle(title);
        error.setHeaderText(headerText);
        error.show();
    }

    private void getPersonInfo(Person person) {
        person.setFirstName(firstNameField.getText());
        person.setLastName(lastNameField.getText());
        person.setJmbg(jmbgField.getText());
        person.setAddress(addressField.getText());
        person.setEmail(emailField.getText());
        Login login = new Login();
        login.setUsername(usernameField.getText());
        login.setPassword(passwordField.getText());
        login.setDateCreated(LocalDate.now());
        login.setUserType(userTypeChoiceBox.getValue());
        person.setLogin(login);
    }

    private void addPerson() throws SQLException {
        Person person;
        if (this.person instanceof Administrator)
            person = new Administrator();
        else if (this.person instanceof Professor)
            person = new Professor();
        else if (this.person instanceof Student)
            person = new Student();
        else
            return;
        getPersonInfo(person);
        if (person instanceof Administrator)
            dataBase.addAdministrator((Administrator) person);
        else if (person instanceof Professor) {
            ((Professor) person).setTitle(titleField.getText());
            dataBase.addProfessor((Professor) person);
        }
        else {
            ((Student) person).setBirthDate(birthDatePicker.getValue());
            ((Student) person).setSemester(semesterChoiceBox.getValue());
            ((Student) person).setCourse(courseChoiceBox.getValue());
            dataBase.addStudent((Student) person);
        }
        showAlert("Uspjeh", "Uspješno dodata osoba", Alert.AlertType.INFORMATION);
    }

    private void updatePerson() throws SQLException {
        getPersonInfo(person);
        if (person instanceof Administrator)
            dataBase.updateAdministrator((Administrator) person);
        else if (person instanceof Professor) {
            ((Professor) person).setTitle(titleField.getText());
            dataBase.updateProfessor((Professor) person);
        }
        else {
            ((Student) person).setBirthDate(birthDatePicker.getValue());
            ((Student) person).setSemester(semesterChoiceBox.getValue());
            ((Student) person).setCourse(courseChoiceBox.getValue());
            dataBase.updateStudent((Student) person);
        }
        showAlert("Uspjeh", "Uspješno ažurirana osoba", Alert.AlertType.INFORMATION);
    }

    public void okClick(ActionEvent actionEvent) {
        if (!validField[0] || !validField[1] || !validField[2] || !validField[3] || !validField[4] || !validField[5] || !validField[6] || !validField[7])
            return;
        if (this.person == null) {
            try {
                addPerson();
            } catch (SQLException error) {
                showAlert("Greška", "Problem pri dodavanju: " + error.getMessage(), Alert.AlertType.ERROR);
                return;
            }
        } else {
            try {
                updatePerson();
            } catch (SQLException error) {
                showAlert("Greška", "Problem pri ažuriranju: " + error.getMessage(), Alert.AlertType.ERROR);
                return;
            }
        }
        Stage currentStage = (Stage) firstNameField.getScene().getWindow();
        currentStage.close();
    }

}

package ba.unsa.etf.rpr.projekat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;

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
    public Label birthDateLabel;
    public Label semesterLabel;
    public Label courseLabel;
    public Label studentLabel;
    public Label professorLabel;
    public Label titleLabel;
    private BazaDAO dataBase;
    private Person person;
    private String userType;

    public AddPersonController(Person person, String userType) {
        dataBase = BazaDAO.getInstance();
        this.person = person;
        this.userType = userType;
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

    private void disableStudent() {
        studentLabel.setDisable(true);
        birthDateLabel.setDisable(true);
        birthDatePicker.setDisable(true);
        disableStudentUpdate();
    }

    private void disableProfessor() {
        professorLabel.setDisable(true);
        disableProfessorUpdate();
    }

    private void disableStudentUpdate() {
        semesterLabel.setDisable(true);
        semesterChoiceBox.setDisable(true);
        courseLabel.setDisable(true);
        courseChoiceBox.setDisable(true);
    }

    private void disableProfessorUpdate() {
        titleLabel.setDisable(true);
        titleField.setDisable(true);
    }

    @FXML
    public void initialize() {
        switch (userType) {
            case "Administrator":
                disableStudent();
                disableProfessor();
                break;
            case "Student":
                disableProfessor();
                if (person != null)
                    disableStudentUpdate();
                break;
            case "Profesor":
                disableStudent();
                if (person != null)
                    disableProfessorUpdate();
                break;
        }

        userTypeChoiceBox.getItems().add(userType);
        userTypeChoiceBox.setValue(userType);
        if (userType.equals("Student")) {
            Thread thread = new Thread(() -> {
                try {
                    var semesters = FXCollections.observableArrayList(dataBase.semesters());
                    var courses = FXCollections.observableArrayList(dataBase.courses());
                    Platform.runLater(() -> semesterChoiceBox.setItems(semesters));
                    Platform.runLater(() -> courseChoiceBox.setItems(courses));
                } catch (SQLException error) {
                    showAlert("Greška", "Problem sa bazom: " + error.getMessage(), Alert.AlertType.ERROR);
                }
            });
            thread.start();
        }


        if (person != null) {
            usernameField.setText(person.getLogin().getUsername());

            firstNameField.setText(person.getFirstName());
            lastNameField.setText(person.getLastName());
            jmbgField.setText(person.getJmbg());
            addressField.setText(person.getAddress());
            emailField.setText(person.getEmail());

            if (person instanceof Professor)
                titleField.setText(((Professor) person).getTitle());
            else if (person instanceof Student) {
                birthDatePicker.setValue(((Student) person).getBirthDate());
                semesterChoiceBox.setValue(((Student) person).getSemester());
                courseChoiceBox.setValue(((Student) person).getCourse());
            }
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
        login.setPassword(LoginController.getEncodedPassword(usernameField.getText(), passwordField.getText()));
        login.setDateCreated(LocalDate.now());
        login.setUserType(userTypeChoiceBox.getValue());
        person.setLogin(login);
    }

    private void addPerson() throws SQLException {
        Person person;
        switch (userType) {
            case "Administrator":
                person = new Administrator();
                break;
            case "Professor":
                person = new Professor();
                break;
            case "Student":
                person = new Student();
                break;
            default:
                return;
        }
        getPersonInfo(person);
        if (person instanceof Administrator)
            dataBase.addAdministrator((Administrator) person);
        else if (person instanceof Professor) {
            ((Professor) person).setTitle(titleField.getText());
            dataBase.addProfessor((Professor) person);
        } else {
            ((Student) person).setBirthDate(birthDatePicker.getValue());
            ((Student) person).setSemester(semesterChoiceBox.getValue());
            ((Student) person).setCourse(courseChoiceBox.getValue());
            dataBase.addStudent((Student) person);
        }
        showAlert("Uspjeh", "Uspješno dodata osoba", Alert.AlertType.INFORMATION);
    }

    private void updatePerson() throws SQLException {
        getPersonInfo(person);
        if (userType.equals("Administrator"))
            dataBase.updateAdministrator((Administrator) person);
        else if (userType.equals("Professor")) {
            ((Professor) person).setTitle(titleField.getText());
            dataBase.updateProfessor((Professor) person);
        } else {
            ((Student) person).setBirthDate(birthDatePicker.getValue());
            ((Student) person).setSemester(semesterChoiceBox.getValue());
            ((Student) person).setCourse(courseChoiceBox.getValue());
            dataBase.updateStudent((Student) person);
        }
        showAlert("Uspjeh", "Uspješno ažurirana osoba", Alert.AlertType.INFORMATION);
    }

    private String getJmbgString() {
        return jmbgField.getText();
    }

    private int getJmbgDay() {
        return Integer.parseInt(getJmbgString().substring(0, 2));
    }

    private int getJmbgMonth() {
        return Integer.parseInt(getJmbgString().substring(2, 4));
    }

    private int getJmbgYear() {
        int godina = Integer.parseInt(getJmbgString().substring(4, 7));
        if (godina >= 900)
            godina += 1000;
        else
            godina += 2000;
        return godina;
    }

    private boolean validJmbg() {
        if (getJmbgString().length() != 13 || !getJmbgString().matches("[0-9]+"))
            return false;
        int[] duzinaMjeseci = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int dan = getJmbgDay();
        int mjesec = getJmbgMonth();
        int godina = getJmbgYear();
        if (mjesec <= 0 || mjesec > 12)
            return false;
        if (godina % 400 == 0 || (godina % 100 != 0 && godina % 4 == 0))
            duzinaMjeseci[1] = 29;
        return ((dan > 0 && dan <= duzinaMjeseci[mjesec - 1]));
    }

    private String getEmailString() {
        return emailField.getText();
    }


    private boolean validEmail() {
        // Prepisano sa StackOverflow
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(getEmailString());
        return m.matches();
    }

    private boolean validBasicInfo() {
        if (firstNameField.getText().isEmpty() || !firstNameField.getText().matches("[a-zA-Z]+") || firstNameField.getText().length() < 2 || firstNameField.getText().length() > 50) {
            addColor(firstNameField, false);
            return false;
        }
        addColor(firstNameField, true);
        if (lastNameField.getText().isEmpty() || !lastNameField.getText().matches("[a-zA-Z]+") || lastNameField.getText().length() < 2 || lastNameField.getText().length() > 55) {
            addColor(lastNameField, false);
            return false;
        }
        addColor(lastNameField, true);
        if (!validJmbg()) {
            addColor(jmbgField, false);
            return false;
        }
        addColor(jmbgField, true);
        if (addressField.getText().isEmpty() || addressField.getText().length() < 2 || addressField.getText().length() > 60) {
            addColor(addressField, false);
            return false;
        }
        addColor(addressField, true);
        if (!validEmail()) {
            addColor(emailField, false);
            return false;
        }
        addColor(emailField, true);
        return true;
    }

    private boolean validLoginInfo() {
        if (usernameField.getText().isEmpty() || usernameField.getText().length() > 50 || usernameField.getText().length() < 3) {
            addColor(usernameField, false);
            return false;
        }
        addColor(usernameField, true);
        if (passwordField.getText().isEmpty() || passwordField.getText().length() > 50 || passwordField.getText().length() < 4) {
            addColor(passwordField, false);
            return false;
        }
        addColor(passwordField, true);
        return true;
    }

    private boolean validStudentInfo() {
        if (birthDatePicker.isDisabled())
            return true;
        LocalDate birthDateValue = birthDatePicker.getValue();
        if (birthDateValue == null || birthDateValue.isAfter(LocalDate.now()) || birthDateValue.getDayOfMonth() != getJmbgDay() || birthDateValue.getMonthValue() != getJmbgMonth() || birthDateValue.getYear() != getJmbgYear()) {
            addColor(birthDatePicker.getEditor(), false);
            return false;
        }
        addColor(birthDatePicker.getEditor(), true);
        return true;
    }

    private boolean validProfessorInfo() {
        if (titleField.isDisabled())
            return true;
        if (titleField.getText().isEmpty() || !titleField.getText().matches("[a-zA-Z]+") || titleField.getText().length() < 2 || titleField.getText().length() > 60) {
            addColor(titleField, false);
            return false;
        }
        addColor(titleField, true);
        return true;
    }

    public void okClick(ActionEvent actionEvent) {
        if (!validBasicInfo() || !validLoginInfo() || !validStudentInfo() || !validProfessorInfo())
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

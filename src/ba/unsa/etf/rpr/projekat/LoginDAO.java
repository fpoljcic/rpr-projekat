package ba.unsa.etf.rpr.projekat;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginDAO {
    private static LoginDAO instance = null;
    private PreparedStatement getLoginStmt;
    private Connection conn;

    public static LoginDAO getInstance() {
        if (instance == null)
            instance = new LoginDAO();
        return instance;
    }

    public static void removeInstance() {
        instance = null;
    }

    private LoginDAO() {
        String url = "jdbc:oracle:thin:@ora.db.lab.ri.etf.unsa.ba:1521:ETFLAB";
        String username = "FP18120";
        String password = "dsmLP88q";
        try {
            conn = DriverManager.getConnection(url, username, password);
            //Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa konektovanjem na bazu: " + error.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        try {
            getLoginStmt = conn.prepareStatement("SELECT * FROM FP18120.LOGIN WHERE USERNAME=? AND PASSWORD=? AND USER_TYPE=?");
        } catch (SQLException error) {
            showAlert("Greška", "Problem sa konektovanjem na bazu: " + error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String headerText, Alert.AlertType type) {
        Alert error = new Alert(type);
        error.setTitle(title);
        error.setHeaderText(headerText);
        error.show();
    }

    public Login getLogin(String username, String password, String user_type) {
        Login login = null;
        try {
            getLoginStmt.setString(1, username);
            getLoginStmt.setString(2, password);
            getLoginStmt.setString(3, user_type);
            var resultSet = getLoginStmt.executeQuery();
            while (resultSet.next()) {
                login = new Login();
                login.setId(resultSet.getInt(1));
                login.setUsername(resultSet.getString(2));
                login.setPassword(resultSet.getString(3));
                login.setDateCreated(resultSet.getDate(4).toLocalDate());
                login.setUserType(resultSet.getString(5));
            }
        } catch (SQLException ignored) {
            return null;
        }
        return login;
    }
}

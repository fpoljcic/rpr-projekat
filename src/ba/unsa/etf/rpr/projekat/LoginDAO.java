package ba.unsa.etf.rpr.projekat;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginDAO {
    private static LoginDAO instance = null;
    private PreparedStatement getIdStatement;
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
            getIdStatement = conn.prepareStatement("SELECT ID FROM FP18120.LOGIN WHERE USERNAME=? AND PASSWORD=? AND USER_TYPE=?");
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

    public Integer getUserId(String username, String password, String user_type) {
        Integer id = null;
        try {
            getIdStatement.setString(1, username);
            getIdStatement.setString(2, password);
            getIdStatement.setString(3, user_type);
            getIdStatement = conn.prepareStatement("SELECT ID FROM FP18120.LOGIN WHERE USERNAME='test' AND PASSWORD='test' AND USER_TYPE='Administrator'");
            var resultSet = getIdStatement.executeQuery();
            while (resultSet.next())
                id = resultSet.getInt(1);
        } catch (SQLException ignored) {
            return null;
        }
        return id;
    }
}

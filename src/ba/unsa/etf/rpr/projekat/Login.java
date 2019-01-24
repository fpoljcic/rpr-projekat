package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;

public class Login {
    private int id;
    private String username;
    private String password;
    private LocalDate dateCreated;
    private String userType;

    public Login(int id, String username, String password, LocalDate dateCreated, String userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.dateCreated = dateCreated;
        this.userType = userType;
    }

    public Login() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}

package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;
import java.util.Objects;

public class Login {
    private int id;
    private String username;
    private String password;
    private LocalDate dateCreated;
    private String userType;
    private LocalDate lastLoginDate;

    public Login(int id, String username, String password, LocalDate dateCreated, String userType, LocalDate lastLoginDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.dateCreated = dateCreated;
        this.userType = userType;
        this.lastLoginDate = lastLoginDate;
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

    public LocalDate getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDate lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Login login = (Login) o;
        return id == login.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

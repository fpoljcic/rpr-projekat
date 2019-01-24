package ba.unsa.etf.rpr.projekat;

public class Professor extends Person {
    private String title;

    public Professor(int id, String firstName, String lastName, String jmbg, String address, String email, Login login, String title) {
        super(id, firstName, lastName, jmbg, address, email, login);
        this.title = title;
    }

    public Professor() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

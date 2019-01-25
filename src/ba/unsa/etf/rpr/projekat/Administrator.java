package ba.unsa.etf.rpr.projekat;

public class Administrator extends Person {
    public Administrator(int id, String firstName, String lastName, String jmbg, String address, String email, Login login, int id1) {
        super(id, firstName, lastName, jmbg, address, email, login);
    }

    public Administrator() {
        super();
    }
}

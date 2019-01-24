package ba.unsa.etf.rpr.projekat;

public class Semester {
    private int id;
    private int no;
    private int cycleNo;
    private int ects;

    public Semester(int id, int no, int cycleNo, int ects) {
        this.id = id;
        this.no = no;
        this.cycleNo = cycleNo;
        this.ects = ects;
    }

    public Semester() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getCycleNo() {
        return cycleNo;
    }

    public void setCycleNo(int cycleNo) {
        this.cycleNo = cycleNo;
    }

    public int getEcts() {
        return ects;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }
}

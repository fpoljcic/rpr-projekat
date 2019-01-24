package ba.unsa.etf.rpr.projekat;

import java.util.Objects;

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

    @Override
    public String toString() {
        return no + ". semestar - " + cycleNo + ". ciklus studija";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Semester semester = (Semester) o;
        return id == semester.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

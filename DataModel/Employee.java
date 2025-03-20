package DataModel;

import java.io.Serializable;
import java.util.Objects;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idEmployee;
    private String name;
    private int workDuration = 0;

    public Employee(int idEmployee) {
        this.idEmployee = idEmployee;
    }

    public Employee(int idEmployee, String name) {
        this(idEmployee);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }

    public int getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(int workDuration) {
        this.workDuration = workDuration;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Employee) {
            return this.idEmployee == ((Employee) obj).idEmployee;
        } else return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmployee);
    }

    @Override
    public String toString() {
        return "idEmployee=" + idEmployee + ", name='" + name + ", work duration=" + getWorkDuration() + '\'';
    }
}

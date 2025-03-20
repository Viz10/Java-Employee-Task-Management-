package DataModel;

import java.io.Serializable;

public sealed abstract class Task implements Serializable permits SimpleTask, ComplexTask {

    private static final long serialVersionUID = 1L;
    private int idTask;
    private String statusTask;
    private int employeeId = -1;

    public Task(int idTask, String statusTask) {
        this.idTask = idTask;
        this.statusTask = statusTask;
    }

    public String changeStatus() {
        if (statusTask.equals("Completed")) {
            statusTask = "Uncompleted";
        } else {
            statusTask = "Completed";
        }
        return statusTask;
    }

    public String getStatusTask() {
        return statusTask;
    }

    public int getIdTask() {
        return idTask;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Task) {
            return this.idTask == ((Task) obj).idTask;
        } else return false;
    }

    public abstract int estimateDuration();
    public abstract int estimateDuration(String status);
}

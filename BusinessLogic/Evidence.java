package BusinessLogic;


import java.io.Serializable;

public class Evidence implements Serializable {
    private int taskCnt = 0;
    private int employeeCnt = 0;

    public Evidence(Evidence emp) {
        this.copy(emp);
    }
    public Evidence(int taskCnt, int employeeCnt) {
        this.taskCnt = taskCnt;
        this.employeeCnt = employeeCnt;
    }

    public int getTaskCnt() {
        return taskCnt;
    }
    public void setTaskCnt(int taskCnt) {
        this.taskCnt = taskCnt;
    }
    public int getEmployeeCnt() {
        return employeeCnt;
    }
    public void setEmployeeCnt(int employeeCnt) {
        this.employeeCnt = employeeCnt;
    }

    public void copy(Evidence evidence) {
        taskCnt = evidence.getTaskCnt();
        employeeCnt = evidence.getEmployeeCnt();
    }

}

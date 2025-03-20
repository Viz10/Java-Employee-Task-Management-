package DataModel;

import java.io.Serializable;

non-sealed public class SimpleTask extends Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private int startHour = 0;
    private int endHour = 0;

    public SimpleTask(int idTask, String statusTask, int startHour, int endHour) {
        super(idTask, statusTask);
        this.endHour = endHour;
        this.startHour = startHour;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    @Override
    public int estimateDuration() {
        return endHour - startHour;
    }
    @Override
    public int estimateDuration(String status){
        if(status.equals("Completed")){
            return endHour - startHour;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Simple task: idTask= " + getIdTask() + ", statusTask= '" + getStatusTask() +", est. work duration: "+estimateDuration()+" hours\n";
    }
}

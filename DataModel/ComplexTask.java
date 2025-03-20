package DataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

non-sealed public class ComplexTask extends Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Task> complex_list;

    public ComplexTask(int idTask,String statusTask){
        super(idTask,statusTask);
        complex_list=new ArrayList<>();
    }

    public List<Task> getComplex_list() {
        return complex_list;
    }

    public void addTask(Task task){
        if(task==null){
            throw new NullPointerException();
        }
        if(complex_list.contains(task)){
            throw new InputMismatchException("Task already exists");
        }
        if(task instanceof ComplexTask){
            complex_list.add(task);
        }
        else {
            complex_list.addFirst(task);
        }
    }

    @Override
    public int estimateDuration(){
        int sum=0;
        for(Task t:complex_list) sum+=t.estimateDuration();
        return sum;
    }

    @Override
    public int estimateDuration(String status){ /// method to compute estimate work duration only for completed tasks
        int sum=0;
        for(Task t:complex_list){
            sum += t.estimateDuration(t.getStatusTask());
        }
        return sum;
    }

    @Override
    public String toString() {
        return "Complex task: idTask= " + getIdTask() + ", statusTask= '" + getStatusTask() +", est. work duration: "+estimateDuration()+" hours\n";
    }
}

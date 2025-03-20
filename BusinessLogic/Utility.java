package BusinessLogic;
import DataModel.ComplexTask;
import DataModel.Employee;
import DataModel.SimpleTask;
import DataModel.Task;

import java.util.*;

public class Utility {

    TasksManagement tasksManagement;
    TreeSet<Employee> orderedEmployees;

    public Utility(TasksManagement tasksManagement){
        this.tasksManagement=tasksManagement;
        orderedEmployees=new TreeSet<>(new MyComparator());
    }

    public StringBuilder sortEmployees(){
        addToSet();
        return printOrderedEmployees();
    }
    private void addToSet(){
        Map<Employee, List<Task>> map=tasksManagement.getMap();
        for(Employee employee:map.keySet()){
            int val=tasksManagement.calculateEmployeeWorkDuration(employee.getIdEmployee());
            employee.setWorkDuration(val);
            if(val>40){
                orderedEmployees.add(employee);
            }
        }
    }
    private StringBuilder printOrderedEmployees(){

        StringBuilder builder=new StringBuilder();
        if(orderedEmployees.isEmpty()){
            return builder; /// return an empty result , will check in GUI
        }
        for(Employee e:orderedEmployees)
            builder.append(e.getName()).append(", ").append(e.getWorkDuration()).append("\n");

        return builder;
    }

    public StringBuilder viewEmployeesTaskStatus(){
        Map<String,Map<String,Integer>> rez=viewEmployeesTaskStatusWrapper();
        StringBuilder str=new StringBuilder();
        for(Map.Entry<String,Map<String,Integer>> entry:rez.entrySet()){
            str.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
        }
        return str;
    }
    private Map<String,Map<String,Integer>> viewEmployeesTaskStatusWrapper(){
        Map<String,Map<String,Integer>> rez = new HashMap<>();

        for(Employee e: tasksManagement.getMap().keySet()){
            employeeTaskStatus(e,rez);
        }

        return rez;
    }
    private void employeeTaskStatus(Employee e,Map<String,Map<String,Integer>> rez){
        Map<String,Integer> map= new HashMap<>();
        Integer[]results={0,0};

        employeeTaskStatusHelper(tasksManagement.getMap().get(e),results);

        map.put("Completed",results[0]);
        map.put("Uncompleted",results[1]);

        rez.put(e.getName(),map);
    }
    private void employeeTaskStatusHelper(List<Task> list,Integer[]results){
        if (list == null || list.isEmpty())
            return;

        for (Task task : list) {
            if(task.getStatusTask().equals("Completed")){
                results[0]++;
            }
            else{
                results[1]++;
            }
            if (task instanceof ComplexTask) {
                List<Task> subtasks = ((ComplexTask) task).getComplex_list();
                if (subtasks != null && !subtasks.isEmpty()) {
                    employeeTaskStatusHelper(subtasks,results);
                }
            }
        }
    }

    public void checkHours(String startTime, String endTime){
        if(startTime==null || endTime==null || startTime.isEmpty() || endTime.isEmpty()){
            throw new IllegalArgumentException("Hour cannot be null or empty");
        }
        if(!startTime.matches("\\d{1,2}") || !endTime.matches("\\d{1,2}")){
            throw new IllegalArgumentException("Hours must between 0 and 24");
        }

        int start,end;
        start=Integer.parseInt(startTime);
        end=Integer.parseInt(endTime);

        if(start<0 || start>24 || end<0 || end>24){
            throw new IllegalArgumentException("Hours must between 0 and 24");
        }

        if(start>end){
            throw new IllegalArgumentException("End hour must come after start");
        }
    }
    public void checkId(String id){
        if(id==null || id.isEmpty()){
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if(!id.matches("[0-9]+")){
            throw new IllegalArgumentException("ID must be a number");
        }
    }
    public void checkName(String name){
        if(name==null || name.isEmpty()){
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if(!name.matches("[a-zA-Z]*")){
            throw new IllegalArgumentException("Name must contain only letters");
        }
    }

    public Map<String,List<Task>> listEmployees(){

       Map<String,List<Task>> rez=new HashMap<>();
       for(Employee e:tasksManagement.getMap().keySet()){
            rez.put(e.getName(),tasksManagement.getMap().get(e));
       }

       return rez;
    }
    public List<Task> listVacantTasks(){

        List<Task> rez=new ArrayList<>();
        for(Task task:tasksManagement.getTasks_list()){
                if(task.getEmployeeId()==-1){
                    if(task instanceof SimpleTask)
                        rez.addFirst(task);
                    else
                        rez.add(task);
                }
        }
        return rez;

    }
}

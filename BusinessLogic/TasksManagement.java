package BusinessLogic;

import java.io.Serializable;
import java.util.*;
import java.util.Map;

import DataAcces.Serialization;
import DataModel.*;

public class TasksManagement implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<Employee, List<Task>> map;
    private List<Task> tasks_list;
    private Evidence evidence;

    public TasksManagement() {
        tasks_list = new ArrayList<>();
        map = new HashMap<>();
        evidence=new Evidence(0,0);
    }

    /// 3 ways of adding tasks:
    /// 1. to the total task list
    /// 2. from the task list , handled one add to certain employee
    /// 3. add to complex task directly

    public void addEmployee(Employee employee) {
        if (employee == null) {
           throw new NullPointerException("Error adding null employee");
        }
        for(Employee e : map.keySet()) { /// using .contains() will match it by id not name
            if (e.getName().equals(employee.getName())) {
                throw new MyException("Employee already exists!");
            }
        }
        map.put(employee, new ArrayList<>());

    }
    public void addToTasksList(Task task) {
        if (task == null) {
            throw new NullPointerException("Error adding null task!");
        }
        if(tasks_list.contains(task)) { /// based on id that is
            throw new InputMismatchException("Task already present!");
        }
        tasks_list.add(task);
    }

    public ComplexTask returnParentComplexWrapper(int idTask, List<Task> list){
        for(Task task:tasks_list) {
            if(task.getEmployeeId()==-1 && task instanceof ComplexTask) {
                /// continue searching the depth of the complex task to find if the task to be assigned is the root or not
                ComplexTask result=  returnParentComplex( idTask, (ComplexTask) task,((ComplexTask) task).getComplex_list());
                if(result!=null) {
                    return result;
                }
            }
        }
        return null;
    }
    private ComplexTask returnParentComplex(int idTask, ComplexTask lastComplex, List<Task> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        for (Task task : list) {
            if (task.getIdTask() == idTask) {
                return lastComplex;
            } else if (task instanceof ComplexTask) {
                lastComplex = (ComplexTask) task; /// save last visited complex task through recursion
                ComplexTask rez = returnParentComplex(idTask, lastComplex, ((ComplexTask) task).getComplex_list());
                if (rez != null) {
                    return rez;
                }
            }
        }
        return null; /// not found in the tree
    }

    /**
     * at least 2 possible methods to assign , given the parameters
     * 1.go through the entity/key set , find employee by id and assign task
     * 2.as Employee class is small and has one implicit id constructor , create a new instance and search it
     *
     * @param idEmployee
     * @param task
     */
    public void assignTaskToEmployee(int idEmployee, Task task) {
        if(task==null){
            throw new NullPointerException("Task does not exist!");
        }
        Employee aux = new Employee(idEmployee);
        if (!map.containsKey(aux)) {
            throw new MyException("employee doesnt exist!");
        }

        if (task.getEmployeeId() != -1) {
            throw new MyException("Task already assigned to someone else!");
        }

        ComplexTask parent=returnParentComplexWrapper(task.getIdTask(),tasks_list);

        if(parent!=null){
            throw new MyException("Task is belonging to: \n"+ parent.toString()+
                    "\ncannot add directly it!");
        }

        task.setEmployeeId(idEmployee);
        /// if complex ,also need to be sure its depth are also set to this employee , could be a tree freshly assigned from list to employee
        if (task instanceof ComplexTask) {
            setIDs(((ComplexTask) task).getComplex_list(), idEmployee);
            map.get(aux).add(task);
        }
        else if(task instanceof SimpleTask){
            map.get(aux).addFirst(task);
        }
        }
    public void assignTaskToComplexTask(int complexId, Task task) {

        /// only vacant tasks can be assigned to vacant complex tasks

        if(task==null){
            throw new NullPointerException("Task does not exist!");
        }

        Task complexTask=searchAndReturn(complexId,tasks_list);

        if(complexTask==null){
            throw new MyException("Complex task doesnt exist!");
        }
        if(complexTask instanceof SimpleTask) {
            throw new RuntimeException("cannot add to simple task");
        }
        if(task.getEmployeeId()!=-1){
            throw new MyException("Task is already assigned to someone else!");
        }

        ComplexTask parent=returnParentComplexWrapper(task.getIdTask(),tasks_list);

        if(parent!=null){
            throw new MyException("Task is belonging to: \n"+ parent.toString()+
                    "\ncannot add directly it!");
        }

        if(task instanceof ComplexTask){

            Task aux=searchAndReturn(complexId,((ComplexTask) task).getComplex_list());

            if(aux!=null && aux.equals(complexTask)){
                throw new MyException("Cannot assign tasks to form dependencies!");
            }

            setIDs(((ComplexTask) task).getComplex_list(), complexTask.getEmployeeId());
        }

        task.setEmployeeId(complexTask.getEmployeeId());
        ((ComplexTask) complexTask).addTask(task);
    }

    public void setIDs(List<Task> list,int id) {
        if (list == null || list.isEmpty())
            return;
        for (Task task : list) {
            task.setEmployeeId(id);
            if (task instanceof ComplexTask) {
                List<Task> subtasks = ((ComplexTask) task).getComplex_list();
                if (subtasks != null && !subtasks.isEmpty()) {
                    setIDs(subtasks,id);
                }
            }
        }
    }

    public void modifyTaskStatus(int idEmployee, int idTask) {
        Employee aux = new Employee(idEmployee);
        if (!map.containsKey(aux)) {
           throw new MyException("employee not found!");
        }
        if (map.get(aux).isEmpty()) {
            throw new MyException("employee has no tasks!");
        }
        Task t = searchAndReturn(idTask, map.get(aux)); /// search task from employee list of tasks
        if (t == null) {
            throw new MyException("employee doesnt have this task assigned!");
        }else{
            t.changeStatus();
        }

    }
    public Task searchAndReturn(int taskID, List<Task> list) {
        if (list == null || list.isEmpty())
            return null;

        for (Task task : list) {
            if (task.getIdTask() == taskID) {
                return task;
            } else if (task instanceof ComplexTask) {
                List<Task> subtasks = ((ComplexTask) task).getComplex_list();
                Task rez = searchAndReturn(taskID, subtasks);
                if (rez != null)
                    return rez;
                /// if null , continue searching in the list , not found in depth
            }
        }
        return null;
    }
    public int calculateEmployeeWorkDuration(int idEmployee) {
        Employee aux = new Employee(idEmployee);
        if (map.containsKey(aux)) {
            if (map.get(aux).isEmpty()) {
                return -1;
            }
            int sum = 0;
            for (Task task : map.get(aux)) {
                if(task instanceof SimpleTask){
                    sum+=task.estimateDuration(task.getStatusTask());
                }
                else if(task instanceof ComplexTask){
                    sum+=task.estimateDuration(""); /// it will check its depth
                }
            }
            return sum;
        }
        return -1;
    }

    public Task returnTask(int id){
        for(Task t:tasks_list){
            if(t.getIdTask()==id)
                return t;
        }
        return null;
    }
    public int returnId(String name){
        for(Employee e:map.keySet()){
            if(e.getName().equals(name))
                return e.getIdEmployee();
        }
        return -1;
    }

    public List<Task> getTasks_list() {
        return tasks_list;
    }
    public Map<Employee, List<Task>> getMap() {
        return map;
    }
    public Evidence getEvidence() {
        return evidence;
    }
    public void setEvidence(Evidence evidence) {
        this.evidence = evidence;
    }
    public void setMap(Map<Employee, List<Task>> map) {
        this.map = map;
    }
    public void setTasks_list(List<Task> tasks_list) {
        this.tasks_list = tasks_list;
    }

    public void saveData(){
        Serialization.write(this);
        Serialization.writeIdClass(this);
    }
    public void loadData(){
        Serialization.read(this);
        Serialization.readIdClass(this);
    }
}

package DataAcces;

import BusinessLogic.MyException;
import BusinessLogic.TasksManagement;
import BusinessLogic.Evidence;
import DataModel.Employee;
import DataModel.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Serialization implements Serializable {

    static public void write(TasksManagement tasksManagement) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.out"));
            oos.writeObject(tasksManagement.getMap());
            oos.writeObject(tasksManagement.getTasks_list());
        } catch (IOException e) {
            throw new MyException("Error saving to the file");
        }
    }
    static public void read(TasksManagement tasksManagement) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.out"));
            tasksManagement.setMap((Map<Employee, List<Task>>) ois.readObject());
            tasksManagement.setTasks_list((List<Task>) ois.readObject());
        } catch (FileNotFoundException e) {
            tasksManagement.setMap(new HashMap<>());
            tasksManagement.setTasks_list(new ArrayList<>());
            write(tasksManagement);
        } catch (InvalidClassException e) {
            tasksManagement.setMap(new HashMap<>());
            tasksManagement.setTasks_list(new ArrayList<>());
            write(tasksManagement);
        } catch (EOFException | StreamCorruptedException e) {
            tasksManagement.setMap(new HashMap<>());
            tasksManagement.setTasks_list(new ArrayList<>());
            write(tasksManagement);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static public void writeIdClass(TasksManagement tasksManagement) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("ids.data"))) {
            oos.writeObject(tasksManagement.getEvidence());
        } catch (IOException e) {
            throw new MyException("Error saving to the file");
        }
    }
    static public void readIdClass(TasksManagement tasksManagement) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("ids.data"))) {
           tasksManagement.setEvidence((Evidence) ois.readObject());
        } catch (FileNotFoundException | InvalidClassException | EOFException | StreamCorruptedException e) {
            tasksManagement.setEvidence(new Evidence(0,0));
            writeIdClass(tasksManagement);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}


import BusinessLogic.TasksManagement;
import GUI.StartPage;

public class Main {
    public static void main(String[] args) {
        new StartPage(new TasksManagement());
    }
}

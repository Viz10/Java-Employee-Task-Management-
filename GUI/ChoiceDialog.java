package GUI;

import BusinessLogic.Evidence;
import BusinessLogic.TasksManagement;
import BusinessLogic.Utility;
import DataModel.Employee;
import DataModel.SimpleTask;
import DataModel.Task;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ChoiceDialog extends JDialog {

    private Map<String, Runnable> map;
    TasksManagement tm;
    Utility utility;
    Evidence evid;

    public ChoiceDialog(JFrame parent, String title, String choice, TasksManagement tasksManagement, Utility utility, Evidence evidence) {
        super(parent, title, true);
        this.map = new HashMap<>();
        this.tm = tasksManagement;
        this.evid = evidence;
        this.utility = utility;
        setUpMap();

        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.setSize(500, 500);
        this.setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Runnable action = map.get(choice);
        if (action != null) {
            action.run();
        }

        this.setLocationRelativeTo(null);
    }

    void setUpMap() {
        map.put("Add Employee", this::addEmployee);
        map.put("Add Simple Task", this::addSimpleTask);
        map.put("Add Task To Employee", this::addTaskToEmployee);
        map.put("Add Task To Complex Task", this::addTaskToComplexTask);
        map.put("Modify Status", this::modifyStatus);
        map.put("Filter 40h+", this::utilityFilter);
        map.put("Task situation", this::utilityTaskStatus);
    }

    private void addEmployee() {
        JLabel title = new JLabel("Add employee name");
        JTextField name = new JTextField();
        JButton addEmployeeb = new JButton("Add Employee");

        name.setMaximumSize(new Dimension(200, 30));
        Font labelFont = new Font(null, Font.PLAIN, 16);
        title.setFont(labelFont);

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        addEmployeeb.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(title);
        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(name);
        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(addEmployeeb);

        addEmployeeb.setPreferredSize(new Dimension(50, 50));
        addEmployeeb.setFocusable(false);

        addEmployeeb.addActionListener(e -> {
            String nameEmp = name.getText().trim();
            if (!tryName(nameEmp)) {
                name.setText("");
                return;
            }
            int employeeCnt = evid.getEmployeeCnt();
            try {
                tm.addEmployee(new Employee(employeeCnt, nameEmp));
            } catch (Exception e2) {
                JOptionPane.showMessageDialog(null, e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                name.setText("");
                return;
            }
            JOptionPane.showMessageDialog(null, "Employee " + nameEmp + " id:" + employeeCnt + " added!");
            evid.setEmployeeCnt(++employeeCnt);
            this.dispose();
        });
    }

    private void addSimpleTask() {
        JLabel title1 = new JLabel("Enter start hour:");
        JLabel title2 = new JLabel("Enter end hour :");
        JTextField startHour = new JTextField();
        JTextField endHour = new JTextField();
        JButton addTask = new JButton("Add simple task");

        startHour.setMaximumSize(new Dimension(200, 30));
        endHour.setMaximumSize(new Dimension(200, 30));

        Font labelFont = new Font(null, Font.PLAIN, 16);
        title1.setFont(labelFont);
        title2.setFont(labelFont);

        title1.setAlignmentX(Component.CENTER_ALIGNMENT);
        title2.setAlignmentX(Component.CENTER_ALIGNMENT);
        addTask.setAlignmentX(Component.CENTER_ALIGNMENT);
        startHour.setAlignmentX(Component.CENTER_ALIGNMENT);
        endHour.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(title1);
        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(startHour);
        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(title2);
        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(endHour);
        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(addTask);

        addTask.setPreferredSize(new Dimension(50, 50));
        addTask.setFocusable(false);

        addTask.addActionListener(e -> {

            String start = startHour.getText().trim();
            String end = endHour.getText().trim();

            if (!tryHours(start, end)) {
                startHour.setText("");
                endHour.setText("");
                return;
            }
            int taskCnt = evid.getTaskCnt();

            try {
                tm.addToTasksList(new SimpleTask(taskCnt, "Uncompleted", Integer.parseInt(start), Integer.parseInt(end)));
            } catch (Exception error) {
                JOptionPane.showMessageDialog(null, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                startHour.setText("");
                endHour.setText("");
                return;
            }
            JOptionPane.showMessageDialog(null, "Simple task with id: " + taskCnt + " added!");
            evid.setTaskCnt(++taskCnt);
            this.dispose();
        });
    }

    private void addTaskToEmployee() {
        JLabel title1 = new JLabel("Employee name: ");
        JLabel title2 = new JLabel("Id of task ");
        JTextField name = new JTextField();
        JTextField id = new JTextField();
        JButton addTask = new JButton("Add Task");

        name.setMaximumSize(new Dimension(200, 30));
        id.setMaximumSize(new Dimension(200, 30));
        Font labelFont = new Font(null, Font.PLAIN, 16);
        title1.setFont(labelFont);
        title2.setFont(labelFont);

        title1.setAlignmentX(Component.CENTER_ALIGNMENT);
        title2.setAlignmentX(Component.CENTER_ALIGNMENT);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        addTask.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(title1);
        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(name);
        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(title2);
        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(id);
        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(addTask);

        addTask.setPreferredSize(new Dimension(50, 50));
        addTask.setFocusable(false);

        addTask.addActionListener(e -> {

            String nameEmp = name.getText().trim();
            String idTask = id.getText().trim();

            if (!tryName(nameEmp) || !tryId(idTask)) {
                name.setText("");
                id.setText("");
                return;
            }

            Task t = tm.returnTask(Integer.parseInt(idTask));
            int idEmp = tm.returnId(nameEmp);

            try {
                tm.assignTaskToEmployee(idEmp, t);
            } catch (Exception error) {
                JOptionPane.showMessageDialog(null, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                name.setText("");
                id.setText("");
                return;
            }
            JOptionPane.showMessageDialog(null, "Task with id: " + idTask + " added to " + nameEmp + " !");
            this.dispose();
        });
    }

    private void addTaskToComplexTask() {

        JLabel title1 = new JLabel("Enter complex id:");
        JLabel title2 = new JLabel("Enter task id :");
        JTextField complexId = new JTextField();
        JTextField taskId = new JTextField();
        JButton addTask = new JButton("Add task");

        complexId.setMaximumSize(new Dimension(200, 30));
        taskId.setMaximumSize(new Dimension(200, 30));

        Font labelFont = new Font(null, Font.PLAIN, 16);
        title1.setFont(labelFont);
        title2.setFont(labelFont);

        title1.setAlignmentX(Component.CENTER_ALIGNMENT);
        title2.setAlignmentX(Component.CENTER_ALIGNMENT);
        addTask.setAlignmentX(Component.CENTER_ALIGNMENT);
        complexId.setAlignmentX(Component.CENTER_ALIGNMENT);
        taskId.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(title1);
        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(complexId);
        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(title2);
        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(taskId);
        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(addTask);

        addTask.setPreferredSize(new Dimension(50, 50));
        addTask.setFocusable(false);
        addTask.addActionListener(e -> {

            String firstId = complexId.getText().trim();
            String secondId = taskId.getText().trim();

            if (!tryId(firstId) || !tryId(secondId)) {
                complexId.setText("");
                taskId.setText("");
                return;
            }

            if (firstId.equals(secondId)) {
                JOptionPane.showMessageDialog(null, "Cannot have self assigment!", "Error", JOptionPane.ERROR_MESSAGE);
                complexId.setText("");
                taskId.setText("");
                return;
            }

            int idcomplex = Integer.parseInt(firstId);
            int idtask = Integer.parseInt(secondId);
            Task task = tm.returnTask(idtask);

            try {
                tm.assignTaskToComplexTask(idcomplex, task);
            } catch (Exception error) {
                JOptionPane.showMessageDialog(null, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                complexId.setText("");
                taskId.setText("");
                return;
            }
            JOptionPane.showMessageDialog(null, "Task with id:" + idtask + " added to " + firstId + " !");
            this.dispose();
        });

    }

    private void modifyStatus() {
        JLabel title1 = new JLabel("Employee id: ");
        JLabel title2 = new JLabel("Id of task ");
        JTextField employeeId = new JTextField();
        JTextField id = new JTextField();
        JButton addTask = new JButton("Modify Task Status");

        employeeId.setMaximumSize(new Dimension(200, 30));
        id.setMaximumSize(new Dimension(200, 30));
        Font labelFont = new Font(null, Font.PLAIN, 16);
        title1.setFont(labelFont);
        title2.setFont(labelFont);

        title1.setAlignmentX(Component.CENTER_ALIGNMENT);
        title2.setAlignmentX(Component.CENTER_ALIGNMENT);
        employeeId.setAlignmentX(Component.CENTER_ALIGNMENT);
        addTask.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(title1);
        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(employeeId);
        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(title2);
        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(id);
        add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(addTask);

        addTask.setPreferredSize(new Dimension(50, 50));
        addTask.setFocusable(false);

        addTask.addActionListener(e -> {

            String idEmpl = employeeId.getText().trim();
            String idTask = id.getText().trim();

            if (!tryId(idEmpl) || !tryId(idTask)) {
                employeeId.setText("");
                id.setText("");
                return;
            }
            Task task = tm.returnTask(Integer.parseInt(idTask));
            int idEmp = Integer.parseInt(idEmpl);
            String result;
            try {
                tm.modifyTaskStatus(idEmp, Integer.parseInt(idTask));
                result = task.getStatusTask();
            } catch (Exception error) {
                JOptionPane.showMessageDialog(null, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                employeeId.setText("");
                id.setText("");
                return;
            }
            JOptionPane.showMessageDialog(null, "Changed task : " + idTask + " to " + result + " for employee with id: " + idEmpl + "!");
            this.dispose();
        });
    }

    private void utilityFilter() {

        JTextArea dataTextArea = new JTextArea();
        dataTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        dataTextArea.setEditable(false);
        dataTextArea.setLineWrap(true);
        dataTextArea.setWrapStyleWord(true);
        dataTextArea.setFocusable(false);
        StringBuilder dataBuilder;
        dataBuilder = utility.sortEmployees();

        if(dataBuilder.isEmpty()){
            dataBuilder.append("No employees with more than 40h of work found!").append("\n");
        }

        dataTextArea.setText(dataBuilder.toString());
        JScrollPane scrollPane = new JScrollPane(dataTextArea);
        this.add(scrollPane);

    }

    private void utilityTaskStatus() {

        JTextArea dataTextArea = new JTextArea();
        dataTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        dataTextArea.setEditable(false);
        dataTextArea.setLineWrap(true);
        dataTextArea.setWrapStyleWord(true);
        dataTextArea.setFocusable(false);

        StringBuilder dataBuilder;
        dataBuilder = utility.viewEmployeesTaskStatus();

        if(dataBuilder.isEmpty()){
            dataBuilder.append("No employees found!").append("\n");
        }

        dataTextArea.setText(dataBuilder.toString());
        JScrollPane scrollPane = new JScrollPane(dataTextArea);
        this.add(scrollPane);
    }

    private boolean tryHours(String startHour, String endHour) {

        try {
            utility.checkHours(startHour, endHour);
        } catch (Exception error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean tryId(String id) {
        try {
            utility.checkId(id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean tryName(String name) {
        try {
            utility.checkName(name);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}

package GUI;

import BusinessLogic.Evidence;
import BusinessLogic.TasksManagement;
import BusinessLogic.Utility;
import DataModel.ComplexTask;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartPage extends JFrame implements ActionListener {

    private TasksManagement tasksManagement;
    private Utility utility;

    JPanel up = new JPanel();
    JPanel left = new JPanel();
    JPanel right = new JPanel();
    JPanel left_down = new JPanel();
    JPanel left_up = new JPanel();
    JPanel left_centre = new JPanel();

    ViewDataDialog viewVacantTask;
    ViewDataDialog viewAllTask;

    JButton addEmployeeB = new JButton("Add Employee"); // done
    JButton addSimpleTaskB = new JButton("Add Simple Task"); //done
    JButton assignToEmployeeB = new JButton("Add Task To Employee"); //done
    JButton viewEmployeeB = new JButton("View Employees");
    JButton modifyStatusB = new JButton("Modify Status");
    JButton addComplexTaskB = new JButton("Add Complex Task"); //done
    JButton addToComplexB = new JButton("Add Task To Complex Task");
    JButton viewVacants = new JButton("View unassigned tasks");
    JButton employeesStatus1 = new JButton("Filter 40h+");
    JButton employeesStatus2 = new JButton("Task situation");

    JLabel welcomeLabel = new JLabel("Welcome admin");

    public StartPage(TasksManagement tasksManagement) {
        this.tasksManagement = tasksManagement;
        this.utility= new Utility(tasksManagement);
        tasksManagement.loadData();
        loadPage();
    }

    private void loadPage() {

        this.setLayout(new BorderLayout(10, 10));
        this.setSize(1920, 1080);

        addEmployeeB.addActionListener(this);
        addToComplexB.addActionListener(this);
        viewVacants.addActionListener(this);
        addSimpleTaskB.addActionListener(this);
        viewEmployeeB.addActionListener(this);
        assignToEmployeeB.addActionListener(this);
        modifyStatusB.addActionListener(this);
        addComplexTaskB.addActionListener(this);
        employeesStatus1.addActionListener(this);
        employeesStatus2.addActionListener(this);

        addEmployeeB.setFocusable(false);
        addSimpleTaskB.setFocusable(false);
        viewVacants.setFocusable(false);
        addSimpleTaskB.setFocusable(false);
        viewEmployeeB.setFocusable(false);
        assignToEmployeeB.setFocusable(false);
        modifyStatusB.setFocusable(false);
        addComplexTaskB.setFocusable(false);
        addToComplexB.setFocusable(false);
        employeesStatus1.setFocusable(false);
        employeesStatus2.setFocusable(false);

        addEmployeeB.setPreferredSize(new Dimension(200, 150));
        addSimpleTaskB.setPreferredSize(new Dimension(200, 150));
        viewVacants.setPreferredSize(new Dimension(200, 150));
        addSimpleTaskB.setPreferredSize(new Dimension(200, 150));
        viewEmployeeB.setPreferredSize(new Dimension(200, 150));
        assignToEmployeeB.setPreferredSize(new Dimension(200, 150));
        modifyStatusB.setPreferredSize(new Dimension(200, 150));
        addComplexTaskB.setPreferredSize(new Dimension(200, 150));
        addToComplexB.setPreferredSize(new Dimension(200, 150));
        employeesStatus1.setPreferredSize(new Dimension(200, 150));
        employeesStatus2.setPreferredSize(new Dimension(200, 150));

        up.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 35));
        up.setBackground(Color.pink);
        right.setBackground(Color.gray);

        left_up.setBackground(new Color(102, 255, 102));
        left_down.setBackground(new Color(204, 153, 255));
        left_centre.setBackground(new Color(117, 117, 72));

        left.setLayout(new BorderLayout());
        right.setLayout(new BorderLayout());

        up.setPreferredSize(new Dimension(200, 100));
        right.setPreferredSize(new Dimension(1000, 50));
        left.setPreferredSize(new Dimension(900, 50));

        left_up.setPreferredSize(new Dimension(1000, 300));
        left_centre.setPreferredSize(new Dimension(1000, 400));
        left_down.setPreferredSize(new Dimension(1000, 300));

        left_up.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 70));
        left_down.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 80));
        left_centre.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 100));

        welcomeLabel.setBackground(Color.black);
        welcomeLabel.setFont(new Font(null, Font.BOLD, 20));
        up.add(welcomeLabel);

        left_up.add(addSimpleTaskB);
        left_up.add(addComplexTaskB);
        left_up.add(addToComplexB);
        left_up.add(assignToEmployeeB);

        left_centre.add(viewVacants);
        left_centre.add(viewVacants);
        left_centre.add(viewEmployeeB);
        left_centre.add(modifyStatusB);

        left_down.add(employeesStatus1);
        left_down.add(employeesStatus2);
        left_down.add(addEmployeeB);

        left.add(left_up,BorderLayout.NORTH);
        left.add(left_centre,BorderLayout.CENTER);
        left.add(left_down,BorderLayout.SOUTH);

        this.add(up, BorderLayout.NORTH);
        this.add(left, BorderLayout.WEST);
        this.add(right, BorderLayout.EAST);

        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                tasksManagement.saveData();
            }
        });
    }

    private void addComplexTask() {
        Evidence evidence=tasksManagement.getEvidence();
        int taskCnt=evidence.getTaskCnt();
        try {
            tasksManagement.addToTasksList(new ComplexTask(taskCnt,"Uncompleted"));
        }catch (Exception error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Complex Task with id= "+taskCnt+" added!");
        evidence.setTaskCnt(++taskCnt);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (e.getSource() == addComplexTaskB) {
            addComplexTask();
        }
        else if(e.getSource() == viewVacants) {

            if(viewVacantTask==null || !viewVacantTask.isVisible()) {
                viewVacantTask = new ViewDataDialog(this, "View unassigned tasks",right, tasksManagement,utility);
                viewVacantTask.setVisible(true);
                viewVacantTask.setFocusable(true);
            }
        }
        else if(e.getSource()==viewEmployeeB) {

            if(viewAllTask==null || !viewAllTask.isVisible()) {
                viewAllTask= new  ViewDataDialog(this,"View Employees",right,tasksManagement,utility);
                viewAllTask.setVisible(true);
            }
        }
        else{
            ChoiceDialog choiceDialog= new ChoiceDialog(this,"Operations",command,tasksManagement,utility,tasksManagement.getEvidence());
            choiceDialog.setVisible(true);
         }
    }
}

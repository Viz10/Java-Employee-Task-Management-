package GUI;

import BusinessLogic.TasksManagement;
import BusinessLogic.Utility;
import DataModel.ComplexTask;
import DataModel.Task;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class ViewDataDialog extends JDialog implements ActionListener{

    TasksManagement tasksManagement;
    Utility utility;
    JPanel up;
    JPanel info;
    JPanel right;
    JScrollPane scrollPane;
    JButton refreshButton;
    JTree tree;
    DefaultMutableTreeNode root;
    private Map<String, Runnable> map;

    public ViewDataDialog(JFrame parent, String title,JPanel right, TasksManagement tasksManagement,Utility utility) {
        super(parent, title, false);

        this.tasksManagement = tasksManagement;
        this.utility = utility;
        this.right = right;
        this.tree = new JTree();
        this.root = new DefaultMutableTreeNode();

        map = new HashMap<>();
        up = new JPanel();
        info = new JPanel();
        scrollPane = new JScrollPane();
        refreshButton = new JButton("Refresh");

        setUpMap();

        this.setLayout(new BorderLayout());

        up.setPreferredSize(new Dimension(100, 80));
        up.setLayout(new FlowLayout());
        refreshButton.setPreferredSize(new Dimension(80, 50));
        refreshButton.setFocusable(false);
        refreshButton.addActionListener(this);
        up.add(refreshButton);
        up.setBackground(Color.black);
        up.setBorder(new EmptyBorder(10, 10, 0, 10));

        ////////////////////////////////////////////////////////////////////////////////////////////////

        info.setLayout(new BorderLayout());
        info.setBorder(BorderFactory.createLineBorder(Color.black, 3));

        refresh(); /// this function generates the data to be displayed

        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(600, 600);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.add(up, BorderLayout.NORTH);
        this.add(info, BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> scrollPane.getViewport().setViewPosition(new Point(0, 0))); /// start scrollbar from up top
    }

    void setUpMap() {
        map.put("View Employees", this::printEmployee);
        map.put("View unassigned tasks", this::printVacantTasks);
    }

    private void refresh(){
        Runnable action = map.get(getTitle());
        if (action != null) { action.run(); }
    }

    private void printEmployee() {

        info.removeAll();

        JPanel container=new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        Map<String,List<Task>> arr = utility.listEmployees();

        if (arr == null || arr.isEmpty()) {
            JLabel label = new JLabel("No employees");
            container.add(label);
        }
        else{
            for(Map.Entry<String,List<Task>> entry : arr.entrySet()){

                JLabel label = new JLabel(entry.getKey());
                label.setFont(new Font(null, Font.PLAIN, 19));

                label.addMouseListener(new MouseAdapter() { /// user interactivity
                @Override
                public void mouseClicked(MouseEvent e) { /// this will capture "enclose" the label and list references even after the loop exectution
                    right.removeAll();
                    tree.removeAll();
                    root.removeAllChildren();

                    root.setUserObject(label.getText()); /// name
                    populateTreeEmployeeTasks(tree,root,entry.getValue()); /// create the tree
                    right.add(new JScrollPane(tree));

                    right.revalidate();
                    right.repaint();
                }
                    /// hover
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        label.setForeground(Color.BLUE);
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        label.setForeground(Color.BLACK);
                    }
                });
                container.add(label);
            }
        }

        scrollPane = new JScrollPane(container);
        info.add(scrollPane);

        info.revalidate();
        info.repaint();
    }
    private void populateTreeEmployeeTasks(JTree tree, DefaultMutableTreeNode root, List<Task> list) { /// bfs
        tree.setModel(new DefaultTreeModel(root)); /// set root

        Queue<Task> tasks = new LinkedList<>();
        Queue<DefaultMutableTreeNode> nodes = new LinkedList<>();

        for (Task task : list) { /// add 1st layer
            tasks.add(task);
            DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(task.toString());
            root.add(currentNode);
            nodes.add(currentNode);
        }

        while (!tasks.isEmpty()) {
            DefaultMutableTreeNode currentRoot = nodes.poll();
            Task task = tasks.poll();

            if(task instanceof ComplexTask) {
                for (Task task1 : ((ComplexTask) task).getComplex_list()) {
                    tasks.add(task1);
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(task1.toString());
                    assert currentRoot != null;
                    currentRoot.add(node);
                    nodes.add(node);
                }
            }
    }
    }

    private void printVacantTasks(){

        info.removeAll();
        JPanel container=new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        List<Task> vacantTasks = utility.listVacantTasks();

        if (vacantTasks == null || vacantTasks.isEmpty()) {
            JLabel label = new JLabel("No unassigned tasks found");
            container.add(label);
        }
        else {
            HashSet<Integer> seen = new HashSet<>(vacantTasks.size());

            for(Task task : vacantTasks){

                JLabel label = new JLabel(task.toString());
                label.setFont(new Font(null, Font.PLAIN, 14));

                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        right.removeAll();
                        tree.removeAll();
                        root.removeAllChildren();

                        seen.add(task.getIdTask()); /// add root as visited
                        root.setUserObject(label.getText());
                        if (task instanceof ComplexTask) {
                            populateTreeVacantTasks(tree, root,((ComplexTask) task).getComplex_list(), seen);
                        }
                        else{
                            tree.setModel(new DefaultTreeModel(root));
                        }

                        right.add(new JScrollPane(tree));
                        right.revalidate();
                        right.repaint();
                    }
                    /// hover
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        label.setForeground(Color.green);
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        label.setForeground(Color.BLACK);
                    }
                });
                    container.add(label);
            }
        }

        scrollPane = new JScrollPane(container);
        info.add(scrollPane);

        info.revalidate();
        info.repaint();
    }
    private void populateTreeVacantTasks(JTree tree, DefaultMutableTreeNode root, List<Task> list,HashSet<Integer> seen) {
        tree.setModel(new DefaultTreeModel(root));

        Queue<Task> tasksQueue = new LinkedList<>();
        Queue<DefaultMutableTreeNode> nodesQueue = new LinkedList<>();

        for (Task task : list) {
            tasksQueue.add(task);
            seen.add(task.getIdTask());
            DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(task.toString());
            root.add(currentNode);
            nodesQueue.add(currentNode);
        }

        while (!tasksQueue.isEmpty()) {
            DefaultMutableTreeNode currentRoot = nodesQueue.poll();
            Task task = tasksQueue.poll();

            if(task instanceof ComplexTask) {
                for (Task task1 : ((ComplexTask) task).getComplex_list()) {
                    if (!seen.contains(task1.getIdTask())) {
                    tasksQueue.add(task1);
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(task1.toString());
                    assert currentRoot != null;
                    currentRoot.add(node);
                    nodesQueue.add(node);
                    seen.add(task.getIdTask());
                }
                }
            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==refreshButton){
            refresh();
            SwingUtilities.invokeLater(() -> scrollPane.getViewport().setViewPosition(new Point(0, 0)));
        }
    }
}


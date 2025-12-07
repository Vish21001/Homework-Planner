import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.*;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

public class StudyPlanner extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField subjectField, descField, dueDateField;
    private JComboBox<String> priorityBox;
    private static final String TASKS_FILE = "tasks.json";
    private JSONArray tasks;

    public StudyPlanner() {
        setTitle("Study Planner");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load tasks from JSON
        tasks = loadTasks();

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 5, 5, 5));

        subjectField = new JTextField();
        descField = new JTextField();
        dueDateField = new JTextField();
        priorityBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        JButton addButton = new JButton("Add Task");

        inputPanel.add(new JLabel("Subject"));
        inputPanel.add(new JLabel("Description"));
        inputPanel.add(new JLabel("Due Date"));
        inputPanel.add(new JLabel("Priority"));
        inputPanel.add(new JLabel(""));
        inputPanel.add(subjectField);
        inputPanel.add(descField);
        inputPanel.add(dueDateField);
        inputPanel.add(priorityBox);
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new String[]{"Subject", "Description", "Due Date", "Priority", "Completed"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Populate table with loaded tasks
        renderTasks();

        // Add task button
        addButton.addActionListener(e -> addTask());

        // Mark complete on double-click
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    toggleComplete(row);
                }
            }
        });

        setVisible(true);
    }

    // Load tasks from tasks.json
    private JSONArray loadTasks() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(TASKS_FILE)));
            return new JSONArray(content);
        } catch (IOException e) {
            return new JSONArray();
        }
    }

    // Save tasks to tasks.json
    private void saveTasks() {
        try {
            Files.write(Paths.get(TASKS_FILE), tasks.toString(4).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Render table from tasks JSONArray
    private void renderTasks() {
        model.setRowCount(0); // clear table
        for (int i = 0; i < tasks.length(); i++) {
            JSONObject task = tasks.getJSONObject(i);
            model.addRow(new Object[]{
                task.getString("subject"),
                task.getString("description"),
                task.getString("dueDate"),
                task.getString("priority"),
                task.getBoolean("completed")
            });
        }
    }

    // Add new task
    private void addTask() {
        JSONObject task = new JSONObject();
        task.put("subject", subjectField.getText());
        task.put("description", descField.getText());
        task.put("dueDate", dueDateField.getText());
        task.put("priority", priorityBox.getSelectedItem());
        task.put("completed", false);

        tasks.put(task);
        saveTasks();
        renderTasks();

        // Clear input fields
        subjectField.setText("");
        descField.setText("");
        dueDateField.setText("");
    }

    // Toggle completion status
    private void toggleComplete(int row) {
        JSONObject task = tasks.getJSONObject(row);
        task.put("completed", !task.getBoolean("completed"));
        saveTasks();
        renderTasks();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudyPlanner::new);
    }
}

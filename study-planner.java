import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class StudyPlanner extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField subjectField, descField;
    private JComboBox<String> priorityBox;
    private JTextField dueDateField;

    public StudyPlanner() {
        setTitle("Study Planner");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

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

        // Add task
        addButton.addActionListener(e -> {
            model.addRow(new Object[]{
                    subjectField.getText(),
                    descField.getText(),
                    dueDateField.getText(),
                    priorityBox.getSelectedItem(),
                    false
            });
            subjectField.setText("");
            descField.setText("");
            dueDateField.setText("");
        });

        // Mark complete on double-click
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    boolean completed = (boolean) model.getValueAt(row, 4);
                    model.setValueAt(!completed, row, 4);
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudyPlanner::new);
    }
}

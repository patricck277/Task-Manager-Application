import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

public class TaskDetailsDialog extends JDialog {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> statusComboBox;
    private JDatePickerImpl datePicker;
    private JButton saveButton;

    public TaskDetailsDialog(JFrame parent, Task task, int selectedRow, DefaultTableModel tableModel) {
        super(parent, "Szczegóoy zadania", true);
        setSize(400, 300);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // ikona
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/logo.png"));
        setIconImage(logoIcon.getImage());

        // kolory
        getContentPane().setBackground(Color.BLACK);

        titleField = new JTextField(task.getTitle());
        titleField.setBackground(Color.DARK_GRAY);
        titleField.setForeground(Color.WHITE);

        descriptionArea = new JTextArea(task.getDescription());
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBackground(Color.DARK_GRAY);
        descriptionArea.setForeground(Color.WHITE);

        statusComboBox = new JComboBox<>(new String[]{"Do zrobienia", "W trakcie", "Zakończone", "Wstrzymane"});
        statusComboBox.setSelectedItem(task.getStatus());
        statusComboBox.setBackground(Color.DARK_GRAY);
        statusComboBox.setForeground(Color.WHITE);

        // datePicker
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.getJFormattedTextField().setBackground(Color.DARK_GRAY);
        datePicker.getJFormattedTextField().setForeground(Color.WHITE);
        datePicker.getModel().setDate(Integer.parseInt(task.getDueDate().substring(0, 4)),
                Integer.parseInt(task.getDueDate().substring(5, 7)) - 1,
                Integer.parseInt(task.getDueDate().substring(8, 10)));
        datePicker.getModel().setSelected(true);

        JLabel titleLabelDialog = new JLabel("TYTUL:", SwingConstants.RIGHT);
        titleLabelDialog.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;
        add(titleLabelDialog, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(titleField, gbc);

        JLabel descriptionLabelDialog = new JLabel("OPIS:", SwingConstants.RIGHT);
        descriptionLabelDialog.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        add(descriptionLabelDialog, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(new JScrollPane(descriptionArea), gbc);

        JLabel statusLabelDialog = new JLabel("STATUS:", SwingConstants.RIGHT);
        statusLabelDialog.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        add(statusLabelDialog, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(statusComboBox, gbc);

        JLabel dueDateLabelDialog = new JLabel("DATA WYKONANIA:", SwingConstants.RIGHT);
        dueDateLabelDialog.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        add(dueDateLabelDialog, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(datePicker, gbc);

        saveButton = new JButton("ZAPISZ");
        saveButton.setBackground(Color.GRAY);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tableModel.setValueAt(titleField.getText(), selectedRow, 0);
                tableModel.setValueAt(datePicker.getJFormattedTextField().getText(), selectedRow, 1);
                tableModel.setValueAt((String) statusComboBox.getSelectedItem(), selectedRow, 2);
                tableModel.setValueAt(descriptionArea.getText(), selectedRow, 3);
                ((TaskManager) parent).saveTasks();
                dispose();
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        add(saveButton, gbc);

        setLocationRelativeTo(parent);
    }
}

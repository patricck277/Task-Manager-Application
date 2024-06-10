import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class TaskManager extends JFrame {
    private DefaultTableModel tableModel;
    private JTable taskTable;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> statusComboBox;
    private JDatePickerImpl datePicker;

    public TaskManager() {
        setTitle("Task Manager - Patryk Norek");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // logo
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/logo.png"));
        setIconImage(logoIcon.getImage());

        Image scaledLogo = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(scaledLogo);

        // Kolory
        getContentPane().setBackground(Color.BLACK);
        setForeground(Color.WHITE);

        tableModel = new DefaultTableModel(new Object[]{"Tytul", "Data wykonania", "Status", "Opis"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Zablokowanie edycji komórek
            }
        };
        taskTable = new JTable(tableModel);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskTable.setBackground(Color.DARK_GRAY);
        taskTable.setForeground(Color.WHITE);

        titleField = new JTextField();
        titleField.setBackground(Color.DARK_GRAY);
        titleField.setForeground(Color.WHITE);

        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBackground(Color.DARK_GRAY);
        descriptionArea.setForeground(Color.WHITE);

        statusComboBox = new JComboBox<>(new String[]{"Do zrobienia", "W trakcie", "Zakończone", "Wstrzymane"});
        statusComboBox.setBackground(Color.DARK_GRAY);
        statusComboBox.setForeground(Color.WHITE);

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setBackground(Color.DARK_GRAY);
        datePicker.setForeground(Color.WHITE);

        // Panel z elementami wprowadzania danych
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // logo
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 5; //
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel logoLabel = new JLabel(logoIcon);
        inputPanel.add(logoLabel, gbc);

        // resetowanie
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridx = 1;
        gbc.gridy = 0;
        JLabel titleLabel = new JLabel("TYTUL:", SwingConstants.RIGHT);
        titleLabel.setForeground(Color.WHITE);
        inputPanel.add(titleLabel, gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        inputPanel.add(titleField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JLabel descriptionLabel = new JLabel("OPIS:", SwingConstants.RIGHT);
        descriptionLabel.setForeground(Color.WHITE);
        inputPanel.add(descriptionLabel, gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        inputPanel.add(new JScrollPane(descriptionArea), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JLabel statusLabel = new JLabel("STATUS:", SwingConstants.RIGHT);
        statusLabel.setForeground(Color.WHITE);
        inputPanel.add(statusLabel, gbc);
        gbc.gridx = 2;
        gbc.gridy = 2;
        inputPanel.add(statusComboBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JLabel dueDateLabel = new JLabel("DATA WYKONANIA:", SwingConstants.RIGHT);
        dueDateLabel.setForeground(Color.WHITE);
        inputPanel.add(dueDateLabel, gbc);
        gbc.gridx = 2;
        gbc.gridy = 3;
        inputPanel.add(datePicker, gbc);

        JButton addButton = new JButton("DODAJ");
        JButton detailsButton = new JButton("SZCZEGOLY");
        JButton deleteButton = new JButton("USUN");

        // kolory buttonow
        addButton.setBackground(Color.GRAY);
        addButton.setForeground(Color.WHITE);
        detailsButton.setBackground(Color.GRAY);
        detailsButton.setForeground(Color.WHITE);
        deleteButton.setBackground(Color.GRAY);
        deleteButton.setForeground(Color.WHITE);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        detailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openTaskDetailsDialog();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteTask();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(addButton);
        buttonPanel.add(detailsButton);
        buttonPanel.add(deleteButton);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(taskTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadTasks();

        // menu - Dodatki
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Dodatki");
        JMenuItem importItem = new JMenuItem("Importuj zadania");
        JMenuItem exportItem = new JMenuItem("Eksportuj zadania");
        JMenuItem generatePdfItem = new JMenuItem("Generuj PDF");

        importItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                importTasks();
            }
        });

        exportItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportTasks();
            }
        });

        generatePdfItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TaskPDFGenerator.generatePDF(TaskManager.this, tableModel);
            }
        });

        fileMenu.add(importItem);
        fileMenu.add(exportItem);
        fileMenu.add(generatePdfItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }
// otwiera nowe okno ( szczeg.)
    private void openTaskDetailsDialog() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            String title = (String) tableModel.getValueAt(selectedRow, 0);
            String dueDate = (String) tableModel.getValueAt(selectedRow, 1);
            String status = (String) tableModel.getValueAt(selectedRow, 2);
            String description = (String) tableModel.getValueAt(selectedRow, 3);
            Task selectedTask = new Task(title, description, status, dueDate);
            TaskDetailsDialog dialog = new TaskDetailsDialog(this, selectedTask, selectedRow, tableModel);
            dialog.setVisible(true);
        }
    }

    private void addTask() {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String status = (String) statusComboBox.getSelectedItem();
        String dueDate = datePicker.getJFormattedTextField().getText();

        if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Wszystkie pola muszą być wypełnione", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Task task = new Task(title, description, status, dueDate);
        tableModel.addRow(new Object[]{task.getTitle(), task.getDueDate(), task.getStatus(), task.getDescription()});
        saveTasks();
        clearInputFields();
    }

    private void clearInputFields() {
        titleField.setText("");
        descriptionArea.setText("");
        statusComboBox.setSelectedIndex(0);
        datePicker.getJFormattedTextField().setText("");
    }

    private void deleteTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
            saveTasks();
        }
    }

    public void loadTasks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tasks.dat"))) {
            ArrayList<Task> tasks = (ArrayList<Task>) ois.readObject();
            for (Task task : tasks) {
                tableModel.addRow(new Object[]{task.getTitle(), task.getDueDate(), task.getStatus(), task.getDescription()});
            }
        } catch (IOException | ClassNotFoundException e) {
        }
    }

    public void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tasks.dat"))) {
            ArrayList<Task> tasks = new ArrayList<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String title = (String) tableModel.getValueAt(i, 0);
                String dueDate = (String) tableModel.getValueAt(i, 1);
                String status = (String) tableModel.getValueAt(i, 2);
                String description = (String) tableModel.getValueAt(i, 3);
                tasks.add(new Task(title, description, status, dueDate));
            }
            oos.writeObject(tasks);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Nie mozna zapisac zadan", "Blad", JOptionPane.ERROR_MESSAGE);
        }
    }
    // import / export
    private void exportTasks() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(file)) {
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    writer.println(tableModel.getValueAt(i, 0) + ";" +
                            tableModel.getValueAt(i, 1) + ";" +
                            tableModel.getValueAt(i, 2) + ";" +
                            tableModel.getValueAt(i, 3));
                }
                JOptionPane.showMessageDialog(this, "Zadania wyeksportowane pomyślnie");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Błąd podczas eksportu zadań", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importTasks() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 4) {
                        tableModel.addRow(parts);
                    }
                }
                JOptionPane.showMessageDialog(this, "Zadania zaimportowane pomyślnie");
                saveTasks();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Błąd podczas importu zadań", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TaskManager().setVisible(true);
            }
        });
    }
}

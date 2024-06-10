import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.awt.Component;

public class TaskIO {

    public static void exportTasks(Component parent, DefaultTableModel tableModel) {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(parent);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    writer.println(tableModel.getValueAt(i, 0) + "," + tableModel.getValueAt(i, 1) + "," + tableModel.getValueAt(i, 2));
                }
                JOptionPane.showMessageDialog(parent, "Zadania zostały wyeksportowane pomyślnie.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Błąd podczas eksportowania zadań.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void importTasks(Component parent, DefaultTableModel tableModel) {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(parent);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        tableModel.addRow(new Object[]{parts[0], parts[1], parts[2]});
                    }
                }
                JOptionPane.showMessageDialog(parent, "Zadania zostały zaimportowane pomyślnie.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Błąd podczas importowania zadań.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

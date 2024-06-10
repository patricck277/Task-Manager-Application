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
                    writer.println(tableModel.getValueAt(i, 0) + "," + tableModel.getValueAt(i, 1) + "," + tableModel.getValueAt(i, 2) + "," + tableModel.getValueAt(i, 3));
                }
                JOptionPane.showMessageDialog(parent, "Tasks exported successfully.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Error exporting tasks.", "Error", JOptionPane.ERROR_MESSAGE);
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
                    if (parts.length == 4) {
                        tableModel.addRow(new Object[]{parts[0], parts[1], parts[2], parts[3]});
                    }
                }
                JOptionPane.showMessageDialog(parent, "Tasks imported successfully.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Error importing tasks.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

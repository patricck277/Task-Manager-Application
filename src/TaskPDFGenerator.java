import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import java.io.File;
import java.io.IOException;

public class TaskPDFGenerator {

    public static void generatePDF(Component parent, DefaultTableModel tableModel) {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(parent);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".pdf")) {
                file = new File(file.getAbsolutePath() + ".pdf");
            }
            try {
                PdfWriter writer = new PdfWriter(file);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                // tytuł
                document.add(new Paragraph("Task List"));

                // tabela z taka sama liczbą kolumn jak tableModel
                Table table = new Table(tableModel.getColumnCount());
                // nagłowki
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    table.addHeaderCell(tableModel.getColumnName(i));
                }
                // dodaj wiersze
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        table.addCell(tableModel.getValueAt(i, j).toString());
                    }
                }
                document.add(table);
                document.close();
                JOptionPane.showMessageDialog(parent, "PDF wygenerowany pomyślnie.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Błąd podczas generowania PDF.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

// Omar Tohami

public class ReportForm extends JFrame {
    private JTextField idField, genDateField, briefField, approvalDateField;
    private JButton saveButton, exitButton;

    public ReportForm() {
        setTitle("Add New Report");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        idField = new JTextField();
        genDateField = new JTextField();
        briefField = new JTextField();
        approvalDateField = new JTextField();
        saveButton = new JButton("Save Report");
        exitButton = new JButton("Exit");

        add(new JLabel("Report ID:"));
        add(idField);
        add(new JLabel("Generation Date (YYYY-MM-DD):"));
        add(genDateField);
        add(new JLabel("Brief:"));
        add(briefField);
        add(new JLabel("Approval Date (YYYY-MM-DD):"));
        add(approvalDateField);
        add(saveButton);
        add(exitButton);

        saveButton.addActionListener(e -> {
            String idText = idField.getText().trim();
            String genDateText = genDateField.getText().trim();
            String brief = briefField.getText().trim();
            String approvalDateText = approvalDateField.getText().trim();

            if (idText.isEmpty() || genDateText.isEmpty() || brief.isEmpty() || approvalDateText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int reportId = Integer.parseInt(idText);
                LocalDate generationDate = LocalDate.parse(genDateText);
                LocalDate approvalDate = LocalDate.parse(approvalDateText);
                
                Report report = new Report(reportId, generationDate, brief, approvalDate);
                boolean success = report.saveToDatabase();

                if (success) {
                    JOptionPane.showMessageDialog(this, "Report saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error saving report to database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Report ID must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Dates must be in YYYY-MM-DD format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    public static void main(String[] args) {
        new ReportForm();
    }
}

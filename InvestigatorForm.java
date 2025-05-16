import javax.swing.*;
import java.awt.*;

//Omar Tohami

public class InvestigatorForm extends JFrame {
    private JTextField activeCasesField, investigatorIdField;
    private JButton saveButton, exitButton;

    public InvestigatorForm() {
        setTitle("Add New Investigator");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        activeCasesField = new JTextField();
        investigatorIdField = new JTextField();
        saveButton = new JButton("Save Investigator");
        exitButton = new JButton("Exit");

        add(new JLabel("Active Cases:"));
        add(activeCasesField);
        add(new JLabel("Investigator ID:"));
        add(investigatorIdField);
        add(saveButton);
        add(exitButton);

        saveButton.addActionListener(e -> {
            String activeCasesText = activeCasesField.getText().trim();
            String investigatorId = investigatorIdField.getText().trim();

            if (activeCasesText.isEmpty() || investigatorId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int activeCases = Integer.parseInt(activeCasesText);
                
                Investigator investigator = new Investigator(activeCases, investigatorId);
                boolean success = investigator.saveToDatabase();

                if (success) {
                    JOptionPane.showMessageDialog(this, "Investigator saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error saving investigator to database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Active Cases must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    public static void main(String[] args) {
        new InvestigatorForm();
    }
}

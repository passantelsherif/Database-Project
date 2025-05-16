import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EmployeeForm extends JFrame {
    private JTextField investigatorIdField, employeeIdField, activeCasesField, bdDateField, emailField, ageField, firstNameField, lastNameField;
    private JButton saveButton, exitButton;

    public EmployeeForm() {
        setTitle("Add New Employee");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(10, 1));

        investigatorIdField = new JTextField();
        employeeIdField = new JTextField();
        activeCasesField = new JTextField();
        bdDateField = new JTextField();
        emailField = new JTextField();
        ageField = new JTextField();
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        saveButton = new JButton("Save Employee");
        exitButton = new JButton("Exit");

        add(new JLabel("Investigator ID:"));
        add(investigatorIdField);
        add(new JLabel("Employee ID:"));
        add(employeeIdField);
        add(new JLabel("Active Cases:"));
        add(activeCasesField);
        add(new JLabel("Birth Date (YYYY-MM-DD):"));
        add(bdDateField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Age (optional):"));
        add(ageField);
        add(new JLabel("First Name:"));
        add(firstNameField);
        add(new JLabel("Last Name:"));
        add(lastNameField);
        add(saveButton);
        add(exitButton);

        saveButton.addActionListener(e -> {
            String investigatorId = investigatorIdField.getText().trim();
            String employeeIdText = employeeIdField.getText().trim();
            String activeCasesText = activeCasesField.getText().trim();
            String bdDateText = bdDateField.getText().trim();
            String email = emailField.getText().trim();
            String ageText = ageField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();

            if (investigatorId.isEmpty() || employeeIdText.isEmpty() || 
                activeCasesText.isEmpty() || bdDateText.isEmpty() || 
                email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int employeeId = Integer.parseInt(employeeIdText);
                int activeCases = Integer.parseInt(activeCasesText);
                LocalDate bdDate = LocalDate.parse(bdDateText);
                Integer age = ageText.isEmpty() ? null : Integer.parseInt(ageText);
                
                Employee employee = new Employee(investigatorId, employeeId, activeCases, bdDate, email, age, firstName, lastName);
                boolean success = employee.saveToDatabase();

                if (success) {
                    JOptionPane.showMessageDialog(this, "Employee saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error saving employee to database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Employee ID and Active Cases must be numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Birth Date must be in YYYY-MM-DD format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    public static void main(String[] args) {
        new EmployeeForm();
    }
}
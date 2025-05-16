import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

//Omar Tohami

public class PaymentForm extends JFrame {
    private JTextField paymentIdField, discountIdField, paymentMethodField, paymentDateField;
    private JButton saveButton, exitButton;

    public PaymentForm() {
        setTitle("Add New Payment");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        paymentIdField = new JTextField();
        discountIdField = new JTextField();
        paymentMethodField = new JTextField();
        paymentDateField = new JTextField();
        saveButton = new JButton("Save Payment");
        exitButton = new JButton("Exit");

        add(new JLabel("Payment ID:"));
        add(paymentIdField);
        add(new JLabel("Discount ID:"));
        add(discountIdField);
        add(new JLabel("Payment Method:"));
        add(paymentMethodField);
        add(new JLabel("Payment Date (YYYY-MM-DD):"));
        add(paymentDateField);
        add(saveButton);
        add(exitButton);

        saveButton.addActionListener(e -> {
            String paymentIdText = paymentIdField.getText().trim();
            String discountIdText = discountIdField.getText().trim();
            String paymentMethod = paymentMethodField.getText().trim();
            String paymentDateText = paymentDateField.getText().trim();

            if (paymentIdText.isEmpty() || discountIdText.isEmpty() || 
                paymentMethod.isEmpty() || paymentDateText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int paymentId = Integer.parseInt(paymentIdText);
                int discountId = Integer.parseInt(discountIdText);
                LocalDate paymentDate = LocalDate.parse(paymentDateText);
                
                Payment payment = new Payment(paymentId, discountId, paymentMethod, paymentDate);
                boolean success = payment.saveToDatabase();

                if (success) {
                    JOptionPane.showMessageDialog(this, "Payment saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error saving payment to database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Payment ID and Discount ID must be numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Payment Date must be in YYYY-MM-DD format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    public static void main(String[] args) {
        new PaymentForm();
    }
}

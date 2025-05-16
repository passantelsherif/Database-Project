import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AccidentForm extends JFrame {
    private JTextField idField, plateField, carIdField, dateField, locationField;
    private JButton addButton, updateButton, viewButton, exitButton;

    public AccidentForm() {
        setTitle("Accident Management");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 1));

        idField = new JTextField();
        plateField = new JTextField();
        carIdField = new JTextField();
        dateField = new JTextField(); // Format: YYYY-MM-DD
        locationField = new JTextField();

        addButton = new JButton("Add Accident");
        updateButton = new JButton("Update Accident");
        viewButton = new JButton("View Accident");
        exitButton = new JButton("Exit");

        add(new JLabel("Accident ID:"));
        add(idField);
        add(new JLabel("Licence Plate:"));
        add(plateField);
        add(new JLabel("Car ID:"));
        add(carIdField);
        add(new JLabel("Date (YYYY-MM-DD):"));
        add(dateField);
        add(new JLabel("Location:"));
        add(locationField);
        add(addButton);
        add(updateButton);
        add(viewButton);
        add(exitButton);

        addButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String plate = plateField.getText().trim();
                String carId = carIdField.getText().trim();
                LocalDate date = LocalDate.parse(dateField.getText().trim());
                String location = locationField.getText().trim();

                Accident accident = new Accident(id, plate, carId, date, location);
                boolean success = accident.saveToDatabase();

                JOptionPane.showMessageDialog(this,
                    success ? "Accident added successfully!" : "Failed to add accident.",
                    success ? "Success" : "Error",
                    success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String plate = plateField.getText().trim();
                String carId = carIdField.getText().trim();
                LocalDate date = LocalDate.parse(dateField.getText().trim());
                String location = locationField.getText().trim();

                Accident accident = new Accident(id, plate, carId, date, location);
                boolean success = accident.updateInDatabase();

                JOptionPane.showMessageDialog(this,
                    success ? "Accident updated successfully!" : "Update failed.",
                    success ? "Success" : "Error",
                    success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        viewButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                Accident acc = Accident.getAccidentById(id);

                if (acc != null) {
                    plateField.setText(acc.licencePlate);
                    carIdField.setText(acc.carId);
                    dateField.setText(acc.date.toString());
                    locationField.setText(acc.location);
                } else {
                    JOptionPane.showMessageDialog(this, "Accident not found.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    public static void main(String[] args) {
        new AccidentForm();
    }
}

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CarForm extends JFrame {
    private JTextField plateField, idField, yearField, modelField, valueField;
    private JButton saveButton, exitButton;
    private JTable carTable;
    private DefaultTableModel tableModel;

    public CarForm() {
        setTitle("Add Car");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // === Top Panel: Form Fields ===
        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        plateField = new JTextField();
        idField = new JTextField();
        yearField = new JTextField();
        modelField = new JTextField();
        valueField = new JTextField();
        saveButton = new JButton("Save");
        exitButton = new JButton("Exit");

        formPanel.add(new JLabel("License Plate:"));
        formPanel.add(plateField);
        formPanel.add(new JLabel("Car ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Year:"));
        formPanel.add(yearField);
        formPanel.add(new JLabel("Model:"));
        formPanel.add(modelField);
        formPanel.add(new JLabel("Current Value:"));
        formPanel.add(valueField);
        formPanel.add(saveButton);
        formPanel.add(exitButton);

        // === Bottom Panel: Table ===
        String[] columns = {"License Plate", "Car ID", "Year", "Model", "Current Value"};
        tableModel = new DefaultTableModel(columns, 0);
        carTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(carTable);

        // === Layout ===
        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // === Events ===
        saveButton.addActionListener(e -> insertCar());
        exitButton.addActionListener(e -> System.exit(0));

        // Load existing data
        loadCarData();

        setVisible(true);
    }

    private void insertCar() {
        String plate = plateField.getText().trim();
        String id = idField.getText().trim();
        String yearText = yearField.getText().trim();
        String model = modelField.getText().trim();
        String value = valueField.getText().trim();

        if (plate.isEmpty() || id.isEmpty() || yearText.isEmpty() || model.isEmpty() || value.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int year = Integer.parseInt(yearText);

            Car car = new Car(plate, id, year, model, value);
            boolean success = car.saveToDatabase();

            if (success) {
                JOptionPane.showMessageDialog(this, "Car saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadCarData(); // Refresh table
            } else {
                JOptionPane.showMessageDialog(this, "Error saving car to database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Year must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCarData() {
        tableModel.setRowCount(0); // Clear table
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=Carproject;encrypt=true;trustServerCertificate=true;";
        String user = "SANDII";
        String password = "sandy321";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM CAR")) {

            while (rs.next()) {
                String plate = rs.getString("LICENCE_PLATE");
                String id = rs.getString("CAR_ID");
                int year = rs.getInt("YEAR");
                String model = rs.getString("CAR_MODEL");
                String value = rs.getString("CURRENT_VALUE");

                tableModel.addRow(new Object[]{plate, id, year, model, value});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load car data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        plateField.setText("");
        idField.setText("");
        yearField.setText("");
        modelField.setText("");
        valueField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CarForm::new);
    }
}

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CarForm extends JFrame {
    private JTextField plateField, idField, yearField, modelField, valueField;
    private JButton saveButton, exitButton, editButton, deleteButton;
    private JTable carTable;
    private DefaultTableModel tableModel;

    public CarForm() {
        setTitle("Add Car");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // === Top Panel: Form Fields ===
        JPanel formPanel = new JPanel(new GridLayout(7, 2));
        plateField = new JTextField();
        idField = new JTextField();
        yearField = new JTextField();
        modelField = new JTextField();
        valueField = new JTextField();
        saveButton = new JButton("Save");
        exitButton = new JButton("Exit");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

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
        formPanel.add(editButton);
        formPanel.add(deleteButton);

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
        editButton.addActionListener(e -> updateCar());
        deleteButton.addActionListener(e -> deleteCar());

        carTable.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = carTable.getSelectedRow();
            if (selectedRow >= 0) {
                plateField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                idField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                yearField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                modelField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                valueField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                plateField.setEditable(false);
                idField.setEditable(false);
            }
        });

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
                loadCarData();
            } else {
                JOptionPane.showMessageDialog(this, "Error saving car to database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Year must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCar() {
        int selectedRow = carTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a car to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

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

            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=Carproject;encrypt=true;trustServerCertificate=true;";
            String user = "SANDII";
            String password = "sandy321";

            String updateQuery = "UPDATE CAR SET YEAR=?, CAR_MODEL=?, CURRENT_VALUE=? WHERE LICENCE_PLATE=? AND CAR_ID=?";

            try (Connection conn = DriverManager.getConnection(connectionUrl, user, password);
                 PreparedStatement ps = conn.prepareStatement(updateQuery)) {

                ps.setInt(1, year);
                ps.setString(2, model);
                ps.setString(3, value);
                ps.setString(4, plate);
                ps.setString(5, id);

                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Car updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadCarData();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Year must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCar() {
        int selectedRow = carTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a car to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String plate = tableModel.getValueAt(selectedRow, 0).toString();
        String id = tableModel.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this car?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=Carproject;encrypt=true;trustServerCertificate=true;";
        String user = "SANDII";
        String password = "sandy321";

        String deleteQuery = "DELETE FROM CAR WHERE LICENCE_PLATE=? AND CAR_ID=?";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password);
             PreparedStatement ps = conn.prepareStatement(deleteQuery)) {

            ps.setString(1, plate);
            ps.setString(2, id);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Car deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCarData();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCarData() {
        tableModel.setRowCount(0);
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
        plateField.setEditable(true);
        idField.setEditable(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CarForm::new);
    }
}

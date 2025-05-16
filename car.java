import java.sql.*;

public class Car {
    private String licencePlate;
    private String carId;
    private int year;
    private String carModel;
    private String currentValue;

    public Car(String licencePlate, String carId, int year, String carModel, String currentValue) {
        this.licencePlate = licencePlate;
        this.carId = carId;
        this.year = year;
        this.carModel = carModel;
        this.currentValue = currentValue;
    }

    public boolean saveToDatabase() {
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=Carproject;encrypt=true;trustServerCertificate=true;";
        String user = "SANDII";
        String password = "sandy321";

        String insertQuery = "INSERT INTO CAR (LICENCE_PLATE, CAR_ID, YEAR, CAR_MODEL, CURRENT_VALUE) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password);
             PreparedStatement ps = connection.prepareStatement(insertQuery)) {

            ps.setString(1, licencePlate);
            ps.setString(2, carId);
            ps.setInt(3, year);
            ps.setString(4, carModel);
            ps.setString(5, currentValue);

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

import java.sql.*;
import java.time.LocalDate;

public class Accident {
    private int accidentId;
    private String licencePlate;
    private String carId;
    private LocalDate date;
    private String location;

    public Accident(int accidentId, String licencePlate, String carId, LocalDate date, String location) {
        this.accidentId = accidentId;
        this.licencePlate = licencePlate;
        this.carId = carId;
        this.date = date;
        this.location = location;
    }

    public boolean saveToDatabase() {
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=Carproject;encrypt=true;trustServerCertificate=true;";
        String user = "SANDII";
        String password = "sandy321";

        String insertQuery = "INSERT INTO ACCIDENT (ACCIDENT_ID, LICENCE_PLATE, CAR_ID, DATE, LOCATION) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password);
             PreparedStatement ps = conn.prepareStatement(insertQuery)) {

            ps.setInt(1, accidentId);
            ps.setString(2, licencePlate);
            ps.setString(3, carId);
            ps.setDate(4, Date.valueOf(date));
            ps.setString(5, location);

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateInDatabase() {
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=Carproject;encrypt=true;trustServerCertificate=true;";
        String user = "SANDII";
        String password = "sandy321";

        String updateQuery = "UPDATE ACCIDENT SET LICENCE_PLATE = ?, CAR_ID = ?, DATE = ?, LOCATION = ? WHERE ACCIDENT_ID = ?";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password);
             PreparedStatement ps = conn.prepareStatement(updateQuery)) {

            ps.setString(1, licencePlate);
            ps.setString(2, carId);
            ps.setDate(3, Date.valueOf(date));
            ps.setString(4, location);
            ps.setInt(5, accidentId);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Accident getAccidentById(int id) {
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=Carproject;encrypt=true;trustServerCertificate=true;";
        String user = "SANDII";
        String password = "sandy321";

        String selectQuery = "SELECT * FROM ACCIDENT WHERE ACCIDENT_ID = ?";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password);
             PreparedStatement ps = conn.prepareStatement(selectQuery)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Accident(
                    rs.getInt("ACCIDENT_ID"),
                    rs.getString("LICENCE_PLATE"),
                    rs.getString("CAR_ID"),
                    rs.getDate("DATE").toLocalDate(),
                    rs.getString("LOCATION")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}

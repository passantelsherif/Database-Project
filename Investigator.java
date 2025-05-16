import java.sql.*;

//Omar Tohami

public class Investigator {
    private int activeCases;
    private String investigatorId;

    public Investigator(int activeCases, String investigatorId) {
        this.activeCases = activeCases;
        this.investigatorId = investigatorId;
    }

    public boolean saveToDatabase() {
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=Carproject;encrypt=true;trustServerCertificate=true;";
        String user = "SANDII";
        String password = "sandy321";

        String insertQuery = "INSERT INTO INVESTIGATOR (ACTIVE_CASES, INVESTIGATOR_ID) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password); 
            PreparedStatement ps = connection.prepareStatement(insertQuery)) {

            ps.setInt(1, activeCases);
            ps.setString(2, investigatorId);

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

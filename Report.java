import java.sql.*;
import java.time.LocalDate;

// Omar Tohami

public class Report {
    private int reportId;
    private LocalDate generationDate;
    private String brief;
    private LocalDate approvalDate;

    public Report(int reportId, LocalDate generationDate, String brief, LocalDate approvalDate) {
        this.reportId = reportId;
        this.generationDate = generationDate;
        this.brief = brief;
        this.approvalDate = approvalDate;
    }

    public boolean saveToDatabase() {
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=Carproject;encrypt=true;trustServerCertificate=true;";
        String user = "SANDII";
        String password = "sandy321";

        String insertQuery = "INSERT INTO REPORT (REPORT_ID, GENERATION_DATE, BRIEF, APPROVAL_DATE) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password); 
            PreparedStatement ps = connection.prepareStatement(insertQuery)) {

            ps.setInt(1, reportId);
            ps.setDate(2, Date.valueOf(generationDate));
            ps.setString(3, brief);
            ps.setDate(4, Date.valueOf(approvalDate));

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
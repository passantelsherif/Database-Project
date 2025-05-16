import java.sql.*;
import java.time.LocalDate;

// Omar Tohami

public class Employee {
    private String investigatorId;
    private int employeeId;
    private int activeCases;
    private LocalDate bdDate;
    private String email;
    private Integer age;
    private String firstName;
    private String lastName;

    public Employee(String investigatorId, int employeeId, int activeCases, 
        LocalDate bdDate, String email, Integer age, String firstName, String lastName) {
        this.investigatorId = investigatorId;
        this.employeeId = employeeId;
        this.activeCases = activeCases;
        this.bdDate = bdDate;
        this.email = email;
        this.age = age;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public boolean saveToDatabase() {
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=Carproject;encrypt=true;trustServerCertificate=true;";
        String user = "SANDII";
        String password = "sandy321";

        String insertQuery = "INSERT INTO EMPLOYEE (INVESTIGATOR_ID, EMPLOYEE_ID, ACTIVE_CASES, BD_DATE, EMAIL, AGE, FIRST_NAME, LAST_NAME) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password); 
            PreparedStatement ps = connection.prepareStatement(insertQuery)) {

            ps.setString(1, investigatorId);
            ps.setInt(2, employeeId);
            ps.setInt(3, activeCases);
            ps.setDate(4, Date.valueOf(bdDate));
            ps.setString(5, email);
            if (age != null) {
                ps.setInt(6, age);
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.setString(7, firstName);
            ps.setString(8, lastName);

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

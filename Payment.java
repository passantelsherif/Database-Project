import java.sql.*;
import java.time.LocalDate;

//Omar Tohami

public class Payment {
    private int paymentId;
    private int discountId;
    private String paymentMethod;
    private LocalDate paymentDate;

    public Payment(int paymentId, int discountId, String paymentMethod, LocalDate paymentDate) {
        this.paymentId = paymentId;
        this.discountId = discountId;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }

    public boolean saveToDatabase() {
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=Carproject;encrypt=true;trustServerCertificate=true;";
        String user = "SANDII";
        String password = "sandy321";

        String insertQuery = "INSERT INTO PAYMENT (PAYMENT_ID, DISCOUNT_ID, PAYMENT_METHOD, PAYMENT_DATE) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password); 
            PreparedStatement ps = connection.prepareStatement(insertQuery)) {

            ps.setInt(1, paymentId);
            ps.setInt(2, discountId);
            ps.setString(3, paymentMethod);
            ps.setDate(4, Date.valueOf(paymentDate));

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
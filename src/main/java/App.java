package src.main.java;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleConnection;
import java.sql.DatabaseMetaData;

import io.github.cdimascio.dotenv.Dotenv;

// This method creates a database connection using
// oracle.jdbc.pool.OracleDataSource.
public class App {
    static Dotenv dotenv = Dotenv.load();
    final static String DB_URL = dotenv.get("DB_URL");
    final static String DB_USER = dotenv.get("DB_USER");
    final static String DB_PASSWORD = dotenv.get("DB_PASSWORD");

    public static void main(String[] args) throws SQLException {
        Properties info = new Properties();

        System.out.println("Initializing connection properties...");
        info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
        info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);
        info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");

        System.out.println("Creating OracleDataSource...");
        OracleDataSource ods = new OracleDataSource();

        System.out.println("Setting connection properties...");
        //System.out.println(DB_URL);

        ods.setURL(DB_URL);
        //System.out.println(ods);
        ods.setConnectionProperties(info);

        // With AutoCloseable, the connection is closed automatically
        try (OracleConnection connection = (OracleConnection) ods.getConnection()) {
            System.out.println("Connection established!");
            // Get JDBC driver name and version
            DatabaseMetaData dbmd = connection.getMetaData();
            System.out.println("Driver Name: " + dbmd.getDriverName());
            System.out.println("Driver Version: " + dbmd.getDriverVersion());
            // Print some connection properties
            System.out.println(
                    "Default Row Prefetch Value: " + connection.getDefaultRowPrefetch()
            );
            System.out.println("Database username: " + connection.getUserName());
            System.out.println();
            // Perform some database operations
            //insertStudent(connection);
            printStudent(connection);
        } catch (Exception e) {
            System.out.println("CONNECTION ERROR:");
            System.out.println(e);
        }
    }

    public static void insertStudent(Connection connection) throws SQLException {
        System.out.println("Preparing to insert Student into Student_Is_In table...");
        // Statement and ResultSet are AutoCloseable and closed automatically.
        try (Statement statement = connection.createStatement()) {
            try (
                    ResultSet resultSet = statement.executeQuery(
                            "INSERT INTO student_is_in VALUES (90210, 'Tyler Scott', '505 Rodeo Dr', 'CS', 91313)"
                    )
            ) {}
        } catch (Exception e) {
            System.out.println("ERROR: insertion failed.");
            System.out.println(e);
        }
    }

    public static void printStudent(Connection connection) throws SQLException {
        // Statement and ResultSet are AutoCloseable and closed automatically.
        try (Statement statement = connection.createStatement()) {
            try (
                    ResultSet resultSet = statement.executeQuery(
                            "SELECT * FROM student_is_in"
                    )
            ) {
                System.out.println("Students:");
                System.out.println("Perm_Number\tS_Name\tAddress\tD_Name\tPin");
                while (resultSet.next()) {
                    System.out.println(
                            resultSet.getString("perm_number") + "\t"
                                    + resultSet.getString("s_name") + "\t"
                                    + resultSet.getString("address") + "\t"
                                    + resultSet.getString("d_name") + "\t"
                                    + resultSet.getString("pin")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR: selection failed.");
            System.out.println(e);
        }
    }
}

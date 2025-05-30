package src.main.java;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleConnection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;

import io.github.cdimascio.dotenv.Dotenv;

// This method creates a database connection using
// oracle.jdbc.pool.OracleDataSource.
public class App {
    static Dotenv dotenv = Dotenv.load();
    final static String DB_URL = dotenv.get("DB_URL");
    final static String DB_USER = dotenv.get("DB_USER");
    final static String DB_PASSWORD = dotenv.get("DB_PASSWORD");

    static boolean DEBUG = false;

    public static void DEBUG(Object x) {
        if (DEBUG) {
            System.out.println("[DEBUG] " + x);
        }
    }

    public static void main(String[] args) throws SQLException {
        Properties info = new Properties();

        DEBUG("Initializing connection properties...");
        info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
        info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);
        info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");

        DEBUG("Creating OracleDataSource...");
        OracleDataSource ods = new OracleDataSource();
        DEBUG("Setting connection properties...");
        ods.setURL(DB_URL);
        ods.setConnectionProperties(info);

        try (OracleConnection connection = (OracleConnection) ods.getConnection()) {
            DEBUG("Connection established!");

            // Get JDBC driver name and version
            DatabaseMetaData dbmd = connection.getMetaData();
            DEBUG("Driver Name: " + dbmd.getDriverName());
            DEBUG("Driver Version: " + dbmd.getDriverVersion());

            // Print some connection properties
            DEBUG("Default Row Prefetch Value: " + connection.getDefaultRowPrefetch());
            DEBUG("Database username: " + connection.getUserName());

            // insertStudent(connection, "90210", "Tyler Scott", "505 Rodeo Dr", "CS", "91313");
            // insertDepartment(connection, "ECR");
            printCourseOfferings(connection);
        } catch (Exception e) {
            System.out.println("CONNECTION ERROR:");
            System.out.println(e);
        }
    }

    public static void insertDepartment(Connection connection, String d_name) throws SQLException {
        String sql = "INSERT INTO department (d_name) VALUES (?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, d_name);
            statement.executeUpdate();

            System.out.println("Department Insertion Successful!");
        } catch (SQLException e) {
            System.err.println("ERROR: Department Insertion Failed");
            System.err.println(e);
        }
    }

    public static void insertStudent(Connection connection, String perm_number, String s_name, String address, String d_name, String pin) throws SQLException {
        String sql = "INSERT INTO student_is_in (perm_number, s_name, address, d_name, pin) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, perm_number);
            statement.setString(2, s_name);
            statement.setString(3, address);
            statement.setString(4, d_name);
            statement.setString(5, pin);
            statement.executeUpdate();

            System.out.println("Student Insertion Successful!");
        } catch (SQLException e) {
            System.err.println("ERROR: Student Insertion Failed");
            System.err.println(e);
        }
    }

    public static void insertCourseCatalog(Connection connection, String course_code) throws SQLException {
        String sql = "INSERT INTO course_catalog (course_code) VALUES (?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, course_code);
            statement.executeUpdate();

            System.out.println("Course Insertion Successful!");
        } catch (SQLException e) {
            System.err.println("ERROR: Course Insertion Failed");
            System.err.println(e);
        }
    }

    public static void insertPrereq(Connection connection, String course_code, String prereq_course) throws SQLException {
        String sql = "INSERT INTO prerequisite (course_code, prereq_course) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, course_code);
            statement.setString(2, prereq_course);
            statement.executeUpdate();

            System.out.println("Course Insertion Successful!");
        } catch (SQLException e) {
            System.err.println("ERROR: Course Insertion Failed");
            System.err.println(e);
        }
    }

    public static void insertCourseOffering(Connection connection, String course_code, String enrollment_code, String year_quarter, String p_name, String max_enrollment, String time_location) throws SQLException {
        String sql = "INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, course_code);
            statement.setString(2, enrollment_code);
            statement.setString(3, year_quarter);
            statement.setString(4, p_name);
            statement.setString(5, max_enrollment);
            statement.setString(6, time_location);
            statement.executeUpdate();

            System.out.println("Course Offering Insertion Successful!");
        } catch (SQLException e) {
            System.err.println("ERROR: Course Offering Insertion Failed");
            System.err.println(e);
        }
    }

    public static void printDepartments(Connection connection) throws SQLException {
        String sql = "SELECT * FROM department";

        try (Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(sql)) {
            // Print formatted header
            System.out.println();
            System.out.printf("%-12s%n", "D_Name");
            System.out.println("-".repeat(80));
            
            while (rs.next()) {
                System.out.printf("%-12s%n",
                    rs.getString("d_name"));
            }

            System.out.println();
        } catch (SQLException e) {
            System.out.println("ERROR: Department Selection Failed.");
            System.out.println(e);
        }
    }

    public static void printStudents(Connection connection) throws SQLException {
        String sql = "SELECT perm_number, s_name, address, d_name, pin FROM student_is_in";
        
        try (Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(sql)) {
            // Print formatted header
            System.out.println();
            System.out.printf("%-12s %-20s %-30s %-10s %-6s%n", "Perm_Number", "Name", "Address", "Dept", "Pin");
            System.out.println("-".repeat(80));
            
            while (rs.next()) {
                System.out.printf("%-12d %-20s %-30s %-10s %-6d%n",
                    rs.getInt("perm_number"),
                    rs.getString("s_name"),
                    rs.getString("address"),
                    rs.getString("d_name"),
                    rs.getInt("pin"));
            } 

            System.out.println();         
        } catch (SQLException e) {
            System.out.println("ERROR: Student Selection Failed.");
            System.out.println(e);
        }
    }

    public static void printCourseCatalog(Connection connection) throws SQLException {
        String sql = "SELECT * FROM course_catalog";

        try (Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(sql)) {
            // Print formatted header
            System.out.println();
            System.out.printf("%-12s%n", "Course_Code");
            System.out.println("-".repeat(80));
            
            while (rs.next()) {
                System.out.printf("%-12s%n",
                    rs.getString("course_code"));
            }

            System.out.println();
        } catch (SQLException e) {
            System.out.println("ERROR: Course Catalog Selection Failed.");
            System.out.println(e);
        }
    }

    public static void printPrereqs(Connection connection) throws SQLException {
        String sql = "SELECT * FROM prerequisite";

        try (Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(sql)) {
            // Print formatted header
            System.out.println();
            System.out.printf("%-20s %-20s%n", "Course_Code", "Prerequisite_Course");
            System.out.println("-".repeat(80));
            
            while (rs.next()) {
                System.out.printf("%-20s %-20s%n",
                    rs.getString("course_code"),
                    rs.getString("prereq_course"));
            }

            System.out.println();
        } catch (SQLException e) {
            System.out.println("ERROR: Prerequisite Selection Failed.");
            System.out.println(e);
        }
    }

    public static void printCourseOfferings(Connection connection) throws SQLException {
        String sql = "SELECT * FROM course_offering";

        try (Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(sql)) {
            // Print formatted header
            System.out.println();
            System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s%n", "Course_Code", "Enrollment_Code", "Year_Quarter", "P_Name", "Max_Enrollment", "Time_Location");
            System.out.println("-".repeat(80));
            
            while (rs.next()) {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s%n",
                    rs.getString("course_code"),
                    rs.getString("enrollment_code"),
                    rs.getString("year_quarter"),
                    rs.getString("p_name"),
                    rs.getString("max_enrollment"),
                    rs.getString("time_location"));
            }

            System.out.println();
        } catch (SQLException e) {
            System.out.println("ERROR: Course Offerings Selection Failed.");
            System.out.println(e);
        }
    }
}

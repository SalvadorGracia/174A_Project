package src.main.java;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Properties;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleConnection;
import io.github.cdimascio.dotenv.Dotenv;

public class GUI extends JFrame {
    private JTable table;
    private JComboBox<String> tableSelector;
    private OracleConnection connection;

    public GUI() {
        setTitle("Database Table");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Initialize components
        table = new JTable();
        tableSelector = new JComboBox<>(new String[]{
            "department", 
            "student_is_in", 
            "course_catalog", 
            "prerequisite", 
            "course_offering"
        });
        
        // Layout
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Table:"));
        topPanel.add(tableSelector);
        
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        // Connect to database
        connectToDatabase();
        
        // Load initial table data
        loadTableData((String)tableSelector.getSelectedItem());
        
        // Add listeners
        tableSelector.addActionListener(e -> {
            String selectedTable = (String)tableSelector.getSelectedItem();
            loadTableData(selectedTable);
        });
        
    }
    
    private void connectToDatabase() {
        try {
            Dotenv dotenv = Dotenv.load();
            String DB_URL = dotenv.get("DB_URL");
            String DB_USER = dotenv.get("DB_USER");
            String DB_PASSWORD = dotenv.get("DB_PASSWORD");

            Properties info = new Properties();
            info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
            info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);
            info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");

            OracleDataSource ods = new OracleDataSource();
            ods.setURL(DB_URL);
            ods.setConnectionProperties(info);

            connection = (OracleConnection) ods.getConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    private void loadTableData(String tableName) {
        try {
            String query = "SELECT * FROM " + tableName;
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            // Create table model
            DefaultTableModel model = new DefaultTableModel();
            
            // Get metadata for column names
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // Add column names
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }
            
            // Add data rows
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }
            
            table.setModel(model);
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading table data: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void dispose() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.dispose();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI table = new GUI();
            table.setVisible(true);
        });
    }
}
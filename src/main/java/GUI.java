package src.main.java;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

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
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    public GUI() {
        
        setTitle("Registrar Interface");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create the card layout panel
        cardLayout = new CardLayout(5, 5);
        cardPanel = new JPanel(cardLayout);
        
        // Create panels for each "page"
        JPanel mainPage = mainPage();
        JPanel addStudentPage = addStudentPage(); 
        JPanel dropStudentPage = dropStudentPage();
        JPanel studentCoursesPage = studentCoursesPage();
        JPanel studentPrevQuarterGrade = prevStudentQuarterGrades();
        JPanel listCourseClasses = listCourseClasses();
        JPanel enterCourseGrades = enterCourseGrades();
        JPanel requestTranscript = requestTranscript();
        JPanel generateMailer = generateMailer();
        
        // Add panels to card layout
        cardPanel.add(mainPage, "Main");
        cardPanel.add(addStudentPage, "AddStudent");
        cardPanel.add(dropStudentPage, "DropStudent");
        cardPanel.add(studentCoursesPage, "StudentCourses");
        cardPanel.add(studentPrevQuarterGrade, "PrevQuarterGrades");
        cardPanel.add(listCourseClasses, "CourseClasses");
        cardPanel.add(enterCourseGrades, "EnterGrades");
        cardPanel.add(requestTranscript, "RequestTranscript");
        cardPanel.add(generateMailer, "GenerateMailer");
        
        // Add card panel to frame
        add(cardPanel);
        
        // Connect to database
        connectToDatabase();
        
        // Load initial table data
        if (tableSelector != null) {
            loadTableData((String)tableSelector.getSelectedItem());
        }
    }
    
    private JPanel mainPage() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome To Registrar Interface", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.NORTH);
        
        // Button panel for main actions
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        
        // New button to navigate to number input page
        JButton addStudentButton = new JButton("Add Student To Course");
        addStudentButton.addActionListener(e -> cardLayout.show(cardPanel, "AddStudent"));
        styleButton(addStudentButton);

        // New button to navigate to number input page
        JButton dropStudentButton = new JButton("Drop Student From Course");
        dropStudentButton.addActionListener(e -> cardLayout.show(cardPanel, "DropStudent"));
        styleButton(dropStudentButton);

        // Button to navigate to list student courses page
        JButton listCoursesButton = new JButton("Student's Courses");
        listCoursesButton.addActionListener(e -> cardLayout.show(cardPanel, "StudentCourses"));
        styleButton(listCoursesButton);
        
        // Button to navigate to student's previous quarter grades
        JButton prevQuarterGradesButton = new JButton("Student's Previous Grades");
        prevQuarterGradesButton.addActionListener(e -> cardLayout.show(cardPanel, "PrevQuarterGrades"));
        styleButton(prevQuarterGradesButton);

        // Button to navigate to classes offered for a course
        JButton listCourseClassesButton = new JButton("Course Offerings");
        listCourseClassesButton.addActionListener(e -> cardLayout.show(cardPanel, "CourseClasses"));
        styleButton(listCourseClassesButton);

        // Button to navigate to enter grades for a course
        JButton enterCourseGrades = new JButton("Enter Course Grades");
        enterCourseGrades.addActionListener(e -> cardLayout.show(cardPanel, "EnterGrades"));
        styleButton(enterCourseGrades);

        // Button to navigate to request student's transcript
        JButton requestTranscript = new JButton("Request Transcript");
        requestTranscript.addActionListener(e -> cardLayout.show(cardPanel, "RequestTranscript"));
        styleButton(requestTranscript);

        // Button to navigate to generate mailer
        JButton generateMailer = new JButton("Generate Mailer");
        generateMailer.addActionListener(e -> cardLayout.show(cardPanel, "GenerateMailer"));
        styleButton(generateMailer);
        
        buttonPanel.add(addStudentButton);
        buttonPanel.add(dropStudentButton);
        buttonPanel.add(listCoursesButton);
        buttonPanel.add(prevQuarterGradesButton);
        buttonPanel.add(listCourseClassesButton);
        buttonPanel.add(enterCourseGrades);
        buttonPanel.add(requestTranscript);
        buttonPanel.add(generateMailer);
        
        // Center the buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(buttonPanel);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(300, 60));
        button.setFont(new Font("Arial", Font.PLAIN, 18));
    }

    private JPanel studentCoursesPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Back button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Main"));
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // First input: 5-digit number
        JLabel numberLabel = new JLabel("Enter PERM Number (5 Digits):");
        numberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JTextField numberInputField = new JTextField(15);
        numberInputField.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Input validation - only allow digits and limit to 5 characters
        numberInputField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) 
                throws BadLocationException {
                if (str == null) return;
                
                // Only allow digits
                if (str.matches("\\d+") && getLength() + str.length() <= 5) {
                    super.insertString(offs, str, a);
                }
            }
        });
        
        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String numberText = numberInputField.getText().trim();
            boolean numberValid = validPERMNumber(numberText);

            if (numberValid) {
                JOptionPane.showMessageDialog(this, 
                    "Inputs are valid:\nNumber: " + numberText, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        
        // Add components to input panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(numberLabel, gbc);
        
        gbc.gridy = 1;
        inputPanel.add(numberInputField, gbc);
        
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(submitButton, gbc);
        
        // Add components to main panel
        panel.add(backButton, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel addStudentPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Back button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Main"));
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // First input: 5-digit number
        JLabel numberLabel = new JLabel("Enter PERM Number (5 Digits):");
        numberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JTextField numberInputField = new JTextField(15);
        numberInputField.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Input validation - only allow digits and limit to 5 characters
        numberInputField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) 
                throws BadLocationException {
                if (str == null) return;
                
                // Only allow digits
                if (str.matches("\\d+") && getLength() + str.length() <= 5) {
                    super.insertString(offs, str, a);
                }
            }
        });
        
        // Pattern input label
        JLabel patternLabel = new JLabel("Enter Course Code (2-4 Letters + 1-3 Digits):");
        patternLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Pattern input field with enhanced validation
        JTextField patternInputField = new JTextField(15);
        patternInputField.setFont(new Font("Arial", Font.PLAIN, 16));
        
        //Input Validation for pattern
        patternInputField.setDocument(new PlainDocument() {            
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null || str.isEmpty()) return;

                String currentText = getText(0, getLength());
                String newText = currentText.substring(0, offs) + str + currentText.substring(offs);

                if (newText.length() > 7) return;

                // Count letters and digits
                int letterCount = newText.replaceAll("[^A-Za-z]", "").length();
                int digitCount = newText.replaceAll("[^0-9]", "").length();

                // Check order: letters must come before digits
                if (!newText.matches("^[A-Za-z]*[0-9]*$")) return;

                // Enforce 2-4 letters first
                if (letterCount > 4) return;
                if (digitCount > 3) return;

                // If inserting a digit before having at least 2 letters, block it
                if (str.matches("[0-9]+") && letterCount < 2) return;

                // All valid, insert (convert letters to uppercase)
                if (str.matches("[A-Za-z]+")) {
                    super.insertString(offs, str.toUpperCase(), a);
                } else {
                    super.insertString(offs, str, a);
                }
            }
            
            @Override
            public void remove(int offs, int len) throws BadLocationException {
                String currentText = getText(0, getLength());
                String remainingText = currentText.substring(0, offs) + currentText.substring(offs + len);
                
                // Check if removal would leave us with less than 2 letters
                int remainingLetters = remainingText.replaceAll("[^A-Za-z]", "").length();
                if (remainingLetters >= 2 || remainingLetters == remainingText.length()) {
                    super.remove(offs, len);
                }
            }
        });
        
        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String pattern = patternInputField.getText().trim();
            String numberText = numberInputField.getText().trim();
            boolean numberValid = validPERMNumber(numberText);
            boolean patternValid = validCourseCode(pattern);

            if (numberValid && patternValid) {
                JOptionPane.showMessageDialog(this, 
                    "Inputs are valid:\nCode: " + pattern + "\nNumber: " + numberText, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        
        // Add components to input panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(numberLabel, gbc);
        
        gbc.gridy = 1;
        inputPanel.add(numberInputField, gbc);
        
        gbc.gridy = 2;
        inputPanel.add(patternLabel, gbc);
        
        gbc.gridy = 3;
        inputPanel.add(patternInputField, gbc);
        
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(submitButton, gbc);
        
        // Add components to main panel
        panel.add(backButton, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel dropStudentPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Back button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Main"));
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // First input: 5-digit number
        JLabel numberLabel = new JLabel("Enter PERM Number (5 Digits):");
        numberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JTextField numberInputField = new JTextField(15);
        numberInputField.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Input validation - only allow digits and limit to 5 characters
        numberInputField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) 
                throws BadLocationException {
                if (str == null) return;
                
                // Only allow digits
                if (str.matches("\\d+") && getLength() + str.length() <= 5) {
                    super.insertString(offs, str, a);
                }
            }
        });
        
        // Pattern input label
        JLabel patternLabel = new JLabel("Enter Course Code (2-4 Letters + 1-3 Digits):");
        patternLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Pattern input field with enhanced validation
        JTextField patternInputField = new JTextField(15);
        patternInputField.setFont(new Font("Arial", Font.PLAIN, 16));
        
        //Input Validation for pattern
        patternInputField.setDocument(new PlainDocument() {            
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null || str.isEmpty()) return;

                String currentText = getText(0, getLength());
                String newText = currentText.substring(0, offs) + str + currentText.substring(offs);

                if (newText.length() > 7) return;

                // Count letters and digits
                int letterCount = newText.replaceAll("[^A-Za-z]", "").length();
                int digitCount = newText.replaceAll("[^0-9]", "").length();

                // Check order: letters must come before digits
                if (!newText.matches("^[A-Za-z]*[0-9]*$")) return;

                // Enforce 2-4 letters first
                if (letterCount > 4) return;
                if (digitCount > 3) return;

                // If inserting a digit before having at least 2 letters, block it
                if (str.matches("[0-9]+") && letterCount < 2) return;

                // All valid, insert (convert letters to uppercase)
                if (str.matches("[A-Za-z]+")) {
                    super.insertString(offs, str.toUpperCase(), a);
                } else {
                    super.insertString(offs, str, a);
                }
            }
            
            @Override
            public void remove(int offs, int len) throws BadLocationException {
                String currentText = getText(0, getLength());
                String remainingText = currentText.substring(0, offs) + currentText.substring(offs + len);
                
                // Check if removal would leave us with less than 2 letters
                int remainingLetters = remainingText.replaceAll("[^A-Za-z]", "").length();
                if (remainingLetters >= 2 || remainingLetters == remainingText.length()) {
                    super.remove(offs, len);
                }
            }
        });
        
        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String pattern = patternInputField.getText().trim();
            String numberText = numberInputField.getText().trim();
            boolean numberValid = validPERMNumber(numberText);
            boolean patternValid = validCourseCode(pattern);

            if (numberValid && patternValid) {
                JOptionPane.showMessageDialog(this, 
                    "Inputs are valid:\nCode: " + pattern + "\nNumber: " + numberText, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        
        // Add components to input panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(numberLabel, gbc);
        
        gbc.gridy = 1;
        inputPanel.add(numberInputField, gbc);
        
        gbc.gridy = 2;
        inputPanel.add(patternLabel, gbc);
        
        gbc.gridy = 3;
        inputPanel.add(patternInputField, gbc);
        
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(submitButton, gbc);
        
        // Add components to main panel
        panel.add(backButton, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel prevStudentQuarterGrades() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Back button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Main"));
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // First input: 5-digit number
        JLabel numberLabel = new JLabel("Enter PERM Number (5 Digits):");
        numberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JTextField numberInputField = new JTextField(15);
        numberInputField.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Input validation - only allow digits and limit to 5 characters
        numberInputField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) 
                throws BadLocationException {
                if (str == null) return;
                
                // Only allow digits
                if (str.matches("\\d+") && getLength() + str.length() <= 5) {
                    super.insertString(offs, str, a);
                }
            }
        });
        
        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String numberText = numberInputField.getText().trim();
            boolean numberValid = validPERMNumber(numberText);

            if (numberValid) {
                JOptionPane.showMessageDialog(this, 
                    "Inputs are valid:\nNumber: " + numberText, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        
        // Add components to input panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(numberLabel, gbc);
        
        gbc.gridy = 1;
        inputPanel.add(numberInputField, gbc);
        
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(submitButton, gbc);
        
        // Add components to main panel
        panel.add(backButton, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel listCourseClasses() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Back button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Main"));
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // First input: 5-digit number
        JLabel numberLabel = new JLabel("Enter PERM Number (5 Digits):");
        numberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JTextField numberInputField = new JTextField(15);
        numberInputField.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Input validation - only allow digits and limit to 5 characters
        numberInputField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) 
                throws BadLocationException {
                if (str == null) return;
                
                // Only allow digits
                if (str.matches("\\d+") && getLength() + str.length() <= 5) {
                    super.insertString(offs, str, a);
                }
            }
        });
        
        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String numberText = numberInputField.getText().trim();
            boolean numberValid = validPERMNumber(numberText);

            if (numberValid) {
                JOptionPane.showMessageDialog(this, 
                    "Inputs are valid:\nNumber: " + numberText, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        
        // Add components to input panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(numberLabel, gbc);
        
        gbc.gridy = 1;
        inputPanel.add(numberInputField, gbc);
        
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(submitButton, gbc);
        
        // Add components to main panel
        panel.add(backButton, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel enterCourseGrades() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Back button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Main"));
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // First input: 5-digit number
        JLabel numberLabel = new JLabel("Enter PERM Number (5 Digits):");
        numberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JTextField numberInputField = new JTextField(15);
        numberInputField.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Input validation - only allow digits and limit to 5 characters
        numberInputField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) 
                throws BadLocationException {
                if (str == null) return;
                
                // Only allow digits
                if (str.matches("\\d+") && getLength() + str.length() <= 5) {
                    super.insertString(offs, str, a);
                }
            }
        });
        
        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String numberText = numberInputField.getText().trim();
            boolean numberValid = validPERMNumber(numberText);

            if (numberValid) {
                JOptionPane.showMessageDialog(this, 
                    "Inputs are valid:\nNumber: " + numberText, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        
        // Add components to input panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(numberLabel, gbc);
        
        gbc.gridy = 1;
        inputPanel.add(numberInputField, gbc);
        
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(submitButton, gbc);
        
        // Add components to main panel
        panel.add(backButton, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel requestTranscript() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Back button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Main"));
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // First input: 5-digit number
        JLabel numberLabel = new JLabel("Enter PERM Number (5 Digits):");
        numberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JTextField numberInputField = new JTextField(15);
        numberInputField.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Input validation - only allow digits and limit to 5 characters
        numberInputField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) 
                throws BadLocationException {
                if (str == null) return;
                
                // Only allow digits
                if (str.matches("\\d+") && getLength() + str.length() <= 5) {
                    super.insertString(offs, str, a);
                }
            }
        });
        
        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String numberText = numberInputField.getText().trim();
            boolean numberValid = validPERMNumber(numberText);

            if (numberValid) {
                JOptionPane.showMessageDialog(this, 
                    "Inputs are valid:\nNumber: " + numberText, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        
        // Add components to input panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(numberLabel, gbc);
        
        gbc.gridy = 1;
        inputPanel.add(numberInputField, gbc);
        
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(submitButton, gbc);
        
        // Add components to main panel
        panel.add(backButton, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel generateMailer() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Back button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Main"));
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // First input: 5-digit number
        JLabel numberLabel = new JLabel("Enter PERM Number (5 Digits):");
        numberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JTextField numberInputField = new JTextField(15);
        numberInputField.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Input validation - only allow digits and limit to 5 characters
        numberInputField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) 
                throws BadLocationException {
                if (str == null) return;
                
                // Only allow digits
                if (str.matches("\\d+") && getLength() + str.length() <= 5) {
                    super.insertString(offs, str, a);
                }
            }
        });
        
        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String numberText = numberInputField.getText().trim();
            boolean numberValid = validPERMNumber(numberText);

            if (numberValid) {
                JOptionPane.showMessageDialog(this, 
                    "Inputs are valid:\nNumber: " + numberText, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        
        // Add components to input panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(numberLabel, gbc);
        
        gbc.gridy = 1;
        inputPanel.add(numberInputField, gbc);
        
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(submitButton, gbc);
        
        // Add components to main panel
        panel.add(backButton, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private boolean validCourseCode(String course_code) {
        // Check if it's empty
        if (course_code.isEmpty()) {
            showError("Please enter a code");
            return false;
        }

        // Check if it matches the regex for course code
        if (!course_code.matches("^[A-Z]{2,4}[0-9]{1,3}$")) {
            showError("Code must:\n- Be 3 to 7 characters long\n- Start with 2 to 4 letters\n- End with 1 to 3 digits");
            return false;
        }

        return true;
    }
    
    private boolean validPERMNumber(String numberText) {
        // Check if it's empty
        if (numberText.isEmpty()) {
            showError("Please enter a number");
            return false;
        }

        // Check if it contains exactly 5 digits
        if (!numberText.matches("\\d{5}")) {
            showError("PERM number must be exactly 5 digits");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Invalid Input", JOptionPane.WARNING_MESSAGE);
    }
    
    private JPanel viewTablesPage() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Initialize components for tables page
        table = new JTable();
        tableSelector = new JComboBox<>(new String[]{
            "department", 
            "student_is_in", 
            "course_catalog", 
            "prerequisite", 
            "course_offering"
        });
        
        // Back button to return to main page
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Main"));
        
        // Layout for tables page
        JPanel topPanel = new JPanel();
        topPanel.add(backButton);
        topPanel.add(new JLabel("Select Table:"));
        topPanel.add(tableSelector);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        // Add listener for table selection
        tableSelector.addActionListener(e -> {
            String selectedTable = (String)tableSelector.getSelectedItem();
            loadTableData(selectedTable);
        });
        
        return panel;
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
            GUI gui = new GUI();
            gui.setVisible(true);
        });
    }
}
package motorph.system.cp2;

/**
 *
 * @ De Pano, Gatsola, Gonzales, Villamor | Group 15 H1101 
 */
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.File;

/**
 * Feature 1: Graphical User Interface (GUI) - MPHCR01
 * Implements the MotorPH payroll system using Java Swing
 * following key UI/UX design principles.
 */
public class MotorPHGUI extends JFrame {
    
    // Login Components
    private JPanel loginPanel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    // Requirement: Specific Input Fields
    private JTextField txtEmployeeId;     // Numeric input
    private JTextField txtEmployeeName;   // Text field (Auto-filled for Efficiency)
    private JComboBox<String> cbPayCoverage; // Changed from JTextField to JComboBox
    private JTextArea areaOutput;         // Result display
    private JButton btnCalculate, btnClear, btnExit, btnLogout;

    public MotorPHGUI() {
        // Configures main window settings
        setTitle("MotorPH Payroll Management System | MPHCR01");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new CardLayout());

        try {
            // Checks if required CSV files exist
            File empFile = new File(MotorPHSystemCP2.EMPLOYEE_DATA_FILE_PATH);
            File attFile = new File(MotorPHSystemCP2.ATTENDANCE_DATA_FILE_PATH);
            
            if (!empFile.exists() || !attFile.exists()) {
                JOptionPane.showMessageDialog(null, "Error: CSV data files are missing. Please check file paths.", "File Missing Error", JOptionPane.ERROR_MESSAGE);
            }

            DataManager.loadEmployeeData(MotorPHSystemCP2.EMPLOYEE_DATA_FILE_PATH);
            DataManager.loadAttendanceData(MotorPHSystemCP2.ATTENDANCE_DATA_FILE_PATH);
            
           // Checks if employee data loaded successfully
            if (DataManager.employeeList == null || DataManager.employeeList.size() == 0) {
                JOptionPane.showMessageDialog(null, "Warning: Employee database is empty. Check if CSV file is corrupted.", "Database Empty", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading system data files: " + e.getMessage(), "Loading Error", JOptionPane.ERROR_MESSAGE);
        }

        initLoginComponents(); // Initializes login screen first
        initComponents(); // Initializes original main components
        setupEventHandling();
        
        // Displays login screen on startup
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), "LOGIN");
        
        // Adjusts window size for login screen
        configureLoginWindowSize();
    }

    private void initLoginComponents() {
        // Build login panel
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(new TitledBorder(new LineBorder(new Color(79, 70, 229), 2), "MotorPH System Login", TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 16)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 12, 6, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        Font font = new Font("Segoe UI", Font.BOLD, 14);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(font);
        loginPanel.add(lblUser, gbc);

        gbc.gridx = 1;
        txtUsername = new JTextField(15);
        txtUsername.setFont(font);
        loginPanel.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(font);
        loginPanel.add(lblPass, gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(15);
        txtPassword.setFont(font);
        loginPanel.add(txtPassword, gbc);

        // Login Button
        gbc.gridx = 1; gbc.gridy = 2;
        btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(240, 240, 240));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFont(font);
        loginPanel.add(btnLogin, gbc);

        // Added login panel to card layout
        add(loginPanel, "LOGIN");
    }
    
    private void initComponents() {
        // Created the main system panel
        JPanel mainSystemPanel = new JPanel(new BorderLayout(20, 20));

        // Created the header section
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 41, 59)); // Deep Slate Blue
        JLabel lblHeader = new JLabel("Employee Payroll Processing");
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(lblHeader);
        mainSystemPanel.add(headerPanel, BorderLayout.NORTH);

        // Main Input Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

       // Shared label font
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);

        // Row 0: Employee ID (Numeric Input)
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblId = new JLabel("Employee Number:");
        lblId.setFont(labelFont);
        formPanel.add(lblId, gbc);

        gbc.gridx = 1;
        txtEmployeeId = new JTextField(15);
        txtEmployeeId.setToolTipText("Enter the numeric employee ID (e.g., 10001)"); // Clarity Tooltip
        formPanel.add(txtEmployeeId, gbc);

        // Row 1: Employee Name
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblName = new JLabel("Employee Name:");
        lblName.setFont(labelFont);
        formPanel.add(lblName, gbc);

        gbc.gridx = 1;
        txtEmployeeName = new JTextField();
        txtEmployeeName.setEditable(false); // UI Efficiency: Derived from ID
        txtEmployeeName.setBackground(new Color(241, 245, 249));
        formPanel.add(txtEmployeeName, gbc);

        // Row 2: Pay Coverage
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblCoverage = new JLabel("Pay Coverage Period:");
        lblCoverage.setFont(labelFont);
        formPanel.add(lblCoverage, gbc);

        gbc.gridx = 1;
        String[] months = {
            "June", 
            "July", 
            "August", 
            "September", 
            "October", 
            "November", 
            "December", 
            "Full Period (June - December Cumulative Summary)"
        };
        cbPayCoverage = new JComboBox<>(months);
        cbPayCoverage.setBackground(Color.WHITE);
        formPanel.add(cbPayCoverage, gbc);

        // Row 3: Action Buttons (Event Handling Targets)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCalculate = new JButton("Process Payroll");
        btnCalculate.setBackground(new Color(79, 70, 229));
        btnCalculate.setForeground(Color.BLACK);
        
        btnClear = new JButton("Clear Form");
        btnLogout = new JButton("Logout"); 
        btnExit = new JButton("Exit");

        actionPanel.add(btnCalculate);
        actionPanel.add(btnClear);
        actionPanel.add(btnLogout);
        actionPanel.add(btnExit);

        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(actionPanel, gbc);

        mainSystemPanel.add(formPanel, BorderLayout.CENTER);

        // Output Area
        areaOutput = new JTextArea(18, 50);
        areaOutput.setFont(new Font("Consolas", Font.PLAIN, 13));
        areaOutput.setEditable(false);
        areaOutput.setBorder(new TitledBorder("Payroll Calculation Summary"));
        JScrollPane scroll = new JScrollPane(areaOutput);
        
        mainSystemPanel.add(scroll, BorderLayout.SOUTH);
        add(mainSystemPanel, "MAIN_SYSTEM");
    }

    private void setupEventHandling() {
        // Registered button actions
        
        // Login Action Handling
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());

            // Validate login credentials
            if ((username.equals("employee") || username.equals("payroll_staff")) && password.equals(MotorPHSystemCP2.DEFAULT_PASSWORD)) {
                CardLayout cl = (CardLayout) getContentPane().getLayout();
                cl.show(getContentPane(), "MAIN_SYSTEM"); // Switch view to system panel
                
                setMinimumSize(null);
                setPreferredSize(null);
                
                pack();
                setLocationRelativeTo(null);
                
                JOptionPane.showMessageDialog(this, "Login successful. Welcome to MotorPH!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error: Incorrect Username and/or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Logout Action Handling
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Clear user inputs before logout
                txtUsername.setText("");
                txtPassword.setText("");
                txtEmployeeId.setText("");
                txtEmployeeName.setText("");
                cbPayCoverage.setSelectedIndex(0); // Reset dropdown selection
                areaOutput.setText("");
                
                CardLayout cl = (CardLayout) getContentPane().getLayout();
                cl.show(getContentPane(), "LOGIN"); // Switch back to login view
                
                // Re-apply the strict, compact constraints for the login view
                configureLoginWindowSize();
            }
        });
        
        // Calculate Button Action
        btnCalculate.addActionListener(e -> handleCalculation());

        // Clear Button Action
        btnClear.addActionListener(e -> {
            txtEmployeeId.setText("");
            txtEmployeeName.setText("");
            cbPayCoverage.setSelectedIndex(0); // Resets selector element indexes
            areaOutput.setText("");
            JOptionPane.showMessageDialog(this, "Form reset successfully.", "System Info", JOptionPane.INFORMATION_MESSAGE);
        });

        // Exit Button
        btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });

        // Allows only numeric employee IDs
        txtEmployeeId.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) e.consume(); // Only allow numbers
            }
        });
    }
    
    private void configureLoginWindowSize() {
        setMinimumSize(new Dimension(380, 220));
        setSize(380, 220);                      
        setLocationRelativeTo(null);
    }

    private void handleCalculation() {
        // Validates input and process payroll
        String empId = txtEmployeeId.getText().trim();

        // Checks for empty employee ID
        if (empId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Employee Number.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ensures employee data is available
        if (DataManager.employeeList == null) {
            JOptionPane.showMessageDialog(this, "Error: Employee list data is not available.", "System Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Finds employee record
            int index = DataManager.findEmployeeIndex(empId);

            // Checks for duplicate employee IDs
            int secondaryMatchCount = 0;
            for (int i = 0; i < DataManager.employeeList.size(); i++) {
                String[] currentRecord = DataManager.employeeList.get(i);
                if (currentRecord != null && currentRecord[0].equals(empId)) {
                    secondaryMatchCount++;
                }
            }
            if (secondaryMatchCount > 1) {
                JOptionPane.showMessageDialog(this, "System Note: Multiple entries discovered matching ID " + empId + ". Processing first record instance.", "Duplicate Record Warning", JOptionPane.WARNING_MESSAGE);
            }

            if (index == -1) {
                // Employee not found
                txtEmployeeName.setText("");
                JOptionPane.showMessageDialog(this, "Employee " + empId + " is not found in database.", "Not Found", JOptionPane.ERROR_MESSAGE);
            } else {
                // Displays employee name
                String[] details = DataManager.employeeList.get(index);
                
                // Validates employee record format
                if (details == null || details.length < 3) {
                    JOptionPane.showMessageDialog(this, "Error: Employee data row is corrupted or missing columns.", "Data Format Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                txtEmployeeName.setText(details[MotorPHSystemCP2.CSV_EMP_FIRST_NAME] + " " + details[MotorPHSystemCP2.CSV_EMP_LAST_NAME]);

                // Captures payroll report output
                captureCalculationOutput(index);
            }
       } catch (Exception ex) {
            // Handles unexpected errors
            JOptionPane.showMessageDialog(this, "An error occurred during verification: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void captureCalculationOutput(int index) {
        // Validates selected pay period
        int selectionIndex = cbPayCoverage.getSelectedIndex();
        if (selectionIndex < 0 || selectionIndex > 7) {
            JOptionPane.showMessageDialog(this, "Error: Invalid coverage month selection choice index.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream oldOut = System.out;
        
        try {
            System.setOut(ps);

            PayrollCalculator.generatePayrollReport(index, selectionIndex);

            System.out.flush();
        } catch (Exception ex) {
            // Handles payroll processing errors
            JOptionPane.showMessageDialog(this, "Error processing payroll recording track: " + ex.getMessage(), "Calculation Output Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Restores original console output
            System.setOut(oldOut);
        }

        String outputText = baos.toString();
        if (outputText == null || outputText.trim().equals("")) {
            areaOutput.setText("No information generated for selection parameters.");
            JOptionPane.showMessageDialog(this, "Warning: Report processing returned an empty summary output track.", "Empty Output Track Notice", JOptionPane.WARNING_MESSAGE);
        } else {
            areaOutput.setText(outputText);
            areaOutput.setCaretPosition(0);
            JOptionPane.showMessageDialog(this, "Payroll Processed Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Log look-and-feel setup errors
            System.err.println("System Look Feel Error: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            try {
                new MotorPHGUI().setVisible(true);
            } catch (Exception e) {
                // Log application startup errors
                System.err.println("Application Startup Execution Error: " + e.getMessage());
            }
        });
    }
}

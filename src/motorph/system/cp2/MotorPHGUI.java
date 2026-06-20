package motorph.system.cp2;

/**
 *
 * @ De Pano, Gatsola, Gonzales, Villamor | Group 15 H1101 
 */

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.HashMap;

public class MotorPHGUI extends JFrame {
    private double[][] yearlyPayrollCache = new double[13][7]; 
    private boolean isCalculated = false;
    private HashMap<String, String[]> payrollResults = new HashMap<>();
    private JPanel loginPanel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    private JTable tableEmployees;
    private DefaultTableModel tableModel;
    private JTextField txtSearchField;
    private JButton btnRefreshTable;

    private JLabel lblDetailsEmpNo;
    private JLabel lblDetailsFullName;
    private JLabel lblDetailsSSS;
    private JLabel lblDetailsPhilHealth;
    private JLabel lblDetailsTIN;
    private JLabel lblDetailsPagIbig;

    private JComboBox<String> cbSalaryMonth;
    private JLabel lblSalaryDailyRate;
    private JLabel lblSalaryDaysWorked;

    private JLabel lblSalaryGrossPay;
    private JLabel lblSalaryNetPay;
    private JLabel lblSssDeduction;
    private JLabel lblPhilHealthDeduction;
    private JLabel lblTinDeduction;
    private JLabel lblPagIbigDeduction;

    private JLabel lblStatusStrip;

    private JButton btnAddRecord;
    private JButton btnDeleteRecord;
    private JButton btnComputeSalaries;
    private JButton btnEditRecord;
    private JButton btnLogout;
    private JButton btnExit;

    private final Dimension SIZE_TOP_LEFT_PANEL = new Dimension(680, 330);
    private final Dimension SIZE_RIGHT_PANEL = new Dimension(310, 330);
    private final Dimension SIZE_BOTTOM_LEFT_PANEL = new Dimension(680, 180);
    private final Dimension SIZE_BOTTOM_RIGHT_PANEL = new Dimension(310, 180);

    private final Color COLOR_NAVY = new Color(30, 41, 59);
    private final Color COLOR_WHITE = Color.WHITE;
    private final Color COLOR_GREY_BG = new Color(241, 245, 249);
    private final Color COLOR_BORDER = new Color(203, 213, 225);
    private final Color COLOR_HEADER_SECONDARY = new Color(51, 65, 85);
    private final Color COLOR_BUTTON_PLAIN = new Color(226, 232, 240);

    private static final DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");

    public MotorPHGUI() {
        setTitle("MotorPH Employee Management & Payroll System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new CardLayout());

        try {
            DataManager.loadEmployeeData(MotorPHSystemCP2.EMPLOYEE_FILE_PATH);
            DataManager.loadAttendanceData(MotorPHSystemCP2.ATTENDANCE_FILE_PATH);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading data files: " + e.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE);
        }

        initLoginComponents();
        initComponents();
        setupEventHandling();

        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), "LOGIN");
        configureLoginWindowSize();
    }

    private void initLoginComponents() {
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(COLOR_WHITE);
        loginPanel.setBorder(new TitledBorder(new LineBorder(COLOR_NAVY, 2), "MotorPH Login System", TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        txtUsername = new JTextField(14);
        loginPanel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JPasswordField(14);
        loginPanel.add(txtPassword, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        btnLogin = new JButton("Login");
        btnLogin.setBackground(COLOR_BUTTON_PLAIN);
        btnLogin.setForeground(Color.BLACK);
        loginPanel.add(btnLogin, gbc);

        add(loginPanel, "LOGIN");
    }

    private void initComponents() {
        JPanel mainSystemWorkspace = new JPanel(new BorderLayout(10, 10));
        mainSystemWorkspace.setBackground(COLOR_GREY_BG);

        JPanel barHeader = new JPanel(new BorderLayout());
        barHeader.setBackground(COLOR_GREY_BG);
        barHeader.setBorder(new EmptyBorder(10, 15, 5, 15));

        JLabel lblHeaderTitle = new JLabel("MotorPH Employee Management & Payroll System");
        lblHeaderTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeaderTitle.setForeground(COLOR_NAVY);
        barHeader.add(lblHeaderTitle, BorderLayout.WEST);

        JPanel panelTopButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelTopButtons.setBackground(COLOR_GREY_BG);
        btnLogout = new JButton("Logout"); btnLogout.setBackground(COLOR_BUTTON_PLAIN); btnLogout.setForeground(Color.BLACK);
        btnExit = new JButton("Exit"); btnExit.setBackground(COLOR_BUTTON_PLAIN); btnExit.setForeground(Color.BLACK);
        panelTopButtons.add(btnLogout);
        panelTopButtons.add(btnExit);
        barHeader.add(panelTopButtons, BorderLayout.EAST);
        mainSystemWorkspace.add(barHeader, BorderLayout.NORTH);

        JPanel panelCenterWorkspace = new JPanel(new GridBagLayout());
        panelCenterWorkspace.setBackground(COLOR_GREY_BG);
        panelCenterWorkspace.setBorder(new EmptyBorder(0, 15, 5, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        JPanel panelLeftTableContainer = new JPanel(new BorderLayout(5, 5));
        panelLeftTableContainer.setBackground(COLOR_WHITE);
        panelLeftTableContainer.setBorder(new LineBorder(COLOR_BORDER, 1));
        panelLeftTableContainer.setPreferredSize(SIZE_TOP_LEFT_PANEL);
        panelLeftTableContainer.setMinimumSize(SIZE_TOP_LEFT_PANEL);
        panelLeftTableContainer.setMaximumSize(SIZE_TOP_LEFT_PANEL);

        JPanel panelSearchRowBar = new JPanel(new BorderLayout(5, 0));
        panelSearchRowBar.setBackground(COLOR_WHITE);
        panelSearchRowBar.setBorder(new EmptyBorder(6, 8, 2, 8));
        JLabel lblSearchTag = new JLabel("Search (ID or Name): ");
        lblSearchTag.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtSearchField = new JTextField();

        btnRefreshTable = new JButton("Refresh");
        btnRefreshTable.setBackground(COLOR_BUTTON_PLAIN);
        btnRefreshTable.setForeground(Color.BLACK);
        btnRefreshTable.setFont(new Font("Segoe UI", Font.BOLD, 11));

        panelSearchRowBar.add(lblSearchTag, BorderLayout.WEST);
        panelSearchRowBar.add(txtSearchField, BorderLayout.CENTER);
        panelSearchRowBar.add(btnRefreshTable, BorderLayout.EAST);
        panelLeftTableContainer.add(panelSearchRowBar, BorderLayout.NORTH);

        String[] headers = {"ID", "Last Name", "First Name", "SSS No.", "PhilHealth No.", "TIN", "Pag-IBIG No."};
        tableModel = new DefaultTableModel(headers, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableEmployees = new JTable(tableModel);
        tableEmployees.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableEmployees.getTableHeader().setReorderingAllowed(false);
        refreshTableData();

        JScrollPane scrollRegistry = new JScrollPane(tableEmployees);
        panelLeftTableContainer.add(scrollRegistry, BorderLayout.CENTER);

        JPanel panelTableActionButtons = new JPanel(new BorderLayout());
        panelTableActionButtons.setBackground(COLOR_WHITE);
        panelTableActionButtons.setBorder(new EmptyBorder(6, 8, 6, 8));

        JPanel panelTableLeftActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelTableLeftActions.setBackground(COLOR_WHITE);

        Font boldButtonFont = new Font("Segoe UI", Font.BOLD, 12);

        btnAddRecord = new JButton("Add"); btnAddRecord.setBackground(COLOR_BUTTON_PLAIN); btnAddRecord.setForeground(Color.BLACK);
        btnAddRecord.setFont(boldButtonFont);

        btnDeleteRecord = new JButton("Delete"); btnDeleteRecord.setBackground(COLOR_BUTTON_PLAIN); btnDeleteRecord.setForeground(Color.BLACK);
        btnDeleteRecord.setFont(boldButtonFont);

        panelTableLeftActions.add(btnAddRecord);
        panelTableLeftActions.add(btnDeleteRecord);
        panelTableActionButtons.add(panelTableLeftActions, BorderLayout.WEST);

        btnComputeSalaries = new JButton("Compute Salaries");
        btnComputeSalaries.setBackground(COLOR_BUTTON_PLAIN);
        btnComputeSalaries.setForeground(Color.BLACK);
        btnComputeSalaries.setFont(boldButtonFont);
        panelTableActionButtons.add(btnComputeSalaries, BorderLayout.EAST);
        panelLeftTableContainer.add(panelTableActionButtons, BorderLayout.SOUTH);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0; gbc.weighty = 0.0;
        panelCenterWorkspace.add(panelLeftTableContainer, gbc);

        JPanel panelRightDetailsCard = new JPanel(new BorderLayout(5, 5));
        panelRightDetailsCard.setBackground(COLOR_WHITE);
        panelRightDetailsCard.setBorder(new LineBorder(COLOR_BORDER, 1));
        panelRightDetailsCard.setPreferredSize(SIZE_RIGHT_PANEL);
        panelRightDetailsCard.setMinimumSize(SIZE_RIGHT_PANEL);
        panelRightDetailsCard.setMaximumSize(SIZE_RIGHT_PANEL);

        JPanel panelDetailsTitleHeader = new JPanel(new GridBagLayout());
        panelDetailsTitleHeader.setBackground(COLOR_NAVY);
        panelDetailsTitleHeader.setPreferredSize(new Dimension(310, 32));
        JLabel lblSectionTitleDetails = new JLabel("Employee Information", SwingConstants.CENTER);
        lblSectionTitleDetails.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSectionTitleDetails.setForeground(COLOR_WHITE);
        panelDetailsTitleHeader.add(lblSectionTitleDetails);
        panelRightDetailsCard.add(panelDetailsTitleHeader, BorderLayout.NORTH);

        JPanel panelDetailsDisplayFields = new JPanel(new GridLayout(6, 1, 4, 4));
        panelDetailsDisplayFields.setBackground(COLOR_WHITE);
        panelDetailsDisplayFields.setBorder(new EmptyBorder(5, 15, 5, 15));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);

        JPanel boxEmpId = createInformationBox();
        lblDetailsEmpNo = new JLabel("Employee ID: ---", SwingConstants.CENTER); lblDetailsEmpNo.setFont(labelFont); boxEmpId.add(lblDetailsEmpNo);

        JPanel boxFullName = createInformationBox();
        lblDetailsFullName = new JLabel("Full Name: ---", SwingConstants.CENTER); lblDetailsFullName.setFont(labelFont); boxFullName.add(lblDetailsFullName);

        JPanel boxSSS = createInformationBox();
        lblDetailsSSS = new JLabel("SSS: ---", SwingConstants.CENTER); lblDetailsSSS.setFont(labelFont); boxSSS.add(lblDetailsSSS);

        JPanel boxPhilHealth = createInformationBox();
        lblDetailsPhilHealth = new JLabel("PhilHealth No.: ---", SwingConstants.CENTER); lblDetailsPhilHealth.setFont(labelFont); boxPhilHealth.add(lblDetailsPhilHealth);

        JPanel boxTIN = createInformationBox();
        lblDetailsTIN = new JLabel("TIN: ---", SwingConstants.CENTER); lblDetailsTIN.setFont(labelFont); boxTIN.add(lblDetailsTIN);

        JPanel boxPagIbig = createInformationBox();
        lblDetailsPagIbig = new JLabel("Pag-IBIG No.: ---", SwingConstants.CENTER); lblDetailsPagIbig.setFont(labelFont); boxPagIbig.add(lblDetailsPagIbig);

        panelDetailsDisplayFields.add(boxEmpId);
        panelDetailsDisplayFields.add(boxFullName);
        panelDetailsDisplayFields.add(boxSSS);
        panelDetailsDisplayFields.add(boxPhilHealth);
        panelDetailsDisplayFields.add(boxTIN);
        panelDetailsDisplayFields.add(boxPagIbig);

        panelRightDetailsCard.add(panelDetailsDisplayFields, BorderLayout.CENTER);

        JPanel panelEditButtonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        panelEditButtonWrapper.setBackground(COLOR_WHITE);
        btnEditRecord = new JButton("Edit");
        btnEditRecord.setBackground(COLOR_BUTTON_PLAIN);
        btnEditRecord.setForeground(Color.BLACK);
        btnEditRecord.setFont(boldButtonFont);
        panelEditButtonWrapper.add(btnEditRecord);
        panelRightDetailsCard.add(panelEditButtonWrapper, BorderLayout.SOUTH);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.0; gbc.weighty = 0.0;
        panelCenterWorkspace.add(panelRightDetailsCard, gbc);

        JPanel panelBottomLeftPayrollSummary = new JPanel(new BorderLayout());
        panelBottomLeftPayrollSummary.setBackground(COLOR_WHITE);
        panelBottomLeftPayrollSummary.setBorder(new LineBorder(COLOR_BORDER, 1));
        panelBottomLeftPayrollSummary.setPreferredSize(SIZE_BOTTOM_LEFT_PANEL);
        panelBottomLeftPayrollSummary.setMinimumSize(SIZE_BOTTOM_LEFT_PANEL);
        panelBottomLeftPayrollSummary.setMaximumSize(SIZE_BOTTOM_LEFT_PANEL);

        JPanel panelPayrollTitleHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelPayrollTitleHeader.setBackground(COLOR_HEADER_SECONDARY);
        JLabel lblSectionTitlePayroll = new JLabel("Payroll Information");
        lblSectionTitlePayroll.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSectionTitlePayroll.setForeground(COLOR_WHITE);
        panelPayrollTitleHeader.add(lblSectionTitlePayroll);
        panelBottomLeftPayrollSummary.add(panelPayrollTitleHeader, BorderLayout.NORTH);

        JPanel panelPayrollMetricsLayoutGrid = new JPanel(new GridBagLayout());
        panelPayrollMetricsLayoutGrid.setBackground(COLOR_WHITE);
        panelPayrollMetricsLayoutGrid.setBorder(new EmptyBorder(8, 10, 8, 10));
        GridBagConstraints pGbc = new GridBagConstraints();
        pGbc.fill = GridBagConstraints.BOTH;
        pGbc.insets = new Insets(2, 4, 2, 4);

        Dimension sizePrimaryPayrollBox = new Dimension(220, 50);

        JPanel boxGross = new JPanel(new GridBagLayout()); boxGross.setBackground(COLOR_GREY_BG); boxGross.setBorder(new LineBorder(COLOR_BORDER, 1));
        boxGross.setPreferredSize(sizePrimaryPayrollBox); boxGross.setMinimumSize(sizePrimaryPayrollBox); boxGross.setMaximumSize(sizePrimaryPayrollBox);
        lblSalaryGrossPay = new JLabel("Gross Pay (GP): ---", SwingConstants.CENTER); lblSalaryGrossPay.setFont(labelFont);
        boxGross.add(lblSalaryGrossPay);

        JPanel boxNet = new JPanel(new GridBagLayout()); boxNet.setBackground(COLOR_GREY_BG); boxNet.setBorder(new LineBorder(COLOR_BORDER, 1));
        boxNet.setPreferredSize(sizePrimaryPayrollBox); boxNet.setMinimumSize(sizePrimaryPayrollBox); boxNet.setMaximumSize(sizePrimaryPayrollBox);
        lblSalaryNetPay = new JLabel("Net Pay (NP): ---", SwingConstants.CENTER); lblSalaryNetPay.setFont(labelFont);
        boxNet.add(lblSalaryNetPay);

        pGbc.gridx = 0; pGbc.gridy = 0; pGbc.weightx = 0.5; pGbc.weighty = 0.5;
        panelPayrollMetricsLayoutGrid.add(boxGross, pGbc);
        pGbc.gridy = 1;
        panelPayrollMetricsLayoutGrid.add(boxNet, pGbc);

        JPanel structuralSplitLine = new JPanel();
        structuralSplitLine.setBackground(COLOR_BORDER);
        structuralSplitLine.setPreferredSize(new Dimension(1, 115));
        structuralSplitLine.setMinimumSize(new Dimension(1, 115));
        structuralSplitLine.setMaximumSize(new Dimension(1, 115));
        pGbc.gridx = 1; pGbc.gridy = 0; pGbc.gridheight = 2; pGbc.weightx = 0.0; pGbc.weighty = 1.0;
        panelPayrollMetricsLayoutGrid.add(structuralSplitLine, pGbc);

        pGbc.gridheight = 1;
        JPanel panelRightDeductionGridContainer = new JPanel(new BorderLayout(0, 3));
        panelRightDeductionGridContainer.setBackground(COLOR_WHITE);
        JLabel lblDeductionsHeaderTag = new JLabel("Government Deductions", SwingConstants.CENTER); lblDeductionsHeaderTag.setFont(labelFont);
        panelRightDeductionGridContainer.add(lblDeductionsHeaderTag, BorderLayout.NORTH);

        JPanel panelDeductionsSubGridItems = new JPanel(new GridLayout(2, 2, 4, 4));
        panelDeductionsSubGridItems.setBackground(COLOR_WHITE);

        Dimension sizeDeductionBox = new Dimension(135, 42);

        JPanel subBoxSss = new JPanel(new GridBagLayout()); subBoxSss.setBackground(COLOR_GREY_BG); subBoxSss.setBorder(new LineBorder(COLOR_BORDER, 1));
        subBoxSss.setPreferredSize(sizeDeductionBox); subBoxSss.setMinimumSize(sizeDeductionBox); subBoxSss.setMaximumSize(sizeDeductionBox);
        lblSssDeduction = new JLabel("SSS: ---", SwingConstants.CENTER); lblSssDeduction.setFont(labelFont); subBoxSss.add(lblSssDeduction);

        JPanel subBoxTin = new JPanel(new GridBagLayout()); subBoxTin.setBackground(COLOR_GREY_BG); subBoxTin.setBorder(new LineBorder(COLOR_BORDER, 1));
        subBoxTin.setPreferredSize(sizeDeductionBox); subBoxTin.setMinimumSize(sizeDeductionBox); subBoxTin.setMaximumSize(sizeDeductionBox);
        lblTinDeduction = new JLabel("TIN: ---", SwingConstants.CENTER); lblTinDeduction.setFont(labelFont); subBoxTin.add(lblTinDeduction);

        JPanel subBoxPh = new JPanel(new GridBagLayout()); subBoxPh.setBackground(COLOR_GREY_BG); subBoxPh.setBorder(new LineBorder(COLOR_BORDER, 1));
        subBoxPh.setPreferredSize(sizeDeductionBox); subBoxPh.setMinimumSize(sizeDeductionBox); subBoxPh.setMaximumSize(sizeDeductionBox);
        lblPhilHealthDeduction = new JLabel("PhilHealth: ---", SwingConstants.CENTER); lblPhilHealthDeduction.setFont(labelFont); subBoxPh.add(lblPhilHealthDeduction);

        JPanel subBoxPag = new JPanel(new GridBagLayout()); subBoxPag.setBackground(COLOR_GREY_BG); subBoxPag.setBorder(new LineBorder(COLOR_BORDER, 1));
        subBoxPag.setPreferredSize(sizeDeductionBox); subBoxPag.setMinimumSize(sizeDeductionBox); subBoxPag.setMaximumSize(sizeDeductionBox);
        lblPagIbigDeduction = new JLabel("Pag-IBIG: ---", SwingConstants.CENTER); lblPagIbigDeduction.setFont(labelFont); subBoxPag.add(lblPagIbigDeduction);

        panelDeductionsSubGridItems.add(subBoxSss); panelDeductionsSubGridItems.add(subBoxTin);
        panelDeductionsSubGridItems.add(subBoxPh); panelDeductionsSubGridItems.add(subBoxPag);
        panelRightDeductionGridContainer.add(panelDeductionsSubGridItems, BorderLayout.CENTER);

        pGbc.gridx = 2; pGbc.gridy = 0; pGbc.gridheight = 2; pGbc.weightx = 0.5; pGbc.weighty = 1.0;
        panelPayrollMetricsLayoutGrid.add(panelRightDeductionGridContainer, pGbc);
        panelBottomLeftPayrollSummary.add(panelPayrollMetricsLayoutGrid, BorderLayout.CENTER);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0; gbc.weighty = 0.0;
        panelCenterWorkspace.add(panelBottomLeftPayrollSummary, gbc);

        JPanel panelBottomRightSalaryContext = new JPanel(new BorderLayout());
        panelBottomRightSalaryContext.setBackground(COLOR_WHITE);
        panelBottomRightSalaryContext.setBorder(new LineBorder(COLOR_BORDER, 1));
        panelBottomRightSalaryContext.setPreferredSize(SIZE_BOTTOM_RIGHT_PANEL);
        panelBottomRightSalaryContext.setMinimumSize(SIZE_BOTTOM_RIGHT_PANEL);
        panelBottomRightSalaryContext.setMaximumSize(SIZE_BOTTOM_RIGHT_PANEL);

        JPanel panelSalaryTitleHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSalaryTitleHeader.setBackground(COLOR_HEADER_SECONDARY);
        JLabel lblSectionTitleSalary = new JLabel("Salary Information");
        lblSectionTitleSalary.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSectionTitleSalary.setForeground(COLOR_WHITE);
        panelSalaryTitleHeader.add(lblSectionTitleSalary);
        panelBottomRightSalaryContext.add(panelSalaryTitleHeader, BorderLayout.NORTH);

        JPanel panelSalarySelectionInputGrid = new JPanel(new GridLayout(3, 1, 4, 4));
        panelSalarySelectionInputGrid.setBackground(COLOR_WHITE);
        panelSalarySelectionInputGrid.setBorder(new EmptyBorder(6, 15, 6, 15));

        JPanel boxDaily = new JPanel(new GridBagLayout()); boxDaily.setBackground(COLOR_GREY_BG); boxDaily.setBorder(new LineBorder(COLOR_BORDER, 1));
        lblSalaryDailyRate = new JLabel("Daily Rate: PHP 0.00", SwingConstants.CENTER); lblSalaryDailyRate.setFont(labelFont); boxDaily.add(lblSalaryDailyRate);

        JPanel boxDays = new JPanel(new GridBagLayout()); boxDays.setBackground(COLOR_GREY_BG); boxDays.setBorder(new LineBorder(COLOR_BORDER, 1));
        lblSalaryDaysWorked = new JLabel("Days Worked: 0 days", SwingConstants.CENTER); lblSalaryDaysWorked.setFont(labelFont); boxDays.add(lblSalaryDaysWorked);

        JPanel panelDropdownSelectorRow = new JPanel(new BorderLayout(5, 0));
        panelDropdownSelectorRow.setBackground(COLOR_WHITE);
        JLabel lblMonthTag = new JLabel("Month Selection: "); lblMonthTag.setFont(labelFont);
        panelDropdownSelectorRow.add(lblMonthTag, BorderLayout.WEST);

        String[] monthsList = {"June", "July", "August", "September", "October", "November", "December"};
        cbSalaryMonth = new JComboBox<>(monthsList); cbSalaryMonth.setBackground(COLOR_WHITE);

        cbSalaryMonth.setPreferredSize(new Dimension(130, 24));
        cbSalaryMonth.setMaximumSize(new Dimension(130, 24));
        panelDropdownSelectorRow.add(cbSalaryMonth, BorderLayout.CENTER);

        panelSalarySelectionInputGrid.add(boxDaily);
        panelSalarySelectionInputGrid.add(boxDays);
        panelSalarySelectionInputGrid.add(panelDropdownSelectorRow);
        panelBottomRightSalaryContext.add(panelSalarySelectionInputGrid, BorderLayout.CENTER);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.0; gbc.weighty = 0.0;
        panelCenterWorkspace.add(panelBottomRightSalaryContext, gbc);

        mainSystemWorkspace.add(panelCenterWorkspace, BorderLayout.CENTER);

        lblStatusStrip = new JLabel(" Status: System Ready.");
        lblStatusStrip.setBorder(new BevelBorder(BevelBorder.LOWERED));
        lblStatusStrip.setPreferredSize(new Dimension(getWidth(), 22));
        mainSystemWorkspace.add(lblStatusStrip, BorderLayout.SOUTH);

        add(mainSystemWorkspace, "MAIN_SYSTEM");
    }

    private JPanel createInformationBox() {
        JPanel box = new JPanel(new GridBagLayout());
        box.setBackground(COLOR_GREY_BG);
        box.setBorder(new LineBorder(COLOR_BORDER, 1));
        return box;
    }

    private void setupEventHandling() {
        btnLogin.addActionListener(e -> {
            String u = txtUsername.getText().trim();
            String p = new String(txtPassword.getPassword());

            if ((u.equals("employee") || u.equals("payroll_staff")) && p.equals("12345")) {
                CardLayout cl = (CardLayout) getContentPane().getLayout();
                cl.show(getContentPane(), "MAIN_SYSTEM");
                setMinimumSize(null);
                pack();
                setSize(1035, 650);
                setLocationRelativeTo(null);
                lblStatusStrip.setText(" Status: System Ready | Workspace Active for " + u + ".");
                JOptionPane.showMessageDialog(this, "Login successful. Welcome to MotorPH!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error: Incorrect Username and/or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        txtSearchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                executeLiveFilteringRegistryDataLookup();
            }
        });

        btnRefreshTable.addActionListener(e -> {
            txtSearchField.setText("");
            refreshTableData();
            isCalculated = false;
            clearDashboardMetricsDisplayBoxes();
            lblStatusStrip.setText(" Status: Table records successfully refreshed.");
        });

        tableEmployees.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                syncSelectionDetailsToDashboardFields();
            }
        });

        cbSalaryMonth.addActionListener(e -> syncSelectionDetailsToDashboardFields());
        btnAddRecord.addActionListener(e -> spawnCreationFormPopupFrameWindow());
        btnEditRecord.addActionListener(e -> spawnModificationFormPopupFrameWindow());
        btnDeleteRecord.addActionListener(e -> executeDataDeletionWorkflowSequence());
        btnComputeSalaries.addActionListener(e -> executeSalaryCalculationModuleEngineRoutine());

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                txtUsername.setText("");
                txtPassword.setText("");
                txtSearchField.setText("");
                clearDashboardMetricsDisplayBoxes();
                CardLayout cl = (CardLayout) getContentPane().getLayout();
                cl.show(getContentPane(), "LOGIN");
                configureLoginWindowSize();
            }
        });

        btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });
    }

    private void configureLoginWindowSize() {
        setMinimumSize(new Dimension(380, 220));
        setSize(380, 220);
        setLocationRelativeTo(null);
    }

    private void refreshTableData() {
        tableModel.setRowCount(0);
        if (DataManager.employeeList != null) {
            for (String[] emp : DataManager.employeeList) {
                tableModel.addRow(new String[]{
                    emp[MotorPHSystemCP2.CSV_EMP_ID], 
                    emp[MotorPHSystemCP2.CSV_EMP_LAST_NAME], 
                    emp[MotorPHSystemCP2.CSV_EMP_FIRST_NAME], 
                    emp[MotorPHSystemCP2.CSV_EMP_SSS], 
                    emp[MotorPHSystemCP2.CSV_EMP_PHILHEALTH], 
                    emp[MotorPHSystemCP2.CSV_EMP_TIN], 
                    emp[MotorPHSystemCP2.CSV_EMP_PAGIBIG]
                });
            }
        }
    }

    private void executeLiveFilteringRegistryDataLookup() {
        String filterText = txtSearchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        if (DataManager.employeeList == null) return;

        for (String[] emp : DataManager.employeeList) {
            String id = emp[MotorPHSystemCP2.CSV_EMP_ID].toLowerCase();
            String ln = emp[MotorPHSystemCP2.CSV_EMP_LAST_NAME].toLowerCase();
            String fn = emp[MotorPHSystemCP2.CSV_EMP_FIRST_NAME].toLowerCase();

            if (filterText.isEmpty() || id.contains(filterText) || ln.contains(filterText) || fn.contains(filterText)) {
                tableModel.addRow(new String[]{
                    emp[MotorPHSystemCP2.CSV_EMP_ID], emp[MotorPHSystemCP2.CSV_EMP_LAST_NAME], emp[MotorPHSystemCP2.CSV_EMP_FIRST_NAME], 
                    emp[MotorPHSystemCP2.CSV_EMP_SSS], emp[MotorPHSystemCP2.CSV_EMP_PHILHEALTH], emp[MotorPHSystemCP2.CSV_EMP_TIN], emp[MotorPHSystemCP2.CSV_EMP_PAGIBIG]
                });
            }
        }
    }

    private void syncSelectionDetailsToDashboardFields() {
        int r = tableEmployees.getSelectedRow();
        if (r == -1) {
            clearDashboardMetricsDisplayBoxes();
            return;
        }

        String id = (String) tableModel.getValueAt(r, 0);
        int idx = DataManager.findEmployeeIndex(id);

        if (idx != -1) {
            String[] emp = DataManager.employeeList.get(idx);
            
            lblDetailsEmpNo.setText("Employee ID: " + emp[MotorPHSystemCP2.CSV_EMP_ID]);
            lblDetailsFullName.setText("Full Name: " + emp[MotorPHSystemCP2.CSV_EMP_FIRST_NAME] + " " + emp[MotorPHSystemCP2.CSV_EMP_LAST_NAME]);
            lblDetailsSSS.setText("SSS: " + emp[MotorPHSystemCP2.CSV_EMP_SSS]);
            lblDetailsPhilHealth.setText("PhilHealth No.: " + emp[MotorPHSystemCP2.CSV_EMP_PHILHEALTH]);
            lblDetailsTIN.setText("TIN: " + emp[MotorPHSystemCP2.CSV_EMP_TIN]);
            lblDetailsPagIbig.setText("Pag-IBIG No.: " + emp[MotorPHSystemCP2.CSV_EMP_PAGIBIG]);

            double basicRate;
try { 
    String cleanSalary = emp[MotorPHSystemCP2.CSV_EMP_BASIC_SALARY].replace(",", "").trim();
    basicRate = Double.parseDouble(cleanSalary); 
} catch (Exception e) { 
    basicRate = 0.0; 
}
lblSalaryDailyRate.setText("Daily Rate: PHP " + moneyFormat.format(basicRate / 21.0));

            int dropIndex = cbSalaryMonth.getSelectedIndex();
            int monthNum = dropIndex + 6;
            int maxDay = (monthNum == 6 || monthNum == 9 || monthNum == 11) ? 30 : 31;
            
double h1 = AttendanceManager.getTotalHoursWorked(id.trim(), monthNum, 1, 15);
            double h2 = AttendanceManager.getTotalHoursWorked(id.trim(), monthNum, 16, maxDay);
            double exactDays = (h1 + h2) / 8.0;
            
            lblSalaryDaysWorked.setText("Days Worked: " + String.format("%.2f", exactDays) + " days");

            if (!isCalculated) {
                resetPayrollMetricsTextDisplayFields();
            } else {
                String currentLookupKey = id.trim() + "_" + monthNum;
                
                if (payrollResults != null && payrollResults.containsKey(currentLookupKey)) {
                    String[] calc = payrollResults.get(currentLookupKey);
                    
                    lblSalaryGrossPay.setText("Gross Pay (GP): PHP " + calc[0]);
                    lblSalaryNetPay.setText("Net Pay (NP): PHP " + calc[1]);
                    lblSssDeduction.setText("SSS: PHP " + calc[2]);
                    lblPhilHealthDeduction.setText("PhilHealth: PHP " + calc[3]);
                    lblTinDeduction.setText("TIN: PHP " + calc[4]);
                    lblPagIbigDeduction.setText("Pag-IBIG: PHP " + calc[5]);
                } else {
                    resetPayrollMetricsTextDisplayFields();
                }
            }
            
            lblStatusStrip.setText(" Status: Ready | Row " + (r + 1) + " (ID " + id + ") Synced for Month " + monthNum + ".");
        }
    }

private void executeSalaryCalculationModuleEngineRoutine() {
        payrollResults.clear();
        if (DataManager.employeeList == null) return;

        for (String[] emp : DataManager.employeeList) {
            String id = emp[MotorPHSystemCP2.CSV_EMP_ID].trim();
            for (int m = 6; m <= 12; m++) {
                int maxDay = (m == 6 || m == 9 || m == 11) ? 30 : 31;
                
                double[] calculations = PayrollCalculator.calculateCompletePayroll(id, m, 1, maxDay);

                String uniqueKey = id + "_" + m;
                payrollResults.put(uniqueKey, new String[] {
                    moneyFormat.format(calculations[0]), 
                    moneyFormat.format(calculations[6]), 
                    moneyFormat.format(calculations[1]), 
                    moneyFormat.format(calculations[2]), 
                    moneyFormat.format(calculations[4]), 
                    moneyFormat.format(calculations[3])  
                });
            }
        }

        isCalculated = true;

        syncSelectionDetailsToDashboardFields();
        JOptionPane.showMessageDialog(this, "All months processed successfully. You can now switch months seamlessly!", "Calculation Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel buildTwoSectionFormPanel(
        JTextField fId, JTextField fLn, JTextField fFn, JTextField fBd, JTextField fAdd, JTextField fPh, JTextField fSss, JTextField fPhl, JTextField fTin, JTextField fPag,
        JTextField fStat, JTextField fPos, JTextField fSup, JTextField fSal, JTextField fRice, JTextField fPhone, JTextField fCloth, JTextField fGrossSemi, JTextField fHr,
        boolean isEditMode, String targetId
    ) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setPreferredSize(new Dimension(680, 480));

        JPanel topSection = new JPanel(new GridLayout(5, 2, 8, 5));
        topSection.setBorder(BorderFactory.createTitledBorder("Personal Profile Data"));

        if (isEditMode) {
            topSection.add(new JLabel("Employee ID:"));
            topSection.add(new JLabel(targetId));
        } else {
            topSection.add(new JLabel("Employee ID:"));
            topSection.add(fId);
        }
        topSection.add(new JLabel("Last Name:")); topSection.add(fLn);
        topSection.add(new JLabel("First Name:")); topSection.add(fFn);
        topSection.add(new JLabel("Birthday (MM/DD/YYYY):")); topSection.add(fBd);
        topSection.add(new JLabel("Address:")); topSection.add(fAdd);
        topSection.add(new JLabel("Phone Number:")); topSection.add(fPh);
        topSection.add(new JLabel("SSS No (12 Digits):")); topSection.add(fSss);
        topSection.add(new JLabel("PhilHealth No (12 Digits):")); topSection.add(fPhl);
        topSection.add(new JLabel("TIN No (9-12 Digits):")); topSection.add(fTin);
        topSection.add(new JLabel("Pag-IBIG No (12 Digits):")); topSection.add(fPag);

        JPanel bottomSection = new JPanel(new GridLayout(5, 2, 8, 5));
        bottomSection.setBorder(BorderFactory.createTitledBorder("Compensation Rules"));

        bottomSection.add(new JLabel("Status:")); bottomSection.add(fStat);
        bottomSection.add(new JLabel("Position:")); bottomSection.add(fPos);
        bottomSection.add(new JLabel("Immediate Supervisor:")); bottomSection.add(fSup);
        bottomSection.add(new JLabel("Basic Salary:")); bottomSection.add(fSal);
        bottomSection.add(new JLabel("Rice Subsidy:")); bottomSection.add(fRice);
        bottomSection.add(new JLabel("Phone Allowance:")); bottomSection.add(fPhone);
        bottomSection.add(new JLabel("Clothing Allowance:")); bottomSection.add(fCloth);
        bottomSection.add(new JLabel("Gross Semi-Monthly Rate:")); bottomSection.add(fGrossSemi);
        bottomSection.add(new JLabel("Hourly Rate:")); bottomSection.add(fHr);

        container.add(topSection);
        container.add(Box.createVerticalStrut(10));
        container.add(new JSeparator(JSeparator.HORIZONTAL));
        container.add(Box.createVerticalStrut(10));
        container.add(bottomSection);

        return container;
    }

    private void spawnCreationFormPopupFrameWindow() {
        JTextField fId = new JTextField(); JTextField fLn = new JTextField(); JTextField fFn = new JTextField();
        JTextField fBd = new JTextField(); JTextField fAdd = new JTextField(); JTextField fPh = new JTextField();
        JTextField fSss = new JTextField(); JTextField fPhl = new JTextField(); JTextField fTin = new JTextField();
        JTextField fPag = new JTextField();
        
        JTextField fStat = new JTextField(); JTextField fPos = new JTextField(); JTextField fSup = new JTextField();
        
        JTextField fSal = new JTextField(); JTextField fRice = new JTextField(); JTextField fPhone = new JTextField();
        JTextField fCloth = new JTextField(); JTextField fGrossSemi = new JTextField(); JTextField fHr = new JTextField();

        applyStrictDigitFilter(fId, 0, false);     
        applyStrictDigitFilter(fPh, 0, false);     
        applyStrictDigitFilter(fSss, 12, false);   
        applyStrictDigitFilter(fPhl, 12, false);   
        applyStrictDigitFilter(fPag, 12, false);   
        applyStrictDigitFilter(fTin, 12, false);   
        
        applyStrictDigitFilter(fSal, 0, false);
        applyStrictDigitFilter(fRice, 0, false);
        applyStrictDigitFilter(fPhone, 0, false);
        applyStrictDigitFilter(fCloth, 0, false);
        applyStrictDigitFilter(fGrossSemi, 0, false);
        applyStrictDigitFilter(fHr, 0, true);      

        JPanel formWrapper = buildTwoSectionFormPanel(
            fId, fLn, fFn, fBd, fAdd, fPh, fSss, fPhl, fTin, fPag,
            fStat, fPos, fSup, fSal, fRice, fPhone, fCloth, fGrossSemi, fHr,
            false, ""
        );

        int result = JOptionPane.showConfirmDialog(this, formWrapper, "Add Employee Record", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String inputId = fId.getText().trim();
            if (inputId.isEmpty() || fLn.getText().trim().isEmpty() || fFn.getText().trim().isEmpty() ||
                fSss.getText().trim().length() != 12 || fPhl.getText().trim().length() != 12 || fPag.getText().trim().length() != 12) {
                JOptionPane.showMessageDialog(this, "Error: Missing inputs, or identifiers do not have exactly 12 digits.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int tinLen = fTin.getText().trim().length();
            if (tinLen < 9 || tinLen > 12) {
                JOptionPane.showMessageDialog(this, "Error: TIN must be between 9 and 12 digits long.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (DataManager.isEmployeeIdExists(inputId)) {
                JOptionPane.showMessageDialog(this, "Error: Employee ID already exists.", "Duplication Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String valSal = fSal.getText().trim().isEmpty() ? "0" : fSal.getText().trim();
            String valRice = fRice.getText().trim().isEmpty() ? "0" : fRice.getText().trim();
            String valPhone = fPhone.getText().trim().isEmpty() ? "0" : fPhone.getText().trim();
            String valCloth = fCloth.getText().trim().isEmpty() ? "0" : fCloth.getText().trim();
            String valGrossSemi = fGrossSemi.getText().trim().isEmpty() ? "0" : fGrossSemi.getText().trim();
            String valHr = fHr.getText().trim().isEmpty() ? "0.0" : fHr.getText().trim();

            String[] newRecord = {
                inputId, fLn.getText().trim(), fFn.getText().trim(), fBd.getText().trim(), fAdd.getText().trim(), fPh.getText().trim(),
                fSss.getText().trim(), fPhl.getText().trim(), fTin.getText().trim(), fPag.getText().trim(), fStat.getText().trim(), fPos.getText().trim(),
                fSup.getText().trim(), valSal, valRice, valPhone, valCloth, valGrossSemi, valHr
            };

            DataManager.appendEmployeeRecord(MotorPHSystemCP2.EMPLOYEE_FILE_PATH, newRecord);
            refreshTableData();
            JOptionPane.showMessageDialog(this, "Record created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void spawnModificationFormPopupFrameWindow() {
        int r = tableEmployees.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee record row first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String targetId = (String) tableModel.getValueAt(r, 0);
        int idx = DataManager.findEmployeeIndex(targetId);
        if (idx == -1) return;

        String[] emp = DataManager.employeeList.get(idx);

        JTextField fLn = new JTextField(emp[1]); JTextField fFn = new JTextField(emp[2]); JTextField fBd = new JTextField(emp[3]);
        JTextField fAdd = new JTextField(emp[4]); JTextField fPh = new JTextField(emp[5]); JTextField fSss = new JTextField(emp[6]);
        JTextField fPhl = new JTextField(emp[7]); JTextField fTin = new JTextField(emp[8]); JTextField fPag = new JTextField(emp[9]);
        
        JTextField fStat = new JTextField(emp[10]); JTextField fPos = new JTextField(emp[11]); JTextField fSup = new JTextField(emp[12]);
        JTextField fSal = new JTextField(emp[13]); JTextField fRice = new JTextField(emp[14]); JTextField fPhone = new JTextField(emp[15]);
        JTextField fCloth = new JTextField(emp[16]); JTextField fGrossSemi = new JTextField(emp[17]); JTextField fHr = new JTextField(emp[18]);

        applyStrictDigitFilter(fPh, 0, false);
        applyStrictDigitFilter(fSss, 12, false);
        applyStrictDigitFilter(fPhl, 12, false);
        applyStrictDigitFilter(fPag, 12, false);
        applyStrictDigitFilter(fTin, 12, false);
        
        applyStrictDigitFilter(fSal, 0, false);
        applyStrictDigitFilter(fRice, 0, false);
        applyStrictDigitFilter(fPhone, 0, false);
        applyStrictDigitFilter(fCloth, 0, false);
        applyStrictDigitFilter(fGrossSemi, 0, false);
        applyStrictDigitFilter(fHr, 0, true);

        JPanel formWrapper = buildTwoSectionFormPanel(
            null, fLn, fFn, fBd, fAdd, fPh, fSss, fPhl, fTin, fPag,
            fStat, fPos, fSup, fSal, fRice, fPhone, fCloth, fGrossSemi, fHr,
            true, targetId
        );

        int result = JOptionPane.showConfirmDialog(this, formWrapper, "Update Employee Record", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            if (fLn.getText().trim().isEmpty() || fFn.getText().trim().isEmpty() ||
                fSss.getText().trim().length() != 12 || fPhl.getText().trim().length() != 12 || fPag.getText().trim().length() != 12) {
                JOptionPane.showMessageDialog(this, "Error: Names cannot be empty, and identification codes require 12 digits.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int tinLen = fTin.getText().trim().length();
            if (tinLen < 9 || tinLen > 12) {
                JOptionPane.showMessageDialog(this, "Error: TIN must be between 9 and 12 digits.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            emp[1] = fLn.getText().trim(); emp[2] = fFn.getText().trim(); emp[3] = fBd.getText().trim();
            emp[4] = fAdd.getText().trim(); emp[5] = fPh.getText().trim(); emp[6] = fSss.getText().trim();
            emp[7] = fPhl.getText().trim(); emp[8] = fTin.getText().trim(); emp[9] = fPag.getText().trim();
            emp[10] = fStat.getText().trim(); emp[11] = fPos.getText().trim(); emp[12] = fSup.getText().trim();
            
            emp[13] = fSal.getText().trim().isEmpty() ? "0" : fSal.getText().trim();
            emp[14] = fRice.getText().trim().isEmpty() ? "0" : fRice.getText().trim();
            emp[15] = fPhone.getText().trim().isEmpty() ? "0" : fPhone.getText().trim();
            emp[16] = fCloth.getText().trim().isEmpty() ? "0" : fCloth.getText().trim();
            emp[17] = fGrossSemi.getText().trim().isEmpty() ? "0" : fGrossSemi.getText().trim();
            emp[18] = fHr.getText().trim().isEmpty() ? "0.0" : fHr.getText().trim();

            DataManager.updateEmployeeRecord(MotorPHSystemCP2.EMPLOYEE_FILE_PATH, idx, emp);
            refreshTableData();
            syncSelectionDetailsToDashboardFields();
            JOptionPane.showMessageDialog(this, "Record updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void executeDataDeletionWorkflowSequence() {
        int r = tableEmployees.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee record to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String targetId = (String) tableModel.getValueAt(r, 0);
        int idx = DataManager.findEmployeeIndex(targetId);
        if (idx == -1) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Employee " + targetId + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            DataManager.deleteEmployeeRecord(MotorPHSystemCP2.EMPLOYEE_FILE_PATH, idx);
            refreshTableData();
            clearDashboardMetricsDisplayBoxes();
            JOptionPane.showMessageDialog(this, "Record successfully removed.", "Deletion Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void applyStrictDigitFilter(JTextField field, int limit, boolean allowDecimal) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (isValidInput(fb.getDocument().getText(0, fb.getDocument().getLength()) + string, limit, allowDecimal)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String nextText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
                if (isValidInput(nextText, limit, allowDecimal)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean isValidInput(String fullResultText, int characterMax, boolean decimalMode) {
                if (fullResultText.isEmpty()) return true;
                if (characterMax > 0 && fullResultText.length() > characterMax) return false;
                if (decimalMode) {
                    return fullResultText.matches("\\d*\\.?\\d*");
                } else {
                    return fullResultText.matches("\\d+");
                }
            }
        });
    }

    private void resetPayrollMetricsTextDisplayFields() {
        lblSalaryGrossPay.setText("Gross Pay (GP): ---");
        lblSalaryNetPay.setText("Net Pay (NP): ---");
        lblSssDeduction.setText("SSS: ---");
        lblPhilHealthDeduction.setText("PhilHealth: ---");
        lblTinDeduction.setText("TIN: ---");
        lblPagIbigDeduction.setText("Pag-IBIG: ---");
    }

    private void clearDashboardMetricsDisplayBoxes() {
        tableEmployees.clearSelection();
        lblDetailsEmpNo.setText("Employee ID: ---");
        lblDetailsFullName.setText("Full Name: ---");
        lblDetailsSSS.setText("SSS: ---");
        lblDetailsPhilHealth.setText("PhilHealth No.: ---");
        lblDetailsTIN.setText("TIN: ---");
        lblDetailsPagIbig.setText("Pag-IBIG No.: ---");
        lblSalaryDailyRate.setText("Daily Rate: PHP 0.00");
        lblSalaryDaysWorked.setText("Days Worked: 0 days");
        resetPayrollMetricsTextDisplayFields();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MotorPHGUI().setVisible(true);
        });
    }
}

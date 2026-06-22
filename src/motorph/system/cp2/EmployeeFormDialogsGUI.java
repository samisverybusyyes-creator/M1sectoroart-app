package motorph.system.cp2;

/**
 *
 * @ De Pano, Gatsola, Gonzales, Villamor | Group 15 H1101
 */

import javax.swing.*;
import java.awt.*;

// Manages the graphical user interface popups for creating and modifying employee records.

public class EmployeeFormDialogsGUI {

    /**
     * Displays a modal dialog window to create a new employee record.
     * Automatically calculates the next available employee identifier.
     * Applies real-time data input filters and validates required data before saving.
     */
    
    public static void spawnCreationFormPopupFrameWindow(MotorPHGUI parent) {
        int highestId = 0;
        // Scans current list records to determine the next available incremental primary key ID.
        if (DataManager.employeeList != null) {
            for (String[] emp : DataManager.employeeList) {
                try {
                    int currentId = Integer.parseInt(emp[MotorPHSystemCP2.CSV_EMP_ID].trim());
                    if (currentId > highestId) {
                        highestId = currentId;
                    }
                } catch (Exception e) {}
            }
        }
        String autoAssignedId = String.valueOf(highestId + 1);
        JLabel lblAutoId = new JLabel(autoAssignedId);

        JTextField fLn = new JTextField(); 
        JTextField fFn = new JTextField(); 

        JPanel datePanel = new JPanel(new GridLayout(1, 3, 5, 0));
        datePanel.setOpaque(false);
        
        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        JComboBox<String> cbMonth = new JComboBox<>(months);
        JComboBox<String> cbDay = new JComboBox<>();
        
        int currentYr = java.time.Year.now().getValue();
        String[] years = new String[100];
        for (int i = 0; i < 100; i++) years[i] = String.valueOf(currentYr - i);
        JComboBox<String> cbYear = new JComboBox<>(years);

        DefaultListCellRenderer centerRenderer = new DefaultListCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
        cbMonth.setRenderer(centerRenderer);
        cbDay.setRenderer(centerRenderer);
        cbYear.setRenderer(centerRenderer);

        // Dynamically populates day combo box items based on selected calendar month and year rules.
        Runnable updateDays = () -> {
            int m = Integer.parseInt((String) cbMonth.getSelectedItem());
            int y = Integer.parseInt((String) cbYear.getSelectedItem());
            int maxDays = java.time.YearMonth.of(y, m).lengthOfMonth();
            
            Object currentSelectedDay = cbDay.getSelectedItem();
            cbDay.removeAllItems();
            for (int i = 1; i <= maxDays; i++) {
                cbDay.addItem(String.format("%02d", i));
            }
            if (currentSelectedDay != null && Integer.parseInt((String)currentSelectedDay) <= maxDays) {
                cbDay.setSelectedItem(currentSelectedDay);
            }
        };
        
        cbMonth.addActionListener(e -> updateDays.run());
        cbYear.addActionListener(e -> updateDays.run());
        updateDays.run(); 

        datePanel.add(cbMonth);
        datePanel.add(cbDay);
        datePanel.add(cbYear);

        JTextField fAdd = new JTextField(); JTextField fPh = new JTextField();
        JTextField fSss = new JTextField(); JTextField fPhl = new JTextField(); JTextField fTin = new JTextField();
        JTextField fPag = new JTextField();
        JTextField fStat = new JTextField(); JTextField fPos = new JTextField(); JTextField fSup = new JTextField();
        JTextField fSal = new JTextField(); JTextField fRice = new JTextField(); JTextField fPhone = new JTextField();
        JTextField fCloth = new JTextField(); JTextField fGrossSemi = new JTextField(); JTextField fHr = new JTextField();

        // Enforces specific system length, decimal, and digit patterns on form entry components.
        FieldValidatorGUI.applyStrictDigitFilter(fPhl, 12, false);   
        FieldValidatorGUI.applyStrictDigitFilter(fPag, 12, false);   
        FieldValidatorGUI.applyStrictDigitFilter(fSal, 0, false);
        FieldValidatorGUI.applyStrictDigitFilter(fRice, 0, false);
        FieldValidatorGUI.applyStrictDigitFilter(fPhone, 0, false);
        FieldValidatorGUI.applyStrictDigitFilter(fCloth, 0, false);
        FieldValidatorGUI.applyStrictDigitFilter(fGrossSemi, 0, false);
        FieldValidatorGUI.applyStrictDigitFilter(fHr, 0, true);      

        FieldValidatorGUI.applyAutoHyphenFilter(fPh, "PHONE");
        FieldValidatorGUI.applyAutoHyphenFilter(fSss, "SSS");
        FieldValidatorGUI.applyAutoHyphenFilter(fTin, "TIN");

        JPanel formWrapper = buildTwoSectionFormPanel(
            lblAutoId, fLn, fFn, datePanel, fAdd, fPh, fSss, fPhl, fTin, fPag, 
            fStat, fPos, fSup, fSal, fRice, fPhone, fCloth, fGrossSemi, fHr
        );

        JDialog dialog = new JDialog(parent, "Add Employee Record", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(formWrapper, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnSave = new JButton("Save Record");
        JButton btnCancel = new JButton("Cancel");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dialog.dispose());

        btnSave.addActionListener(e -> {
            // Verifies all required personal info attributes are filled before creation sequence.
            if (fFn.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: First Name cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fLn.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Last Name cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fAdd.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Address cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fPh.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Phone Number cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fSss.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: SSS Number cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fPhl.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: PhilHealth Number cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fTin.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: TIN cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fPag.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Pag-IBIG Number cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            
            // Verifies all mandatory operational or financial entries are filled.
            if (fStat.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Status cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fPos.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Position cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fSup.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Immediate Supervisor cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fSal.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Basic Salary cannot be empty. Enter 0 if none.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fRice.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Rice Subsidy cannot be empty. Enter 0 if none.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fPhone.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Phone Allowance cannot be empty. Enter 0 if none.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fCloth.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Clothing Allowance cannot be empty. Enter 0 if none.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fGrossSemi.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Gross Semi-Monthly Rate cannot be empty. Enter 0 if none.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fHr.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Hourly Rate cannot be empty. Enter 0 if none.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }

            // Validates specific length constraints for government tracking identification fields.
            if (fSss.getText().trim().length() != 12) {
                JOptionPane.showMessageDialog(dialog, "Error: SSS Number must be exactly 12 characters (Format: ##-#######-#).", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (fPhl.getText().trim().length() != 12) {
                JOptionPane.showMessageDialog(dialog, "Error: PhilHealth Number must be exactly 12 digits.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (fTin.getText().trim().length() != 15) {
                JOptionPane.showMessageDialog(dialog, "Error: TIN must be exactly 15 characters (Format: ###-###-###-###).", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (fPag.getText().trim().length() != 12) {
                JOptionPane.showMessageDialog(dialog, "Error: Pag-IBIG Number must be exactly 12 digits.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String formattedBirthday = cbMonth.getSelectedItem() + "/" + cbDay.getSelectedItem() + "/" + cbYear.getSelectedItem();

            String[] newRecord = {
                autoAssignedId, fLn.getText().trim(), fFn.getText().trim(), formattedBirthday, 
                fAdd.getText().trim(), fPh.getText().trim(), fSss.getText().trim(), fPhl.getText().trim(), 
                fTin.getText().trim(), fPag.getText().trim(), fStat.getText().trim(), fPos.getText().trim(),
                fSup.getText().trim(), fSal.getText().trim(), fRice.getText().trim(), fPhone.getText().trim(), 
                fCloth.getText().trim(), fGrossSemi.getText().trim(), fHr.getText().trim()
            };

            DataManager.appendEmployeeRecord(MotorPHSystemCP2.EMPLOYEE_FILE_PATH, newRecord);
            parent.refreshTableData();
            JOptionPane.showMessageDialog(dialog, "Record created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * Displays a modal dialog window to update an existing employee record.
     * Pre-populates all user data entry inputs using the row item selection attributes index pointer.
     * Refreshes dashboard fields and execution formulas immediately upon saving.
     */
    
    public static void spawnModificationFormPopupFrameWindow(MotorPHGUI parent) {
        int r = parent.getTableEmployees().getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(parent, "Please select an employee record row first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String targetId = (String) parent.getTableModel().getValueAt(r, 0);
        int idx = DataManager.findEmployeeIndex(targetId);
        if (idx == -1) return;

        String[] emp = DataManager.employeeList.get(idx);

        JLabel lblIdValue = new JLabel(targetId);
        JTextField fLn = new JTextField(emp[1]); 
        JTextField fFn = new JTextField(emp[2]); 

        JPanel datePanel = new JPanel(new GridLayout(1, 3, 5, 0));
        datePanel.setOpaque(false);
        
        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        JComboBox<String> cbMonth = new JComboBox<>(months);
        JComboBox<String> cbDay = new JComboBox<>();
        
        int currentYr = java.time.Year.now().getValue();
        String[] years = new String[100];
        for (int i = 0; i < 100; i++) years[i] = String.valueOf(currentYr - i);
        JComboBox<String> cbYear = new JComboBox<>(years);

        DefaultListCellRenderer centerRenderer = new DefaultListCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
        cbMonth.setRenderer(centerRenderer);
        cbDay.setRenderer(centerRenderer);
        cbYear.setRenderer(centerRenderer);

        Runnable updateDays = () -> {
            int m = Integer.parseInt((String) cbMonth.getSelectedItem());
            int y = Integer.parseInt((String) cbYear.getSelectedItem());
            int maxDays = java.time.YearMonth.of(y, m).lengthOfMonth();
            
            Object currentSelectedDay = cbDay.getSelectedItem();
            cbDay.removeAllItems();
            for (int i = 1; i <= maxDays; i++) {
                cbDay.addItem(String.format("%02d", i));
            }
            if (currentSelectedDay != null && Integer.parseInt((String)currentSelectedDay) <= maxDays) {
                cbDay.setSelectedItem(currentSelectedDay);
            }
        };
        
        cbMonth.addActionListener(e -> updateDays.run());
        cbYear.addActionListener(e -> updateDays.run());
        updateDays.run(); 

        // Attempts to extract historical date details into separate menu item choices.
        try {
            String[] dateParts = emp[3].trim().split("/");
            if (dateParts.length == 3) {
                cbMonth.setSelectedItem(dateParts[0]);
                cbYear.setSelectedItem(dateParts[2]);
                updateDays.run(); 
                cbDay.setSelectedItem(dateParts[1]);
            }
        } catch (Exception e) {}

        datePanel.add(cbMonth);
        datePanel.add(cbDay);
        datePanel.add(cbYear);

        JTextField fAdd = new JTextField(emp[4]); 
        JTextField fPh = new JTextField(emp[5]); 
        JTextField fSss = new JTextField(emp[6]);
        JTextField fPhl = new JTextField(emp[7]); 
        JTextField fTin = new JTextField(emp[8]); 
        JTextField fPag = new JTextField(emp[9]);
        JTextField fStat = new JTextField(emp[10]); 
        JTextField fPos = new JTextField(emp[11]); 
        JTextField fSup = new JTextField(emp[12]);
        JTextField fSal = new JTextField(emp[13]); 
        JTextField fRice = new JTextField(emp[14]); 
        JTextField fPhone = new JTextField(emp[15]);
        JTextField fCloth = new JTextField(emp[16]); 
        JTextField fGrossSemi = new JTextField(emp[17]); 
        JTextField fHr = new JTextField(emp[18]);

        FieldValidatorGUI.applyStrictDigitFilter(fPhl, 12, false);
        FieldValidatorGUI.applyStrictDigitFilter(fPag, 12, false);
        FieldValidatorGUI.applyStrictDigitFilter(fSal, 0, false);
        FieldValidatorGUI.applyStrictDigitFilter(fRice, 0, false);
        FieldValidatorGUI.applyStrictDigitFilter(fPhone, 0, false);
        FieldValidatorGUI.applyStrictDigitFilter(fCloth, 0, false);
        FieldValidatorGUI.applyStrictDigitFilter(fGrossSemi, 0, false);
        FieldValidatorGUI.applyStrictDigitFilter(fHr, 0, true);

        FieldValidatorGUI.applyAutoHyphenFilter(fPh, "PHONE");
        FieldValidatorGUI.applyAutoHyphenFilter(fSss, "SSS");
        FieldValidatorGUI.applyAutoHyphenFilter(fTin, "TIN");

        JPanel formWrapper = buildTwoSectionFormPanel( 
            lblIdValue, fLn, fFn, datePanel, fAdd, fPh, fSss, fPhl, fTin, fPag,
            fStat, fPos, fSup, fSal, fRice, fPhone, fCloth, fGrossSemi, fHr
        );

        JDialog dialog = new JDialog(parent, "Update Employee Record", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(formWrapper, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnSave = new JButton("Save Record");
        JButton btnCancel = new JButton("Cancel");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dialog.dispose());

        btnSave.addActionListener(e -> {
            if (fFn.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: First Name cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fLn.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Last Name cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fAdd.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Address cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fPh.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Phone Number cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fSss.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: SSS Number cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fPhl.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: PhilHealth Number cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fTin.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: TIN cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fPag.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Pag-IBIG Number cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            
            if (fStat.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Status cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fPos.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Position cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fSup.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Immediate Supervisor cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fSal.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Basic Salary cannot be empty. Enter 0 if none.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fRice.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Rice Subsidy cannot be empty. Enter 0 if none.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fPhone.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Phone Allowance cannot be empty. Enter 0 if none.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fCloth.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Clothing Allowance cannot be empty. Enter 0 if none.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fGrossSemi.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Gross Semi-Monthly Rate cannot be empty. Enter 0 if none.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            if (fHr.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dialog, "Error: Hourly Rate cannot be empty. Enter 0 if none.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }

            if (fSss.getText().trim().length() != 12) {
                JOptionPane.showMessageDialog(dialog, "Error: SSS Number must be exactly 12 characters (Format: ##-#######-#).", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (fPhl.getText().trim().length() != 12) {
                JOptionPane.showMessageDialog(dialog, "Error: PhilHealth Number must be exactly 12 digits.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (fTin.getText().trim().length() != 15) {
                JOptionPane.showMessageDialog(dialog, "Error: TIN must be exactly 15 characters (Format: ###-###-###-###).", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (fPag.getText().trim().length() != 12) {
                JOptionPane.showMessageDialog(dialog, "Error: Pag-IBIG Number must be exactly 12 digits.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String formattedBirthday = cbMonth.getSelectedItem() + "/" + cbDay.getSelectedItem() + "/" + cbYear.getSelectedItem();

            emp[1] = fLn.getText().trim(); emp[2] = fFn.getText().trim(); emp[3] = formattedBirthday;
            emp[4] = fAdd.getText().trim(); emp[5] = fPh.getText().trim(); emp[6] = fSss.getText().trim();
            emp[7] = fPhl.getText().trim(); emp[8] = fTin.getText().trim(); emp[9] = fPag.getText().trim();
            emp[10] = fStat.getText().trim(); emp[11] = fPos.getText().trim(); emp[12] = fSup.getText().trim();
            emp[13] = fSal.getText().trim(); emp[14] = fRice.getText().trim(); emp[15] = fPhone.getText().trim();
            emp[16] = fCloth.getText().trim(); emp[17] = fGrossSemi.getText().trim(); emp[18] = fHr.getText().trim();

            DataManager.updateEmployeeRecord(MotorPHSystemCP2.EMPLOYEE_FILE_PATH, idx, emp);
            parent.refreshTableData();
            
            // Recalculates operational data targets instantly if the evaluation module engine is open.
            if (parent.isCalculated()) {
                parent.executeSalaryCalculationModuleEngineRoutine();
            }
            
            if (r < parent.getTableEmployees().getRowCount()) {
                parent.getTableEmployees().setRowSelectionInterval(r, r);
            }
            parent.syncSelectionDetailsToDashboardFields();
            
            JOptionPane.showMessageDialog(dialog, "Record updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * Constructs a centralized two-column grid assembly panel framework.
     * Separates fields systematically into Profile details versus Financial compensation configurations.
     */
    
    private static JPanel buildTwoSectionFormPanel(
            Object fIdOrComponent, JTextField fLn, JTextField fFn, Component fBd, JTextField fAdd, JTextField fPh, JTextField fSss, JTextField fPhl, JTextField fTin, JTextField fPag,
            JTextField fStat, JTextField fPos, JTextField fSup, JTextField fSal, JTextField fRice, JTextField fPhone, 
            JTextField fCloth, JTextField fGrossSemi, JTextField fHr
        ) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setPreferredSize(new Dimension(720, 480));

        JPanel personalProfileGrid = new JPanel(new GridLayout(1, 2, 15, 0));
        personalProfileGrid.setBorder(BorderFactory.createTitledBorder("Personal Profile Data"));

        JPanel leftColumn = new JPanel(new GridLayout(5, 2, 5, 5));
        JPanel rightColumn = new JPanel(new GridLayout(5, 2, 5, 5));

        leftColumn.add(new JLabel("Employee ID:"));
        if (fIdOrComponent instanceof Component) {
            leftColumn.add((Component) fIdOrComponent);
        }
        leftColumn.add(new JLabel("First Name:")); leftColumn.add(fFn);
        leftColumn.add(new JLabel("Last Name:")); leftColumn.add(fLn);
        leftColumn.add(new JLabel("Birthday (MM/DD/YYYY):")); leftColumn.add(fBd);
        leftColumn.add(new JLabel("Address:")); leftColumn.add(fAdd);

        rightColumn.add(new JLabel("Phone Number:")); rightColumn.add(fPh);
        rightColumn.add(new JLabel("SSS No:")); rightColumn.add(fSss);
        rightColumn.add(new JLabel("PhilHealth No:")); rightColumn.add(fPhl);
        rightColumn.add(new JLabel("TIN No:")); rightColumn.add(fTin);
        rightColumn.add(new JLabel("Pag-IBIG No:")); rightColumn.add(fPag);

        personalProfileGrid.add(leftColumn);
        personalProfileGrid.add(rightColumn);

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

        container.add(personalProfileGrid);
        container.add(Box.createVerticalStrut(10));
        container.add(new JSeparator(JSeparator.HORIZONTAL));
        container.add(Box.createVerticalStrut(10));
        container.add(bottomSection);

        return container;
    }
}

package motorph.system.cp2;

/**
 *
 * @ De Pano, Gatsola, Gonzales, Villamor | Group 15 H1101
 */

/**
 * PayrollCalculator
 * Processes payroll reports and computes
 * salary breakdowns for selected employees.
 */

public class PayrollCalculator {

    /**
     * Updated to accept the drop down selection index from the GUI.
     * index 0 = June, 1 = July, 2 = August, 3 = September, 4 = October, 5 = November, 6 = December
     * index 7 = Full Period Cumulative Summary (June-December)
     */
    public static void generatePayrollReport(int employeeIndex, int selectedDropdownIndex) {
        String[] emp = DataManager.employeeList.get(employeeIndex);
        Employee.printEmployeeDetails(emp);
        System.out.println("\n======| Processed Payroll |======");
        double hourlyRate = Double.parseDouble(emp[MotorPHSystemCP2.CSV_EMP_HOURLY_RATE].replace(",", ""));

        if (selectedDropdownIndex == 7) {
            // Process all months sequentially if "Full Period" is selected
            for (int month = 6; month <= 12; month++) {
                processMonthlyPayroll(emp[MotorPHSystemCP2.CSV_EMP_ID], month, hourlyRate);
            }
        } else {
            // Map the selected index (0-6) cleanly to the corresponding month number (June = 6)
            int targetMonth = selectedDropdownIndex + 6;
            processMonthlyPayroll(emp[MotorPHSystemCP2.CSV_EMP_ID], targetMonth, hourlyRate);
        }
    }
    
    public static void generatePayrollReport(int employeeIndex) {
        // Default console command processes the full sequence matching option index 7
        generatePayrollReport(employeeIndex, 7);
    }

    static void processMonthlyPayroll(String empID, int month, double rate) {
        String monthLabel = DataManager.getMonthNameLabel(month);

        // 1st Cutoff Processing
        double hours1 = AttendanceManager.getTotalHoursWorked(empID, month, 1, 15);
        double gross1 = hours1 * rate;
        printPayrollResults(formatCutoffPeriod(monthLabel, 1, 15), hours1, gross1, gross1);

        // 2nd Cutoff Processing
        int lastDay = (month == 6 || month == 9 || month == 11) ? 30 : 31;
        double hours2 = AttendanceManager.getTotalHoursWorked(empID, month, 16, lastDay);
        double gross2 = hours2 * rate;
        double monthlyGross = gross1 + gross2;

        if (monthlyGross > 0) {
            double sss = GovernmentDeductions.calculateSSS(monthlyGross);
            double philHealth = GovernmentDeductions.calculatePhilHealth(monthlyGross);
            double pagIbig = GovernmentDeductions.calculatePagIbig(monthlyGross);
            double withholdingTax = GovernmentDeductions.calculateWithholdingTax(monthlyGross, sss, philHealth, pagIbig);
            double netSalary = gross2 - (sss + philHealth + pagIbig + withholdingTax);

            printPayrollResults(formatCutoffPeriod(monthLabel, 16, lastDay), hours2, gross2, netSalary);
            printDeductionBreakdown(sss, philHealth, pagIbig, withholdingTax, netSalary);
        } else {
            printPayrollResults(formatCutoffPeriod(monthLabel, 16, lastDay), hours2, gross2, gross2);
        }
    }

    
    static String formatCutoffPeriod(String monthLabel, int startDay, int endDay) {
        return monthLabel + " " + startDay + "–" + endDay;
    }

    
    static void printPayrollResults(String period, double hours, double gross, double net) {
        System.out.println("Period            : " + period);
        System.out.println("Total Hours Worked: " + MotorPHSystemCP2.hoursFormat.format(hours) + " hours");
        System.out.println("Gross Salary      : PHP " + MotorPHSystemCP2.moneyFormat.format(gross));
        System.out.println("Net Salary        : PHP " + MotorPHSystemCP2.moneyFormat.format(net));
        System.out.println("-----------------------------------");
    }

    
    static void printDeductionBreakdown(double sss, double philHealth, double pagIbig, double tax, double net) {
        System.out.println("EACH DEDUCTION:");
        System.out.println("- SSS: PHP " + MotorPHSystemCP2.moneyFormat.format(sss));
        System.out.println("- PhilHealth: PHP " + MotorPHSystemCP2.moneyFormat.format(philHealth));
        System.out.println("- Pag-IBIG: PHP " + MotorPHSystemCP2.moneyFormat.format(pagIbig));
        System.out.println("- Withholding Tax: PHP " + MotorPHSystemCP2.moneyFormat.format(tax));
        System.out.println("TOTAL DEDUCTIONS : PHP " + MotorPHSystemCP2.moneyFormat.format(sss + philHealth + pagIbig + tax));
        System.out.println("|====================================|");
    }
}

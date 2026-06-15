package motorph.system.cp2;

/**
 *
 * @ De Pano, Gatsola, Gonzales, Villamor | Group 15 H1101
 */
import java.util.Scanner;
import java.text.DecimalFormat;
import java.math.RoundingMode;

/**
 * MotorPHSystemCP2
 * Main entry point of the payroll system.
 * Handles login authentication and
 * payroll system navigation.
 */

public class MotorPHSystemCP2 {
    
    // CSV Mapping Constants
    public static final int CSV_EMP_ID = 0;
    public static final int CSV_EMP_LAST_NAME = 1;
    public static final int CSV_EMP_FIRST_NAME = 2;
    public static final int CSV_EMP_BIRTHDAY = 3;
    public static final int CSV_EMP_HOURLY_RATE = 18;

    public static final int CSV_ATT_EMP_ID = 0;
    public static final int CSV_ATT_DATE = 3;
    public static final int CSV_ATT_TIME_IN = 4;
    public static final int CSV_ATT_TIME_OUT = 5;

    // Work Policy Constants
    public static final int SHIFT_START_HOUR = 8;
    public static final int MINUTES_IN_HOUR = 60;
    public static final int LUNCH_THRESHOLD_MINS = 240;
    public static final int LUNCH_DEDUCTION_MINS = 60;
    public static final int GRACE_PERIOD_LIMIT = 10;

    public static final String DEFAULT_PASSWORD = "12345";
    public static final String EMPLOYEE_DATA_FILE_PATH = "Data/MotorPH_Employee Data - Employee Details.csv";
    public static final String ATTENDANCE_DATA_FILE_PATH = "Data/MotorPH_Employee Data - Attendance Record.csv";

    // Government Deductions Settings
    public static final double SSS_MAXIMUM_CONTRIBUTION_CAP = 1125.0; 
    public static final double MIN_PHILHEALTH_CONTRIBUTION_SALARY_THRESHOLD = 10000.0;
    public static final double MAX_PHILHEALTH_SALARY_THRESHOLD = 60000.0;
    public static final double MIN_PHILHEALTH_PREMIUM = 300.0;
    public static final double MAX_PHILHEALTH_PREMIUM = 1800.0;
    public static final double PHILHEALTH_PREMIUM_RATE = 0.03;
    public static final double PAGIBIG_LOWER_SALARY_THRESHOLD = 1500.0;
    public static final double PAGIBIG_LOWER_RATE = 0.01;
    public static final double PAGIBIG_UPPER_RATE = 0.02;
    public static final double MAX_PAGIBIG_CONTRIBUTION_CAP = 100.0;

    public static final DecimalFormat moneyFormat = new DecimalFormat("#,##0.############");
    public static final DecimalFormat hoursFormat = new DecimalFormat("#,##0.############");

    static {
        moneyFormat.setRoundingMode(RoundingMode.DOWN);
        hoursFormat.setRoundingMode(RoundingMode.DOWN);
    }

    public static void main(String[] args) {
        DataManager.loadEmployeeData(EMPLOYEE_DATA_FILE_PATH);
        DataManager.loadAttendanceData(ATTENDANCE_DATA_FILE_PATH);

        Scanner inputScanner = new Scanner(System.in);
        System.out.println("------| MOTORPH SYSTEM LOGIN |------");

        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            System.out.print("Username: ");
            String username = inputScanner.nextLine();
            System.out.print("Password: ");
            String password = inputScanner.nextLine();

            if ((username.equals("employee") || username.equals("payroll_staff")) && password.equals(DEFAULT_PASSWORD)) {
                isLoggedIn = true;
                if (username.equals("employee")) {
                    viewEmployeeProfile(inputScanner);
                } else {
                    launchPayrollMenu(inputScanner);
                }
            } else {
                System.out.println("Error: Incorrect Username and/or Password");
            }
        }
    }

    static void viewEmployeeProfile(Scanner inputScanner) {
        System.out.println("\nWelcome!");
        while (true) {
            System.out.print("Enter Employee Number: ");
            String employeeNumber = inputScanner.nextLine();
            int index = DataManager.findEmployeeIndex(employeeNumber);
            if (index == -1) {
                System.out.println("Error: Employee Number Does Not Exist");
                continue;
            }
            Employee.printEmployeeDetails(DataManager.employeeList.get(index));
            break;
        }
        System.exit(0);
    }

    static void launchPayrollMenu(Scanner inputScanner) {
        System.out.println("\nWelcome!");
        System.out.println("1. Process Payroll \n2. Exit");
        int choice = getSafeInt(inputScanner, "Select: ", 1, 2);

        if (choice == 2) System.exit(0);

        System.out.println("\n1. One Employee \n2. All Employees \n3. Exit");
        int subChoice = getSafeInt(inputScanner, "Select: ", 1, 3);

        if (subChoice == 1) {
            while (true) {
                System.out.print("Enter Employee Number: ");
                String empNum = inputScanner.nextLine();
                int index = DataManager.findEmployeeIndex(empNum);
                if (index != -1) {
                    PayrollCalculator.generatePayrollReport(index);
                    break;
                } else {
                    System.out.println("Error: Employee Number Not Found. Please try again.");
                }
            }
        } else if (subChoice == 2) {
            for (int i = 0; i < DataManager.employeeList.size(); i++) {
                PayrollCalculator.generatePayrollReport(i);
            }
            System.out.println("\nPayroll Processing Status: Finished");
        }
        System.exit(0);
    }

    // Safely reads numeric menu input with validation and retry handling
    static int getSafeInt(Scanner scanner, String prompt, int min, int max) { 
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) return value;
                System.out.println("Error: Invalid Choice. Please Select A Valid Option From The List..." + min + "-" + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            }
        }
    }

}

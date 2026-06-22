package motorph.system.cp2;

/**
 *
 * @ De Pano, Gatsola, Gonzales, Villamor | Group 15 H1101
 */

/**
 * Serves as the central repository for global configuration matrices, index schemas, and compliance variables.
 * Manages foundational application entry logic and initializes memory caches before starting interfaces.
 */

public class MotorPHSystemCP2 {

    // File Registry Configurations.
    public static final String EMPLOYEE_FILE_PATH = "Data/MotorPH_Employee Data - Employee Details.csv";
    public static final String ATTENDANCE_FILE_PATH = "Data/MotorPH_Employee Data - Attendance Record.csv";

    // Employee CSV Column Index Mappings
    public static final int CSV_EMP_ID = 0;
    public static final int CSV_EMP_LAST_NAME = 1;
    public static final int CSV_EMP_FIRST_NAME = 2;
    public static final int CSV_EMP_BIRTHDAY = 3;
    public static final int CSV_EMP_ADDRESS = 4;
    public static final int CSV_EMP_PHONE = 5;
    public static final int CSV_EMP_SSS = 6;
    public static final int CSV_EMP_PHILHEALTH = 7;
    public static final int CSV_EMP_TIN = 8;
    public static final int CSV_EMP_PAGIBIG = 9;
    public static final int CSV_EMP_STATUS = 10;
    public static final int CSV_EMP_POSITION = 11;
    public static final int CSV_EMP_SUPERVISOR = 12;
    public static final int CSV_EMP_BASIC_SALARY = 13;
    public static final int CSV_EMP_RICE_SUBSIDY = 14;
    public static final int CSV_EMP_PHONE_ALLOWANCE = 15;
    public static final int CSV_EMP_CLOTHING_ALLOWANCE = 16;
    public static final int CSV_EMP_SEMI_MONTHLY_RATE = 17;
    public static final int CSV_EMP_HOURLY_RATE = 18;

    // Attendance Management Params
    public static final int CSV_ATT_EMP_ID = 0;
    public static final int CSV_ATT_DATE = 3;
    public static final int CSV_ATT_TIME_IN = 4;
    public static final int CSV_ATT_TIME_OUT = 5;

    // Time & Attendance Rule Constants
    public static final int SHIFT_START_HOUR = 8;
    public static final int GRACE_PERIOD_LIMIT = 10;
    public static final int LUNCH_THRESHOLD_MINS = 240;
    public static final int LUNCH_DEDUCTION_MINS = 60;
    public static final int MINUTES_IN_HOUR = 60;

    // Statutory Deduction Parameters
    public static final double SSS_MAXIMUM_CONTRIBUTION_CAP = 1125.0;
    
    public static final double MIN_PHILHEALTH_CONTRIBUTION_SALARY_THRESHOLD = 10000.0;
    public static final double MAX_PHILHEALTH_SALARY_THRESHOLD = 59999.99;
    public static final double PHILHEALTH_PREMIUM_RATE = 0.03;
    public static final double MIN_PHILHEALTH_PREMIUM = 300.0; 
    public static final double MAX_PHILHEALTH_PREMIUM = 1800.0;

    public static final double PAGIBIG_LOWER_SALARY_THRESHOLD = 1500.0;
    public static final double PAGIBIG_LOWER_RATE = 0.01;
    public static final double PAGIBIG_UPPER_RATE = 0.02;
    public static final double MAX_PAGIBIG_CONTRIBUTION_CAP = 100.0;

    /**
     * Program Entry Core.
     * Populates database tables in memory before launching the user views.
     */
    
    public static void main(String[] args) {
        System.out.println("Initializing MotorPH System Data Pipelines...");
        
        DataManager.loadEmployeeData(EMPLOYEE_FILE_PATH);
        DataManager.loadAttendanceData(ATTENDANCE_FILE_PATH);
        
        System.out.println("Loaded " + DataManager.employeeList.size() + " Employee profiles.");
        System.out.println("Loaded " + DataManager.attendanceList.size() + " Attendance entries.");
    }
}

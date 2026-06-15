package motorph.system.cp2;

/**
 *
 * @ De Pano, Gatsola, Gonzales, Villamor | Group 15 H1101
 */

/**
 * Employee 
 * Handles employee profile display
 * and formatting utilities.
 */

public class Employee {

    public static void printEmployeeDetails(String[] details) {
        System.out.println("\n------| EMPLOYEE DETAILS |------");
        System.out.println("Employee Number: " + details[MotorPHSystemCP2.CSV_EMP_ID]);
        System.out.println("Employee Full Name: " + details[MotorPHSystemCP2.CSV_EMP_FIRST_NAME] + " " + details[MotorPHSystemCP2.CSV_EMP_LAST_NAME]);
        System.out.println("Birthday: " + formatBirthdayDate(details[MotorPHSystemCP2.CSV_EMP_BIRTHDAY]));
        System.out.println("-----------------------------------");
        System.out.println("\n===================================");
    }

    // Converts birthday from numeric format into a readable date label
    public static String formatBirthdayDate(String dateStr) {
        String[] parts = dateStr.split("/");
        return DataManager.getMonthNameLabel(Integer.parseInt(parts[0])) + " " + parts[1] + ", " + parts[2];
    }
}

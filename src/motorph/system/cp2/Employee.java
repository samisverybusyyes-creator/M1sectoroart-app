package motorph.system.cp2;

/**
 *
 * @ De Pano, Gatsola, Gonzales, Villamor | Group 15 H1101
 */

public class Employee {

    public static String getFormatedEmployeeDetails(String[] details) {
        if (details == null || details.length <= MotorPHSystemCP2.CSV_EMP_HOURLY_RATE) {
            return "Error: Incomplete employee data structure.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("------| EMPLOYEE DETAILS |------\n");
        sb.append("Employee Number   : ").append(details[MotorPHSystemCP2.CSV_EMP_ID]).append("\n");
        sb.append("Employee Full Name: ").append(details[MotorPHSystemCP2.CSV_EMP_FIRST_NAME]).append(" ")
          .append(details[MotorPHSystemCP2.CSV_EMP_LAST_NAME]).append("\n");
        sb.append("Birthday          : ").append(formatBirthdayDate(details[MotorPHSystemCP2.CSV_EMP_BIRTHDAY])).append("\n");
        sb.append("Position          : ").append(details[MotorPHSystemCP2.CSV_EMP_POSITION]).append("\n");
        sb.append("Status            : ").append(details[MotorPHSystemCP2.CSV_EMP_STATUS]).append("\n");
        sb.append("SSS Number        : ").append(details[MotorPHSystemCP2.CSV_EMP_SSS]).append("\n");
        sb.append("PhilHealth Number : ").append(details[MotorPHSystemCP2.CSV_EMP_PHILHEALTH]).append("\n");
        sb.append("TIN Number        : ").append(details[MotorPHSystemCP2.CSV_EMP_TIN]).append("\n");
        sb.append("Pag-IBIG Number   : ").append(details[MotorPHSystemCP2.CSV_EMP_PAGIBIG]).append("\n");
        sb.append("-----------------------------------");
        return sb.toString();
    }

    public static String formatBirthdayDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return "N/A";
        }
        
        String[] parts = dateStr.split("/");
        if (parts.length < 3) {
            return dateStr; 
        }
        
        try {
            int monthNum = Integer.parseInt(parts[0].trim());
            String monthLabel = DataManager.getMonthNameLabel(monthNum);
            if (monthLabel.isEmpty()) {
                return dateStr;
            }
            return monthLabel + " " + parts[1].trim() + ", " + parts[2].trim();
        } catch (NumberFormatException e) {
            return dateStr;
        }
    }
}

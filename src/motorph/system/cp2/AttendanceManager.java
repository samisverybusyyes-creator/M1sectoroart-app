package motorph.system.cp2; 

/**
 *
 * @ De Pano, Gatsola, Gonzales, Villamor | Group 15 H1101
 */
import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

/**
 * AttendanceManager
 * Handles employee attendance processing and computes work hours
 * based on recorded time-in and time-out entries.
 */

public class AttendanceManager {

    public static double computeDailyWorkHours(String timeInStr, String timeOutStr) {
        try {
            // Formats parser for attendance time values
            DateTimeFormatter format = DateTimeFormatter.ofPattern("H:mm");

             // Converts text values into LocalTime objects
            LocalTime in = LocalTime.parse(timeInStr.trim(), format);
            LocalTime out = LocalTime.parse(timeOutStr.trim(), format);

            // Company shift policy reference times
            LocalTime start = LocalTime.of(MotorPHSystemCP2.SHIFT_START_HOUR, 0);
            LocalTime graceLimit = LocalTime.of(MotorPHSystemCP2.SHIFT_START_HOUR, MotorPHSystemCP2.GRACE_PERIOD_LIMIT);

            LocalTime effectiveIn = (in.isBefore(start) || !in.isAfter(graceLimit)) ? start : in; // Applies grace period adjustment
            LocalTime effectiveOut = out.isAfter(LocalTime.of(17, 0)) ? LocalTime.of(17, 0) : out;  // Caps workday at official shift end

            // Prevents negative work duration
            if (effectiveOut.isBefore(effectiveIn)) return 0.0;

            // Computes total worked minutes
            long totalMinutes = Duration.between(effectiveIn, effectiveOut).toMinutes();

            // Deducts lunch break if threshold is exceeded
            if (totalMinutes > MotorPHSystemCP2.LUNCH_THRESHOLD_MINS) totalMinutes -= MotorPHSystemCP2.LUNCH_DEDUCTION_MINS;

            // Converts minutes to decimal hours
            return Math.max(0, totalMinutes / (double) MotorPHSystemCP2.MINUTES_IN_HOUR);
        } catch (Exception e) {
            System.out.println("Warning: Invalid time format encountered. Skipping record."); // Invalid attendance record handling
            return 0.0;
        }
    }
    
    public static double getTotalHoursWorked(String id, int month, int startDay, int endDay) {
        double totalHours = 0;

        // Scans attendance records
        for (String[] record : DataManager.attendanceList) { 
            if (!record[MotorPHSystemCP2.CSV_ATT_EMP_ID].equals(id)) continue; // Skips records not belonging to employee
            String[] dateParts = record[MotorPHSystemCP2.CSV_ATT_DATE].split("/"); // Extracts month and day from date
            int recMonth = Integer.parseInt(dateParts[0]);
            int recDay = Integer.parseInt(dateParts[1]);

            // Includes only records within selected cutoff
            if (recMonth == month && recDay >= startDay && recDay <= endDay) {
                totalHours += computeDailyWorkHours(record[MotorPHSystemCP2.CSV_ATT_TIME_IN], record[MotorPHSystemCP2.CSV_ATT_TIME_OUT]);
            }
        }
        return totalHours;
    }
}

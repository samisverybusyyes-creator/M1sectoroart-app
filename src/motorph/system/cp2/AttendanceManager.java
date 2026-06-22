package motorph.system.cp2;

/**
 *
 * @ De Pano, Gatsola, Gonzales, Villamor | Group 15 H1101
 */

import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

// Manages employee attendance logs and calculates work hours and days.

public class AttendanceManager {
    
    /**
     * Calculates total hours worked for a single day based on time-in and time-out strings.
     * Implements grace periods, lunch deductions, and standard shifts.
     * Incorporates basic error handling to return 0.0 for invalid inputs.
     */

    public static double computeDailyWorkHours(String timeInStr, String timeOutStr) {
        try {
            // Checks for missing or empty time values to prevent parsing errors.
            if (timeInStr == null || timeOutStr == null || timeInStr.trim().isEmpty() || timeOutStr.trim().isEmpty()) {
                return 0.0;
            }

            DateTimeFormatter format = DateTimeFormatter.ofPattern("H:mm");

            LocalTime in = LocalTime.parse(timeInStr.trim(), format);
            LocalTime out = LocalTime.parse(timeOutStr.trim(), format);

            // Establishes shift rules using system configuration variables.
            LocalTime start = LocalTime.of(MotorPHSystemCP2.SHIFT_START_HOUR, 0);
            LocalTime graceLimit = LocalTime.of(MotorPHSystemCP2.SHIFT_START_HOUR, MotorPHSystemCP2.GRACE_PERIOD_LIMIT);

            // Applies grace period rules to determine the effective start time.
            LocalTime effectiveIn = (in.isBefore(start) || !in.isAfter(graceLimit)) ? start : in;
            
            // Caps the maximum effective end time at 17:00 (5:00 PM).
            LocalTime effectiveOut = out.isAfter(LocalTime.of(17, 0)) ? LocalTime.of(17, 0) : out;

            // Rejects invalid records where departure occurs before arrival.
            if (effectiveOut.isBefore(effectiveIn)) {
                return 0.0;
            }

            long totalMinutes = Duration.between(effectiveIn, effectiveOut).toMinutes();

            // Deducts lunch break hours if total work time exceeds the threshold.
            if (totalMinutes > MotorPHSystemCP2.LUNCH_THRESHOLD_MINS) {
                totalMinutes -= MotorPHSystemCP2.LUNCH_DEDUCTION_MINS;
            }

            // Converts total minutes into fractional hours. Avoids negative values.
            return Math.max(0, totalMinutes / (double) MotorPHSystemCP2.MINUTES_IN_HOUR);
        } catch (Exception e) {
            // Returns zero hours if date parsing or duration arithmetic fails.
            return 0.0;
        }
    }

    /**
     * Aggregates total hours worked by an employee within a specific date range.
     * Iterates through global attendance list data records.
     */
    
    public static double getTotalHoursWorked(String id, int month, int startDay, int endDay) {
        double totalHours = 0;

        for (String[] record : DataManager.attendanceList) {
            // Confirms the record contains complete data and matches the target employee ID.
            if (record.length > MotorPHSystemCP2.CSV_ATT_TIME_OUT && record[MotorPHSystemCP2.CSV_ATT_EMP_ID].trim().equals(id.trim())) {
                try {
                    // Splits the date string component using the forward slash delimiter.
                    String[] dateParts = record[MotorPHSystemCP2.CSV_ATT_DATE].split("/");
                    if (dateParts.length >= 2) {
                        int recMonth = Integer.parseInt(dateParts[0].trim());
                        int recDay = Integer.parseInt(dateParts[1].trim());

                        // Verifies if the record falls inside the designated date boundaries.
                        if (recMonth == month && recDay >= startDay && recDay <= endDay) {
                            totalHours += computeDailyWorkHours(record[MotorPHSystemCP2.CSV_ATT_TIME_IN], record[MotorPHSystemCP2.CSV_ATT_TIME_OUT]);
                        }
                    }
                } catch (NumberFormatException e) {
                    // Ignores individual corrupt lines to allow processing of remaining records.
                }
            }
        }
        return totalHours;
    }

    /**
     * Counts total days an employee reported for work during a specific date range.
     * A day counts if calculated work hours exceed zero.
     */
    
    public static int getTotalDaysWorked(String id, int month, int startDay, int endDay) {
        int totalDays = 0;

        for (String[] record : DataManager.attendanceList) {
            // Confirms the record contains complete data and matches the target employee ID.
            if (record.length > MotorPHSystemCP2.CSV_ATT_TIME_OUT && record[MotorPHSystemCP2.CSV_ATT_EMP_ID].trim().equals(id.trim())) {
                try {
                    // Splits the date string component using the forward slash delimiter.
                    String[] dateParts = record[MotorPHSystemCP2.CSV_ATT_DATE].split("/");
                    if (dateParts.length >= 2) {
                        int recMonth = Integer.parseInt(dateParts[0].trim());
                        int recDay = Integer.parseInt(dateParts[1].trim());

                        // Verifies if the record falls inside the designated date boundaries.
                        if (recMonth == month && recDay >= startDay && recDay <= endDay) {
                            double hours = computeDailyWorkHours(record[MotorPHSystemCP2.CSV_ATT_TIME_IN], record[MotorPHSystemCP2.CSV_ATT_TIME_OUT]);
                            // Only counts the days if actual labor hours were performed.
                            if (hours > 0.0) {
                                totalDays++;
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    // Ignores individual corrupt lines to allow processing of remaining records.
                }
            }
        }
        return totalDays;
    }
}

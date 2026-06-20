package motorph.system.cp2; 

/**
 *
 * @ De Pano, Gatsola, Gonzales, Villamor | Group 15 H1101
 */
import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class AttendanceManager {
    public static double computeDailyWorkHours(String timeInStr, String timeOutStr) {
        try {
            if (timeInStr == null || timeOutStr == null || timeInStr.trim().isEmpty() || timeOutStr.trim().isEmpty()) {
                return 0.0;
            }

            DateTimeFormatter format = DateTimeFormatter.ofPattern("H:mm");

            LocalTime in = LocalTime.parse(timeInStr.trim(), format);
            LocalTime out = LocalTime.parse(timeOutStr.trim(), format);

            LocalTime start = LocalTime.of(MotorPHSystemCP2.SHIFT_START_HOUR, 0);
            LocalTime graceLimit = LocalTime.of(MotorPHSystemCP2.SHIFT_START_HOUR, MotorPHSystemCP2.GRACE_PERIOD_LIMIT);

            LocalTime effectiveIn = (in.isBefore(start) || !in.isAfter(graceLimit)) ? start : in;
            
            LocalTime effectiveOut = out.isAfter(LocalTime.of(17, 0)) ? LocalTime.of(17, 0) : out;

            if (effectiveOut.isBefore(effectiveIn)) {
                return 0.0;
            }

            long totalMinutes = Duration.between(effectiveIn, effectiveOut).toMinutes();

            if (totalMinutes > MotorPHSystemCP2.LUNCH_THRESHOLD_MINS) {
                totalMinutes -= MotorPHSystemCP2.LUNCH_DEDUCTION_MINS;
            }

            return Math.max(0, totalMinutes / (double) MotorPHSystemCP2.MINUTES_IN_HOUR);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static double getTotalHoursWorked(String id, int month, int startDay, int endDay) {
        double totalHours = 0;

        for (String[] record : DataManager.attendanceList) {
            if (record.length > MotorPHSystemCP2.CSV_ATT_TIME_OUT && record[MotorPHSystemCP2.CSV_ATT_EMP_ID].trim().equals(id.trim())) {
                try {
                    String[] dateParts = record[MotorPHSystemCP2.CSV_ATT_DATE].split("/");
                    if (dateParts.length >= 2) {
                        int recMonth = Integer.parseInt(dateParts[0].trim());
                        int recDay = Integer.parseInt(dateParts[1].trim());

                        if (recMonth == month && recDay >= startDay && recDay <= endDay) {
                            totalHours += computeDailyWorkHours(record[MotorPHSystemCP2.CSV_ATT_TIME_IN], record[MotorPHSystemCP2.CSV_ATT_TIME_OUT]);
                        }
                    }
                } catch (NumberFormatException e) {
                    
                }
            }
        }
        return totalHours;
    }

    public static int getTotalDaysWorked(String id, int month, int startDay, int endDay) {
        int totalDays = 0;

        for (String[] record : DataManager.attendanceList) {
            if (record.length > MotorPHSystemCP2.CSV_ATT_TIME_OUT && record[MotorPHSystemCP2.CSV_ATT_EMP_ID].trim().equals(id.trim())) {
                try {
                    String[] dateParts = record[MotorPHSystemCP2.CSV_ATT_DATE].split("/");
                    if (dateParts.length >= 2) {
                        int recMonth = Integer.parseInt(dateParts[0].trim());
                        int recDay = Integer.parseInt(dateParts[1].trim());

                        if (recMonth == month && recDay >= startDay && recDay <= endDay) {
                            double hours = computeDailyWorkHours(record[MotorPHSystemCP2.CSV_ATT_TIME_IN], record[MotorPHSystemCP2.CSV_ATT_TIME_OUT]);
                            if (hours > 0.0) {
                                totalDays++;
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    
                }
            }
        }
        return totalDays;
    }
}

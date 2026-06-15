package motorph.system.cp2;

/**
 *
 * @ De Pano, Gatsola, Gonzales, Villamor | Group 15 H1101
 */ 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DataManager
 * Responsible for loading and managing employee
 * and attendance CSV data.
 */

public class DataManager {

    public static List<String[]> employeeList = new ArrayList<>(); // Stores employee records loaded from CSV
    public static List<String[]> attendanceList = new ArrayList<>(); // Stores attendance records loaded from CSV

    public static void loadEmployeeData(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Splits CSV while preserving commas inside quoted values
                if (data.length > MotorPHSystemCP2.CSV_EMP_HOURLY_RATE) {
                    for (int i = 0; i < data.length; i++) data[i] = data[i].replace("\"", "").trim(); // Validates minimum required columns before storing
                    employeeList.add(data);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading employees: " + e.getMessage());
        }
    }

    public static void loadAttendanceData(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > MotorPHSystemCP2.CSV_ATT_TIME_OUT) attendanceList.add(data);
            }
        } catch (IOException e) {
            System.out.println("Error loading attendance: " + e.getMessage());
        }
    }

    public static int findEmployeeIndex(String id) {
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i)[MotorPHSystemCP2.CSV_EMP_ID].equals(id)) return i;
        }
        return -1;
    }

    public static String getMonthNameLabel(int monthNum) {
        String[] months = {"", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return months[monthNum];
    }
}

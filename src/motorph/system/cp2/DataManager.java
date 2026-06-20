package motorph.system.cp2;

/**
 *
 * @ De Pano, Gatsola, Gonzales, Villamor | Group 15 H1101
 */ 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    public static List<String[]> employeeList = new ArrayList<>();
    public static List<String[]> attendanceList = new ArrayList<>();
    
    private static String employeeHeader = "Employee #,Last Name,First Name,Birthday,Address,Phone Number,SSS #,Philhealth #,TIN #,Pag-ibig #,Status,Position,Immediate Supervisor,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate";

    public static void loadEmployeeData(String fileName) {
        employeeList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String header = br.readLine();
            if (header != null) {
                employeeHeader = header;
            }
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (data.length > MotorPHSystemCP2.CSV_EMP_HOURLY_RATE) {
                    for (int i = 0; i < data.length; i++) {
                        data[i] = data[i].replace("\"", "").trim();
                    }
                    employeeList.add(data);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading employees: " + e.getMessage());
        }
    }

    public static void loadAttendanceData(String fileName) {
        attendanceList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > MotorPHSystemCP2.CSV_ATT_TIME_OUT) {
                    for (int i = 0; i < data.length; i++) {
                        data[i] = data[i].trim();
                    }
                    attendanceList.add(data);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading attendance: " + e.getMessage());
        }
    }

    public static int findEmployeeIndex(String id) {
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i)[MotorPHSystemCP2.CSV_EMP_ID].trim().equals(id.trim())) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isEmployeeIdExists(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        for (String[] emp : employeeList) {
            if (emp.length > MotorPHSystemCP2.CSV_EMP_ID && emp[MotorPHSystemCP2.CSV_EMP_ID].trim().equalsIgnoreCase(id.trim())) {
                return true;
            }
        }
        return false;
    }

    public static boolean saveAllEmployees(String fileName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            pw.println(employeeHeader);
            for (String[] emp : employeeList) {
                StringBuilder row = new StringBuilder();
                for (int i = 0; i < emp.length; i++) {
                    String field = emp[i];
                    if (field.contains(",")) {
                        row.append("\"").append(field).append("\"");
                    } else {
                        row.append(field);
                    }
                    if (i < emp.length - 1) {
                        row.append(",");
                    }
                }
                pw.println(row.toString());
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error saving employee data: " + e.getMessage());
            return false;
        }
    }

    public static boolean appendEmployeeRecord(String fileName, String[] newRecord) {
        employeeList.add(newRecord);
        return saveAllEmployees(fileName);
    }

    public static boolean updateEmployeeRecord(String fileName, int index, String[] updatedRecord) {
        if (index >= 0 && index < employeeList.size()) {
            employeeList.set(index, updatedRecord);
            return saveAllEmployees(fileName);
        }
        return false;
    }

    public static boolean deleteEmployeeRecord(String fileName, int index) {
        if (index >= 0 && index < employeeList.size()) {
            employeeList.remove(index);
            return saveAllEmployees(fileName);
        }
        return false;
    }

    public static String getMonthNameLabel(int monthNum) {
        String[] months = {"", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        if (monthNum >= 1 && monthNum <= 12) {
            return months[monthNum];
        }
        return "";
    }
}

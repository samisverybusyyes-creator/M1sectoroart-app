package motorph.system.cp2;

/**
 *
 * @ De Pano, Gatsola, Gonzales, Villamor | Group 15 H1101
 */

/**
 * Processes automated employee compensation components and statutory deduction distributions.
 * Computes individual gross breakdowns, tax bounds, and total net revenue returns.
 */

public class PayrollCalculator {

    // Calculates foundational compensation distributions using strict hourly metrics.
    
    public static double computeGrossPayHourly(double hourlyRate, double totalHoursWorked) {
        if (hourlyRate < 0 || totalHoursWorked < 0) return 0.0;
        return hourlyRate * totalHoursWorked;
    }

    // Calculates foundational compensation distributions using strict daily metrics.
    
    public static double computeGrossPay(double dailyRate, int totalDaysWorked) {
        if (dailyRate < 0 || totalDaysWorked < 0) return 0.0;
        return dailyRate * totalDaysWorked;
    }

    // Executes complete multi-tier monthly payroll metrics calculations for a specified identity.
    
    public static double[] calculateCompletePayroll(String employeeId, int month, int startDay, int endDay) {
        double[] payrollBreakdown = new double[7]; 
        
        int empIndex = DataManager.findEmployeeIndex(employeeId.trim());
        if (empIndex == -1) {
            return payrollBreakdown; 
        }
        
        String[] empDetails = DataManager.employeeList.get(empIndex);
        
        try {
            String rawRate = empDetails[MotorPHSystemCP2.CSV_EMP_HOURLY_RATE].replaceAll("[^\\d.]", "");
            double hourlyRate = Double.parseDouble(rawRate);
            
            int maxDay = (month == 6 || month == 9 || month == 11) ? 30 : 31;
            double hours1 = AttendanceManager.getTotalHoursWorked(employeeId.trim(), month, 1, 15);
            double hours2 = AttendanceManager.getTotalHoursWorked(employeeId.trim(), month, 16, maxDay);
            
            double gross1 = hours1 * hourlyRate;
            double gross2 = hours2 * hourlyRate;
            double monthlyGross = gross1 + gross2; 
            
            double sss = 0, philHealth = 0, pagIbig = 0, withholdingTax = 0, netPay = 0;

            if (monthlyGross > 0) {
                sss = GovernmentDeductions.calculateSSS(monthlyGross);
                philHealth = GovernmentDeductions.calculatePhilHealth(monthlyGross);
                pagIbig = GovernmentDeductions.calculatePagIbig(monthlyGross);
                withholdingTax = GovernmentDeductions.calculateWithholdingTax(monthlyGross, sss, philHealth, pagIbig);
                
                netPay = monthlyGross - (sss + philHealth + pagIbig + withholdingTax);
            }
            
            payrollBreakdown[0] = monthlyGross;
            payrollBreakdown[1] = sss;
            payrollBreakdown[2] = philHealth;
            payrollBreakdown[3] = pagIbig;
            payrollBreakdown[4] = withholdingTax;
            payrollBreakdown[5] = (sss + philHealth + pagIbig + withholdingTax);
            payrollBreakdown[6] = Math.max(0.0, netPay); 
            
        } catch (Exception e) {
            System.out.println("PAYROLL CRASH for ID " + employeeId + ":");
            e.printStackTrace(); 
        }
        
        return payrollBreakdown;
    }
}

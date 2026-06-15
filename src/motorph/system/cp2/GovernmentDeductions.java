package motorph.system.cp2;

/**
 *
 * @ De Pano, Gatsola, Gonzales, Villamor | Group 15 H1101
 */ 

/**
 * GovernmentDeductions
 * Calculates mandatory government deductions
 * including SSS, PhilHealth, Pag-IBIG,
 * and withholding tax.
 */

public class GovernmentDeductions {

    // Calculates employee SSS contribution based on salary bracket table
    public static double calculateSSS(double monthlyGross) { 
        double[][] sssTable = { {3250, 135.0}, {3750, 157.5}, {4250, 180.0},
            {4750, 202.5}, {5250, 225.0}, {5750, 247.5}, {6250, 270.0},
            {6750,292.5}, {7250, 315.0}, {7750, 337.5}, {8250, 360.0},
            {8750, 382.5}, {9250, 405.0}, {9750, 427.5}, {10250, 450.0},
            {10750, 472.5}, {11250,495.0}, {11750, 517.5}, {12250, 540.0},
            {12750, 562.5}, {13250, 585.0}, {13750, 607.5}, {14250, 630.0},
            {14750, 652.5}, {15250, 675.0}, {15750, 697.5}, {16250, 720.0},
            {16750, 742.5}, {17250, 765.0}, {17750, 787.5}, {18250, 810.0},
            {18750, 832.5}, {19250, 855.0}, {19750, 877.5}, {20250, 900.0},
            {20750, 922.5}, {21250, 945.0}, {21750, 967.5}, {22250, 990.0},
            {22750, 1012.5}, {23250, 1035.0}, {23750, 1057.5}, {24250, 1080.0},
            {24750, 1102.5} };
        for (double[] bracket : sssTable) if (monthlyGross < bracket[0]) return bracket[1];
        return MotorPHSystemCP2.SSS_MAXIMUM_CONTRIBUTION_CAP;
    }

    // Calculates employee PhilHealth contribution
    public static double calculatePhilHealth(double monthlyGross) {
        if (monthlyGross <= MotorPHSystemCP2.MIN_PHILHEALTH_CONTRIBUTION_SALARY_THRESHOLD) return MotorPHSystemCP2.MIN_PHILHEALTH_PREMIUM / 2;
        if (monthlyGross < MotorPHSystemCP2.MAX_PHILHEALTH_SALARY_THRESHOLD) return (monthlyGross * MotorPHSystemCP2.PHILHEALTH_PREMIUM_RATE) / 2;
        return MotorPHSystemCP2.MAX_PHILHEALTH_PREMIUM / 2;
    }

    // Calculates Pag-IBIG contribution subject to contribution cap
    public static double calculatePagIbig(double monthlyGross) {
        double rate = (monthlyGross <= MotorPHSystemCP2.PAGIBIG_LOWER_SALARY_THRESHOLD) ? MotorPHSystemCP2.PAGIBIG_LOWER_RATE : MotorPHSystemCP2.PAGIBIG_UPPER_RATE;
        return Math.min(monthlyGross * rate, MotorPHSystemCP2.MAX_PAGIBIG_CONTRIBUTION_CAP);
    }

    // Calculates withholding tax based on taxable income after deductions
    public static double calculateWithholdingTax(double monthlyGross, double sss, double philHealth, double pagIbig) {
        double taxableIncome = monthlyGross - (sss + philHealth + pagIbig);
        if (taxableIncome <= 20832) return 0;
        if (taxableIncome < 33333) return (taxableIncome - 20833) * 0.20;
        if (taxableIncome < 66667) return 2500 + (taxableIncome - 33333) * 0.25;
        if (taxableIncome < 166667) return 10833 + (taxableIncome - 66667) * 0.30;
        if (taxableIncome < 666667) return 40833.33 + (taxableIncome - 166667) * 0.32;
        return 200833.33 + (taxableIncome - 666667) * 0.35;
    }
}

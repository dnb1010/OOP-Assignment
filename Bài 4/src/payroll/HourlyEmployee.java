// Đồng Ngọc Bảo 202418849
package payroll;

import java.util.Locale;

public class HourlyEmployee extends Employee {
    private double hourlyRate;
    private double workedHours;

    public HourlyEmployee(String employeeId, String fullName) {
        this(employeeId, fullName, "Unassigned", 0.0, 0.0);
    }

    public HourlyEmployee(String employeeId, String fullName, String department,
                          double hourlyRate, double workedHours) {
        super(employeeId, fullName, department);
        setHourlyRate(hourlyRate);
        setWorkedHours(workedHours);
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        if (hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative.");
        }
        this.hourlyRate = hourlyRate;
    }

    public double getWorkedHours() {
        return workedHours;
    }

    public void setWorkedHours(double workedHours) {
        if (workedHours < 0 || workedHours > 250) {
            throw new IllegalArgumentException("Worked hours must be between 0 and 250.");
        }
        this.workedHours = workedHours;
    }

    public double getBasePay() {
        if (workedHours <= 160) {
            return workedHours * hourlyRate;
        }
        return 160 * hourlyRate + (workedHours - 160) * hourlyRate * 1.5;
    }

    @Override
    public double calculateGrossPay() {
        return getBasePay() + getMonthlyBonus();
    }

    @Override
    public String getEmployeeType() {
        return "HourlyEmployee";
    }

    @Override
    public void displayPayrollInfo() {
        System.out.printf(Locale.US,
                "[%s] %s | Department: %s | Hourly rate: %,.2f | Worked hours: %,.2f | Base pay: %,.2f | Bonus: %,.2f | Gross pay: %,.2f%n",
                getEmployeeType(), getFullName(), getDepartment(), hourlyRate, workedHours,
                getBasePay(), getMonthlyBonus(), calculateGrossPay());
    }
}


// Đồng Ngọc Bảo 202418849
package payroll;

import java.util.Locale;

public class SalariedEmployee extends Employee {
    private double monthlySalary;
    private double responsibilityAllowance;

    public SalariedEmployee(String employeeId, String fullName) {
        this(employeeId, fullName, "Unassigned", 0.0, 0.0);
    }

    public SalariedEmployee(String employeeId, String fullName, String department,
                           double monthlySalary, double responsibilityAllowance) {
        super(employeeId, fullName, department);
        setMonthlySalary(monthlySalary);
        setResponsibilityAllowance(responsibilityAllowance);
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(double monthlySalary) {
        if (monthlySalary < 0) {
            throw new IllegalArgumentException("Monthly salary cannot be negative.");
        }
        this.monthlySalary = monthlySalary;
    }

    public double getResponsibilityAllowance() {
        return responsibilityAllowance;
    }

    public void setResponsibilityAllowance(double responsibilityAllowance) {
        if (responsibilityAllowance < 0) {
            throw new IllegalArgumentException("Responsibility allowance cannot be negative.");
        }
        this.responsibilityAllowance = responsibilityAllowance;
    }

    @Override
    public double calculateGrossPay() {
        return monthlySalary + responsibilityAllowance + getMonthlyBonus();
    }

    @Override
    public String getEmployeeType() {
        return "SalariedEmployee";
    }

    @Override
    public void displayPayrollInfo() {
        System.out.printf(Locale.US,
                "[%s] %s | Department: %s | Monthly salary: %,.2f | Allowance: %,.2f | Bonus: %,.2f | Gross pay: %,.2f%n",
                getEmployeeType(), getFullName(), getDepartment(), monthlySalary, responsibilityAllowance,
                getMonthlyBonus(), calculateGrossPay());
    }
}


// Đồng Ngọc Bảo 202418849
package payroll;

import java.util.Locale;

public class SalesEmployee extends Employee {
    private double baseSalary;
    private double salesRevenue;
    private double commissionRate;

    public SalesEmployee(String employeeId, String fullName) {
        this(employeeId, fullName, "Unassigned", 0.0, 0.0, 0.0);
    }

    public SalesEmployee(String employeeId, String fullName, String department,
                         double baseSalary, double salesRevenue, double commissionRate) {
        super(employeeId, fullName, department);
        setBaseSalary(baseSalary);
        setSalesRevenue(salesRevenue);
        setCommissionRate(commissionRate);
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        if (baseSalary < 0) {
            throw new IllegalArgumentException("Base salary cannot be negative.");
        }
        this.baseSalary = baseSalary;
    }

    public double getSalesRevenue() {
        return salesRevenue;
    }

    public void setSalesRevenue(double salesRevenue) {
        if (salesRevenue < 0) {
            throw new IllegalArgumentException("Sales revenue cannot be negative.");
        }
        this.salesRevenue = salesRevenue;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate) {
        if (commissionRate < 0 || commissionRate > 0.3) {
            throw new IllegalArgumentException("Commission rate must be between 0 and 0.3.");
        }
        this.commissionRate = commissionRate;
    }

    @Override
    public double calculateGrossPay() {
        return baseSalary + salesRevenue * commissionRate + getMonthlyBonus();
    }

    @Override
    public String getEmployeeType() {
        return "SalesEmployee";
    }

    @Override
    public void displayPayrollInfo() {
        System.out.printf(Locale.US,
                "[%s] %s | Department: %s | Base salary: %,.2f | Sales revenue: %,.2f | Commission rate: %,.2f%% | Bonus: %,.2f | Gross pay: %,.2f%n",
                getEmployeeType(), getFullName(), getDepartment(), baseSalary, salesRevenue,
                commissionRate * 100, getMonthlyBonus(), calculateGrossPay());
    }
}

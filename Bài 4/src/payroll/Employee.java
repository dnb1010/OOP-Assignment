// Đồng Ngọc Bảo 202418849
package payroll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Employee {
    private String employeeId;
    private String fullName;
    private String department;
    private double monthlyBonus;
    private final List<BonusRecord> bonusHistory = new ArrayList<>();

    protected Employee(String employeeId, String fullName) {
        this(employeeId, fullName, "Unassigned");
    }

    protected Employee(String employeeId, String fullName, String department) {
        setEmployeeId(employeeId);
        setFullName(fullName);
        setDepartment(department);
        this.monthlyBonus = 0.0;
    }

    public static class BonusRecord {
        private final double amount;
        private final String reason;
        private final String type;

        public BonusRecord(double amount, String reason, String type) {
            this.amount = amount;
            this.reason = reason;
            this.type = type;
        }

        public double getAmount() {
            return amount;
        }

        public String getReason() {
            return reason;
        }

        public String getType() {
            return type;
        }
    }

    public void addBonus(double amount) {
        addBonus(amount, null);
    }

    public void addBonus(double amount, String reason) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Bonus amount must be greater than 0.");
        }
        if (reason != null && reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Bonus reason cannot be empty when provided.");
        }

        monthlyBonus += amount;
        bonusHistory.add(new BonusRecord(amount, reason, "Fixed"));
    }

    public void addBonus(double rate, double referenceAmount, String reason) {
        if (rate <= 0 || rate > 0.5) {
            throw new IllegalArgumentException("Bonus rate must be in the range (0, 0.5].");
        }
        if (referenceAmount <= 0) {
            throw new IllegalArgumentException("Reference amount must be greater than 0.");
        }
        if (reason != null && reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Bonus reason cannot be empty when provided.");
        }

        double computedBonus = rate * referenceAmount;
        monthlyBonus += computedBonus;
        bonusHistory.add(new BonusRecord(computedBonus, reason, "Rate-based"));
    }

    public void resetMonthlyBonus() {
        this.monthlyBonus = 0.0;
        this.bonusHistory.clear();
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be empty.");
        }
        this.employeeId = employeeId.trim();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty.");
        }
        this.fullName = fullName.trim();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty.");
        }
        this.department = department.trim();
    }

    public double getMonthlyBonus() {
        return monthlyBonus;
    }

    public List<BonusRecord> getBonusHistory() {
        return Collections.unmodifiableList(new ArrayList<>(bonusHistory));
    }

    public abstract double calculateGrossPay();

    public abstract String getEmployeeType();

    public abstract void displayPayrollInfo();
}

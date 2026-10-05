
// Đồng Ngọc Bảo 202418849
package payroll;

import java.util.ArrayList;
import java.util.List;

public class Payroll {
    private final String period;
    private final List<Employee> employees = new ArrayList<>();

    public Payroll(String period) {
        if (period == null || period.trim().isEmpty()) {
            throw new IllegalArgumentException("Payroll period cannot be empty.");
        }
        this.period = period.trim();
    }

    public String getPeriod() {
        return period;
    }

    public void addEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        if (findEmployee(employee.getEmployeeId()) != null) {
            throw new IllegalArgumentException("Employee with ID " + employee.getEmployeeId() + " already exists.");
        }
        employees.add(employee);
    }

    public Employee findEmployee(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            return null;
        }
        for (Employee employee : employees) {
            if (employee.getEmployeeId().equalsIgnoreCase(employeeId.trim())) {
                return employee;
            }
        }
        return null;
    }

    public double calculateTotalPayroll() {
        double total = 0;
        for (Employee employee : employees) {
            total += employee.calculateGrossPay();
        }
        return total;
    }

    public double calculatePayrollByDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty.");
        }

        double total = 0;
        for (Employee employee : employees) {
            if (employee.getDepartment().equalsIgnoreCase(department.trim())) {
                total += employee.calculateGrossPay();
            }
        }
        return total;
    }

    public Employee findHighestPaidEmployee() {
        if (employees.isEmpty()) {
            return null;
        }

        Employee highestPaid = employees.get(0);
        for (int i = 1; i < employees.size(); i++) {
            Employee current = employees.get(i);
            if (current.calculateGrossPay() > highestPaid.calculateGrossPay()) {
                highestPaid = current;
            }
        }
        return highestPaid;
    }

    public void displayPayroll() {
        if (employees.isEmpty()) {
            System.out.println("Payroll for " + period + " is empty.");
            return;
        }

        System.out.println("Payroll period: " + period);
        for (Employee employee : employees) {
            employee.displayPayrollInfo();
        }
        System.out.println("Total payroll: " + String.format(java.util.Locale.US, "%,.0f", calculateTotalPayroll()));
    }
}

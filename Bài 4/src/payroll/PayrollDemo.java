
// Đồng Ngọc Bảo 202418849
package payroll;

public class PayrollDemo {
    public static void main(String[] args) {
        Payroll payroll = new Payroll("2026-09");

        SalariedEmployee e001 = new SalariedEmployee(
                "E001", "Nguyễn Minh An", "Đào tạo", 15_000_000, 2_000_000
        );
        e001.addBonus(1_000_000, "Thưởng cố định");

        HourlyEmployee e002 = new HourlyEmployee(
                "E002", "Trần Thu Bình", "Hỗ trợ", 100_000, 150
        );
        e002.addBonus(500_000, "Thưởng tháng");

        HourlyEmployee e003 = new HourlyEmployee(
                "E003", "Lê Hoàng Chi", "Hỗ trợ", 100_000, 170
        );

        SalesEmployee e004 = new SalesEmployee(
                "E004", "Phạm Quốc Dũng", "Kinh doanh", 8_000_000, 200_000_000, 0.05
        );
        e004.addBonus(0.02, 50_000_000, "Thưởng theo tỷ lệ");

        payroll.addEmployee(e001);
        payroll.addEmployee(e002);
        payroll.addEmployee(e003);
        payroll.addEmployee(e004);

        payroll.displayPayroll();

        System.out.println("Total payroll expected: 70,000,000");
        System.out.println("Total payroll actual: " + String.format(java.util.Locale.US, "%,.0f", payroll.calculateTotalPayroll()));
        System.out.println("Support department total: " + String.format(java.util.Locale.US, "%,.0f", payroll.calculatePayrollByDepartment("Hỗ trợ")));

        Employee highestPaidEmployee = payroll.findHighestPaidEmployee();
        if (highestPaidEmployee != null) {
            System.out.println("Highest paid employee: " + highestPaidEmployee.getFullName() + " => "
                    + String.format(java.util.Locale.US, "%,.0f", highestPaidEmployee.calculateGrossPay()));
        }
    }
}

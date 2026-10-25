# Bài 4 — Hệ thống tính lương Java

Ứng dụng tính tổng lương theo kỳ cho ba loại nhân viên. Các lớp cùng thuộc package `payroll` dưới `src/payroll`.

## Thiết kế và trách nhiệm từng lớp

### `Employee`

Lớp trừu tượng chứa mã nhân viên, họ tên, phòng ban, tổng thưởng tháng và lịch sử `BonusRecord`. Constructor mặc định phòng ban là `Unassigned`; các setter mã/tên/phòng ban từ chối `null` hoặc chuỗi rỗng.

`addBonus()` được overload theo ba cách:

- `addBonus(amount)`: thưởng cố định.
- `addBonus(amount, reason)`: thưởng cố định kèm lý do tùy chọn.
- `addBonus(rate, referenceAmount, reason)`: thưởng bằng tỷ lệ nhân số tiền tham chiếu; tỷ lệ thuộc `(0, 0.5]`.

Thưởng cố định phải lớn hơn 0; lý do nếu có không được là chuỗi trắng. Mỗi lần cộng thưởng ghi một bản ghi với loại `Fixed` hoặc `Rate-based`. `getBonusHistory()` trả về bản sao không sửa được; `resetMonthlyBonus()` đặt tổng thưởng về 0 và xóa lịch sử. Lớp khai báo các phương thức trừu tượng `calculateGrossPay()`, `getEmployeeType()` và `displayPayrollInfo()`.

### Các loại nhân viên

| Lớp | Dữ liệu riêng | Công thức `calculateGrossPay()` |
|---|---|---|
| `SalariedEmployee` | Lương tháng, phụ cấp trách nhiệm | Lương tháng + phụ cấp + thưởng |
| `HourlyEmployee` | Đơn giá giờ, số giờ làm | Lương cơ bản + thưởng |
| `SalesEmployee` | Lương cơ bản, doanh thu, tỷ lệ hoa hồng | Lương cơ bản + doanh thu × tỷ lệ + thưởng |

Các số tiền không âm. Nhân viên hưởng giờ nhận tối đa 250 giờ; 160 giờ đầu tính thường, phần vượt tính 1.5 lần đơn giá. Tỷ lệ hoa hồng nhân viên bán hàng nằm trong `[0, 0.3]`. Mỗi lớp override phương thức hiển thị để in các thành phần lương tương ứng.

### `Payroll`

Quản lý kỳ lương và danh sách `Employee`:

- `addEmployee()` từ chối null và mã nhân viên trùng.
- `findEmployee()` tìm mã không phân biệt chữ hoa/thường; đầu vào rỗng trả về `null`.
- `calculateTotalPayroll()` cộng lương thực nhận của tất cả nhân viên bằng dispatch đa hình.
- `calculatePayrollByDepartment()` cộng lương theo phòng ban, không phân biệt chữ hoa/thường.
- `findHighestPaidEmployee()` trả về nhân viên có lương cao nhất hoặc `null` nếu danh sách rỗng.
- `displayPayroll()` in kỳ lương, thông tin từng nhân viên và tổng.

### `PayrollDemo`

Tạo kỳ `2026-09`, thêm một nhân viên lương tháng, hai nhân viên hưởng giờ và một nhân viên bán hàng; cộng thưởng, in bảng lương, truy vấn phòng ban và nhân viên có lương cao nhất.

## Biên dịch và chạy

Yêu cầu JDK. Từ PowerShell tại thư mục repository:

```powershell
Set-Location "Bài 4"
javac -d out src\payroll\*.java
java -cp out payroll.PayrollDemo
```

Với dữ liệu demo, tổng lương là `70,000,000`: 18,000,000 (nhân viên lương tháng) + 15,500,000 (150 giờ) + 17,500,000 (170 giờ, gồm 10 giờ tăng ca) + 19,000,000 (nhân viên bán hàng). Con số kỳ vọng được in trong `PayrollDemo` khớp với kết quả tính.

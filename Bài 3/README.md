# Bài 3 — Quản lý nhân sự dự án

Chương trình Python mô hình hóa nhân sự dự án, trong đó kỹ sư phần mềm là một loại nhân viên và nhóm dự án tập hợp nhân viên cùng một trưởng nhóm. Mã nguồn nằm trong `employee.py`, `software_engineer.py`, `project_team.py` và `main.py`.

## Mô hình lớp

### `employee.py`: kiểm tra dữ liệu và `Employee`

- `check_text(value, field_name)` yêu cầu chuỗi không rỗng sau khi trim và trả về chuỗi đã trim.
- `check_number(value, field_name, positive=False)` chỉ nhận `int`/`float` hữu hạn, từ chối `bool`; giá trị mặc định phải không âm, còn `positive=True` yêu cầu lớn hơn 0. Giá trị trả về là `float`.
- `Employee` lưu mã, họ tên và lương cơ bản trong thuộc tính private. Mã và tên bắt buộc không rỗng; lương không âm.
- Các property `employeeId`, `id`, `fullName`, `baseSalary` cung cấp quyền đọc. `id` là bí danh của `employeeId`.
- `increaseSalary(value, byPercentage=False)` tăng theo số tiền cố định hoặc theo tỷ lệ phần trăm. Mức tăng phải lớn hơn 0; `byPercentage` phải là boolean. Sau khi tính, lương mới được xác nhận hữu hạn.
- `calculateMonthlyCost()` trả về lương cơ bản. `calculateMontlyCost()` giữ tên cũ bị thiếu chữ “h” và chuyển tiếp sang method đúng.
- `displayInfo()` in dữ liệu nhân viên. Destructor in thông báo khi đối tượng bị thu gom.

### `software_engineer.py`: `SoftwareEngineer(Employee)`

Constructor gọi constructor lớp cha rồi thêm ngôn ngữ chính (bắt buộc có nội dung) và phụ cấp kỹ thuật (không âm). `calculateMonthlyCost()` trả về lương cơ bản cộng phụ cấp. `displayInfo()` hiển thị thêm ngôn ngữ và chi phí. Lớp cũng cung cấp alias `calculateMontlyCost()`.

### `project_team.py`: `ProjectTeam`

- Mã/tên dự án được kiểm tra bằng `check_text`.
- Thành viên được lưu trong từ điển theo mã nhân viên; mỗi giá trị là `weakref`, nên nhóm không sở hữu mạnh vòng đời của nhân viên. Bên gọi cần giữ tham chiếu mạnh đến nhân viên nếu muốn họ luôn còn trong nhóm.
- `members` trả về tuple snapshot, `leader` trả về trưởng nhóm hoặc `None`, `contains()` kiểm tra mã.
- `addMember(employee, makeLeader=False)` chỉ nhận `Employee`. Mã trùng bị từ chối; thêm đúng cùng một đối tượng đã có cũng trả về `False`, trừ khi yêu cầu đặt chính đối tượng đó làm trưởng nhóm.
- `removeMember(id)` trả về `False` nếu không có mã hoặc nhân viên đang là trưởng nhóm; không tự xóa/đổi trưởng nhóm.
- `changeLeader(employee)` đặt trưởng nhóm; nếu nhân viên chưa có trong nhóm thì tự thêm. Một đối tượng khác có cùng mã sẽ bị từ chối.
- `calculateTotalMonthlyCost()` cộng `calculateMonthlyCost()` của thành viên, do đó chi phí kỹ sư được tính qua đa hình.
- `displayTeam()` in thông tin dự án, trưởng nhóm, danh sách và tổng chi phí.

## `main.py`: hai cách chạy

- Khi chạy `python main.py`, chương trình gọi `demo()`. Demo dùng `assert` để kiểm tra tăng lương, thành viên trùng, đổi/xóa trưởng nhóm, đa hình, tổng chi phí và các trường hợp dữ liệu không hợp lệ.
- `main()` là luồng nhập liệu tương tác: nhập dự án, số thành viên, loại nhân sự, lương và dữ liệu kỹ sư nếu cần; sau đó có thể chọn trưởng nhóm. Để gọi luồng này:

```powershell
python -c "import main; main.main()"
```

Chạy demo tại thư mục này:

```powershell
python main.py
```

Yêu cầu Python 3.10 trở lên do mã dùng union type `|`. Không cần thư viện ngoài.

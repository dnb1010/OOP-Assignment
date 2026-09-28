# Bài 3: Quản lý nhân sự dự án

Chương trình minh họa lập trình hướng đối tượng bằng Python với ba lớp `Employee`, `SoftwareEngineer` và `ProjectTeam`.

## Yêu cầu

- Python 3.10 trở lên.
- Không cần cài thêm thư viện.

## Chạy chương trình

Mở terminal tại thư mục `Bài 3`.

Chạy phần demo có sẵn:

```powershell
python main.py
```

Để nhập thông tin dự án và nhân sự từ bàn phím:

```powershell
python -c "import main; main.main()"
```

Chương trình nhập mã và tên dự án, số lượng nhân sự, loại nhân sự, thông tin lương và mã trưởng nhóm. Với kỹ sư phần mềm, chương trình cũng hỏi ngôn ngữ lập trình và phụ cấp kỹ thuật. Nhấn Enter ở lời nhắc mã trưởng nhóm để tạo nhóm chưa có trưởng nhóm.

## Nội dung minh họa

- Khởi tạo nhân viên và kỹ sư phần mềm.
- Tăng lương theo số tiền hoặc theo phần trăm.
- Thêm thành viên, đổi trưởng nhóm và ngăn mã nhân sự trùng.
- Không cho xóa trưởng nhóm hiện tại.
- Tính chi phí hàng tháng bằng đa hình.
- Kiểm tra dữ liệu không hợp lệ và một số trường hợp biên.

## Các tệp

- `employee.py`: lớp `Employee` và kiểm tra dữ liệu đầu vào.
- `software_engineer.py`: lớp `SoftwareEngineer`, kế thừa `Employee`.
- `project_team.py`: quản lý thành viên, trưởng nhóm và tổng chi phí dự án.
- `main.py`: demo có sẵn và luồng nhập dữ liệu từ bàn phím.
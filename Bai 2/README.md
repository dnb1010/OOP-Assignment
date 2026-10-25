# Bài 2 — Người và học phần

Tệp [`Course.py`](./Course.py) chứa các lớp mô hình người học/giảng dạy và học phần. Phần `main()` tạo dữ liệu mẫu, gọi các thao tác, rồi in trạng thái trước và sau khi cập nhật.

## Mô hình người

### `Person`

Lớp cha lưu `ID`, `full_name` và `email`. Phương thức `display_info()` in ba trường này. Các trường được lưu trực tiếp trên đối tượng; lớp không chuẩn hóa hoặc xác nhận định dạng email/mã.

### `Student(Person)`

Thêm `major` và `accumulated_credits`. Constructor gọi `super().__init__()` để khởi tạo phần dữ liệu chung, sau đó yêu cầu số tín chỉ là `int` không âm. `display_info()` ghi tiếp ngành/chương trình học và số tín chỉ sau khi in thông tin cơ sở.

### `Lecturer(Person)`

Thêm `department` và `experience`. Số năm kinh nghiệm phải là `int` không âm. Phương thức hiển thị được ghi đè theo cùng cách với `Student`.

## `Course`

Học phần giữ mã, tên, số tín chỉ và số sinh viên đang đăng ký.

- `credits` là property có kiểm tra: chỉ nhận `int` từ 1 đến 6.
- `number_of_students` là property chỉ nhận `int` không âm.
- `register_student()` tăng số lượng lên một.
- `cancel_registration()` giảm một và trả về `True`; nếu đang bằng 0 thì in thông báo và trả về `False`.
- `calculate_theory_hours()` trả về `credits * 15`.
- `calculate_theory_hourse()` là tên viết sai chính tả nhưng được giữ như một phương thức chuyển tiếp sang `calculate_theory_hours()`.
- `display_info()` in thông tin học phần và số sinh viên.

Trong kiểm tra số nguyên, code dùng `type(value) is int`, nên giá trị boolean cũng bị từ chối dù `bool` là kiểu con của `int` trong Python.

## Luồng demo

`main()` tạo hai học phần, in thông tin, đăng ký ba sinh viên vào học phần thứ nhất, hủy một lượt, tính giờ lý thuyết, rồi in lại trạng thái. Phần cuối tạo một `Student` và một `Lecturer`, đồng thời minh họa hủy đăng ký khi số lượng đang bằng 0.

Chạy từ thư mục repository:

```powershell
python "Bai 2\Course.py"
```

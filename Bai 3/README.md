# Bai 3 — Đóng gói và phương thức trong Python

[`class.py`](./class.py) là chương trình demo ba lớp độc lập: `Student`, `Calculator` và `Circle`. Có thể chạy trực tiếp; `main()` lần lượt kiểm tra các lớp và bắt một số lỗi dữ liệu.

## `Student`

- Lưu tên và GPA trong hai thuộc tính name-mangled `__name`, `__GPA`.
- Property `name` cho phép đọc/ghi tên; setter hiện không kiểm tra tên rỗng.
- Property `gpa` chỉ chấp nhận giá trị trong đoạn `[0.0, 10.0]`; ngoài đoạn này phát sinh `ValueError`.
- Biến lớp `__total_students` đếm số đối tượng khởi tạo thành công. Constructor tăng bộ đếm sau khi gán GPA thành công.
- `print_total_students()` là `classmethod`, đọc bộ đếm qua `cls`.

## `Calculator`

`add(a, b, c=None)` là `staticmethod`: cộng hai đối số nếu `c` không được truyền, nếu không thì cộng cả ba. `Caculator` là bí danh viết sai chính tả trỏ cùng lớp `Calculator`.

## `Circle`

Property `radius` chỉ nhận số dương. `get_area()` tính diện tích `pi * radius²`; `get_parameter()` trả chu vi `2 * pi * radius` (tên method dùng “parameter” thay vì “perimeter”).

## Luồng demo và chạy

`main()`:

1. Tạo các sinh viên hợp lệ, thử GPA 11 và in lỗi.
2. In tổng số sinh viên đã tạo thành công.
3. Gọi phép cộng với số nguyên, số thực và ba toán hạng.
4. Tạo hình tròn hợp lệ, thử bán kính âm, rồi in diện tích và chu vi.

Chạy từ thư mục repository:

```powershell
python "Bai 3\class.py"
```

# Bài 1 — Nhập, xuất và điều kiện

Hai chương trình Python nhỏ minh họa thao tác đọc dữ liệu từ console và xử lý giá trị.

## `a1.py` — In thông tin cá nhân

1. Đọc tên, địa chỉ và số điện thoại bằng `input()`.
2. Ghép ba giá trị vào một chuỗi f-string.
3. In kết quả thành ba dòng có nhãn.

Chương trình giữ dữ liệu dưới dạng chuỗi, không kiểm tra trường rỗng hay định dạng số điện thoại.

Chạy từ thư mục repository:

```powershell
python "Bai 1\a1.py"
```

## `a2.py` — Tìm số lớn nhất

1. Đọc ba đầu vào và chuyển từng giá trị sang `int`.
2. Gán `number1` làm giá trị lớn nhất ban đầu.
3. So sánh `number2`, rồi cập nhật biến `max` nếu lớn hơn.
4. In kết quả.

Đầu vào không phải số nguyên sẽ làm `int()` phát sinh `ValueError`. Có một lỗi logic trong cấu trúc điều kiện hiện tại: phép so sánh `number3` nằm bên trong nhánh `number2 > max`. Vì vậy, nếu số thứ hai không lớn hơn số thứ nhất, số thứ ba sẽ không được so sánh và kết quả có thể sai. Ví dụ `5, 3, 9` sẽ in `5`, dù số lớn nhất là `9`.

Chạy:

```powershell
python "Bai 1\a2.py"
```

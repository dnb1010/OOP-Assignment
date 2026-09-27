# Nhóm dự án và nhân sự

Bài tập OOP chuyển từ đề C++ `Lab03-04-overloading.pdf` sang Python, dùng thư viện chuẩn, gồm `Employee`, `SoftwareEngineer` và `ProjectTeam`.

## Yêu cầu

- Python 3.10 trở lên.
- Không cần cài thư viện ngoài.

## Chạy chương trình minh họa

Mở terminal tại thư mục này rồi chạy:

```powershell
python main.py
```

`main.py` lần lượt trình diễn đủ 15 bước trong phần C của đề.

## Chạy kiểm thử

```powershell
python -m unittest discover -s tests -v
```

## Xem sơ đồ UML

Mở [docs/class_diagram.mmd](docs/class_diagram.mmd) hoặc sơ đồ trong [DESIGN.md](DESIGN.md) bằng Markdown Preview của VS Code để xem Mermaid. Nếu phần xem trước chưa render Mermaid, dùng lệnh **Markdown: Open Preview** và bật hỗ trợ Mermaid của VS Code.

## Quy ước API Python

- `Employee()` dùng giá trị mặc định; `Employee(id, fullName)` và `Employee(id, fullName, baseSalary)` tương ứng các constructor trong đề.
- Kỹ sư dùng thứ tự `SoftwareEngineer(id, fullName, primaryLanguage, baseSalary=0, technicalAllowance=0)`; dạng rút gọn truyền ba tham số đầu.
- `increaseSalary(value)` tăng số tiền cố định; `increaseSalary(value, byPercentage=True)` tăng theo phần trăm.
- Nhóm giữ liên kết `weakref` không sở hữu. Hãy giữ tham chiếu mạnh đến nhân sự ở bên ngoài nhóm khi cần dùng; `with ProjectTeam(...)` hoặc `close()` đóng nhóm một cách xác định mà không đóng nhân sự.

Chi tiết phân tích, bất biến, bội số UML và khác biệt C++/Python nằm trong [DESIGN.md](DESIGN.md).
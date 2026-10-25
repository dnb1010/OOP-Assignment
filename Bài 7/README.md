# Bài 7 - Quản lý và cấp phát thiết bị giảng dạy

Chương trình minh họa repository generic, trạng thái thiết bị, ngoại lệ nghiệp vụ
và Strategy Pattern để chọn thiết bị phù hợp.

## Biên dịch và chạy

Mở PowerShell tại thư mục `Bài 7`, sau đó chạy:

```powershell
$utf8 = New-Object System.Text.UTF8Encoding
[Console]::OutputEncoding = $utf8
$OutputEncoding = $utf8
$null = chcp 65001

$sources = Get-ChildItem -Path .\src -Filter *.java -Recurse | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -Xlint:all -d .\bin $sources
java -cp .\bin lab07.app.Main
```

Thiết lập UTF-8 trước khi chạy để tiếng Việt trong PowerShell hiển thị đúng.
Nếu chỉ thấy chữ bị lỗi mã hóa nhưng kết quả vẫn là `42 đạt, 0 không đạt`, đó
là vấn đề hiển thị của terminal chứ không phải lỗi kiểm thử.

`Main` chạy bộ kiểm tra tích hợp và in kết quả từng trường hợp; chương trình trả
mã thoát khác 0 nếu có kiểm tra thất bại.

## Cấu trúc chính

- `lab07.model`: thiết bị, trạng thái và quy tắc sẵn sàng/điểm ưu tiên.
- `lab07.repository`: repository generic lưu thiết bị trong bộ nhớ.
- `lab07.strategy`: chọn ứng viên đầu tiên hoặc ứng viên có điểm cao nhất.
- `lab07.service`: lọc, chọn và cấp phát thiết bị.
- `lab07.exception`: ngoại lệ nghiệp vụ.

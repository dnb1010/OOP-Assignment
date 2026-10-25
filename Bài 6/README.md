# Bài 6 — Quản lý thiết bị giảng dạy và mượn/trả

Chương trình Java mô hình hóa người mượn, thiết bị, phòng học và phiếu mượn. Package trong mã là `src.borrowing` cùng các package con `borrower`, `model`, `exception`. Mã nguồn thật ở `src/`; `bin/` và `out/` là đầu ra biên dịch.

## Cấu trúc mã nguồn

### `borrower/` — Người mượn

- `IBorrower` định nghĩa mã, họ tên và giới hạn số thiết bị trên một phiếu.
- `Lecturers` cho phép tối đa 3 thiết bị.
- `TeachingAssistants` cho phép tối đa 2 thiết bị.

Hai lớp xác nhận mã và họ tên không null/rỗng. Giới hạn được truy vấn qua interface để `BorrowingSlip` áp dụng quy tắc mà không cần phân nhánh theo loại người mượn.

### `model/` — Mô hình nghiệp vụ

- `DeviceStatus` có ba trạng thái: `Available`, `Borrowed`, `Undermaintenance`.
- `Device` là lớp trừu tượng lưu mã, tên, năm đưa vào sử dụng, giá mua (`BigDecimal`) và trạng thái. Có thể đổi tên; thiết bị cần bảo trì nếu đang bảo trì hoặc đã sử dụng trên 5 năm. Lớp định nghĩa `calculateAnnualMaintenanceCost()` trừu tượng.
- `TeachingDevice` mở rộng `Device` với giới hạn ngày mượn, số tiết tối đa và phí phạt mỗi ngày. `markAsBorrowed()` chỉ thành công khi thiết bị đang sẵn sàng; `maskAsAvaiblable()` chuyển thiết bị đang mượn về sẵn sàng (tên method hiện được viết sai chính tả).
- `AirConRemote`: tối đa 2 ngày/5 tiết, phí trễ 10.000 đồng/ngày, bảo trì hằng năm bằng 5% giá mua.
- `ProjectorRemote`: tối đa 3 ngày/5 tiết, phí trễ 15.000 đồng/ngày, bảo trì bằng 6% giá mua.
- `Microphone`: tối đa 1 ngày/3 tiết, phí trễ 20.000 đồng/ngày, bảo trì bằng 8% giá mua cộng 100.000 đồng/năm.
- `BorrowItem` ghi nhận một thiết bị, ngày mượn/hạn trả, khoảng tiết 1–12 và ngày trả nếu có. Ngày hạn không trước ngày mượn; số tiết không vượt giới hạn loại thiết bị. `getLateDays()` tính số ngày trễ, `calculateLateFee()` nhân ngày trễ với phí theo ngày. Ngày trả không được trước ngày mượn và không thể trả cùng item hai lần.
- `BorrowingSlip` kết hợp một người mượn, phòng dạy và danh sách item. Nếu không truyền ngày mượn thì dùng ngày hiện tại. Thêm thiết bị kiểm tra ngày mượn tối đa, trùng trong phiếu, hạn mức người mượn và trạng thái sẵn sàng trước khi đổi trạng thái thiết bị. Trả thiết bị kiểm tra item và ngày trả trước khi ghi nhận việc trả và mở lại thiết bị. Có các truy vấn thiết bị chưa trả, trả trễ và tổng phí trễ.
- `ClassRoom` lưu mã/tên/sức chứa và danh sách thiết bị. Mã thiết bị không được trùng trong phòng; danh sách getter là chỉ đọc. Lớp hỗ trợ tìm/xóa, tổng chi phí bảo trì hằng năm và lọc thiết bị cần bảo trì.

### `exception/` — Ngoại lệ nghiệp vụ

`BorrowingException` là checked exception cơ sở. Các loại chuyên biệt diễn đạt: vượt hạn mức (`BorrowLimitExceededException`), thiết bị đã trả (`DeviceAlreadyReturnedException`), thiết bị không nằm trong phiếu (`DeviceNotInSlipException`), thiết bị không sẵn sàng (`DeviceUnavailableException`), mã thiết bị trùng (`DuplicateDeviceException`), thời hạn mượn sai (`InvalidBorrowPeriodException`) và ngày trả trước ngày mượn (`InvalidReturnDateException`).

### `Main`

`Main.main()` chạy ba nhóm demo: luồng mượn/trả thành công, các tình huống ngoại lệ và kiểm thử `ClassRoom`/đa hình. Hàm `expect()` xác nhận loại exception kỳ vọng và kiểm tra trạng thái sau lỗi còn nhất quán; cuối chương trình in số kiểm thử đạt/không đạt. Các kịch bản dùng ngày/năm hiện tại để tránh phụ thuộc ngày cố định.

## Quy tắc trạng thái và phí

Luồng trạng thái thiết bị thường là `Available → Borrowed → Available`; bảo trì chuyển `Available → Undermaintenance → Available`. Không thể mượn thiết bị đang mượn hoặc bảo trì. Phí trễ chỉ áp dụng sau hạn trả; trả đúng hạn hoặc sớm hơn cho phí bằng 0.

Các giá trị tiền dùng `BigDecimal` để tránh sai số nhị phân của `double`. Các công thức bảo trì là đa hình: `ClassRoom` gọi cùng một phương thức trên từng `Device` mà không cần kiểm tra kiểu cụ thể.

## Biên dịch và chạy trên Windows PowerShell

Yêu cầu JDK. Từ thư mục repository:

```powershell
Set-Location "Bài 6"
javac -d out (Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName })
java -cp out src.borrowing.Main
```

Trong VS Code, cấu hình hiện tại đặt source path là `src` và output path là `bin`; có thể chạy `Main` từ Java Projects/Run Java. Package trong mã bắt đầu bằng `src.borrowing`, nên khi chạy class đã biên dịch, tên đầy đủ là `src.borrowing.Main`.

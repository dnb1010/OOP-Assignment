/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing.exception;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Ngoại lệ bổ sung: ngày trả không hợp lệ (trước ngày mượn).
 * Lý do bổ sung: đề yêu cầu "Ngày trả không hợp lệ" là một tình huống bất thường,
 * nhưng danh sách ngoại lệ bắt buộc không có lớp riêng cho trường hợp này.
 */
public class InvalidReturnDateException extends BorrowingException {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final String deviceId;
    private final LocalDate borrowedDate;
    private final LocalDate returnedDate;

    public InvalidReturnDateException(String deviceId, LocalDate borrowedDate, LocalDate returnedDate) {
        super("Ngày trả " + returnedDate.format(FMT) + " của thiết bị '" + deviceId
                + "' không được trước ngày mượn " + borrowedDate.format(FMT) + ".");
        this.deviceId = deviceId;
        this.borrowedDate = borrowedDate;
        this.returnedDate = returnedDate;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }
}

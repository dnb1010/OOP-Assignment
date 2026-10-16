/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import src.borrowing.exception.DeviceAlreadyReturnedException;
import src.borrowing.exception.InvalidReturnDateException;

public class BorrowItem {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static final int MIN_PERIOD_NUMBER = 1;
    public static final int MAX_PERIOD_NUMBER = 12;

    private final TeachingDevice device;
    private final LocalDate borrowedDate;
    private final LocalDate dueDate;
    private int fromPeriod;
    private int toPeriod;
    private LocalDate returnedDate; // null = chưa trả

    /** Constructor ngắn: mặc định mượn từ tiết 1 đến số tiết tối đa của thiết bị. */
    public BorrowItem(TeachingDevice device, LocalDate borrowedDate, LocalDate dueDate) {
        this(device, borrowedDate, dueDate, MIN_PERIOD_NUMBER,
                device == null ? MIN_PERIOD_NUMBER : device.getMaxCredits());
    }

    /** Constructor đầy đủ. */
    public BorrowItem(TeachingDevice device, LocalDate borrowedDate, LocalDate dueDate,
                      int fromPeriod, int toPeriod) {
        if (device == null) {
            throw new IllegalArgumentException("Thiet bi khong duoc null");
        }
        if (borrowedDate == null) {
            throw new IllegalArgumentException("Ngay muon khong duoc null");
        }
        if (dueDate == null) {
            throw new IllegalArgumentException("Ngay phai tra khong duoc null");
        }
        if (dueDate.isBefore(borrowedDate)) {
            throw new IllegalArgumentException("Ngay phai tra phai sau hoac bang ngay muon");
        }
        validatePeriods(device, fromPeriod, toPeriod);

        this.device = device;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.fromPeriod = fromPeriod;
        this.toPeriod = toPeriod;
    }

    // ---------- getters / setters ----------
    public TeachingDevice getDevice() {
        return device;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public int getFromPeriod() {
        return fromPeriod;
    }

    public void setFromPeriod(int fromPeriod) {
        validatePeriods(device, fromPeriod, toPeriod);
        this.fromPeriod = fromPeriod;
    }

    public int getToPeriod() {
        return toPeriod;
    }

    public void setToPeriod(int toPeriod) {
        validatePeriods(device, fromPeriod, toPeriod);
        this.toPeriod = toPeriod;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public boolean isReturned() {
        return returnedDate != null;
    }

    // ---------- trả muộn ----------
    public int getLateDays() {
        if (!isReturned()) {
            return 0;
        }
        return (int) Math.max(0, ChronoUnit.DAYS.between(dueDate, returnedDate));
    }

    public boolean isLate() {
        return getLateDays() > 0;
    }

    public BigDecimal calculateLateFee() {
        return device.getLateFeePerDay().multiply(BigDecimal.valueOf(getLateDays()));
    }

    // ---------- trả thiết bị ----------
    public void validateReturn(LocalDate returnedDate)
            throws DeviceAlreadyReturnedException, InvalidReturnDateException {
        if (returnedDate == null) {
            throw new IllegalArgumentException("Ngay tra khong duoc null");
        }
        if (isReturned()) {
            throw new DeviceAlreadyReturnedException(device.getDeviceId());
        }
        if (returnedDate.isBefore(borrowedDate)) {
            throw new InvalidReturnDateException(device.getDeviceId(), borrowedDate, returnedDate);
        }
    }

    public void returnItem(LocalDate returnedDate)
            throws DeviceAlreadyReturnedException, InvalidReturnDateException {
        validateReturn(returnedDate);       // kiểm tra xong mới đổi trạng thái
        this.returnedDate = returnedDate;
    }

    // ---------- kiểm tra tiết ----------
    private static void validatePeriods(TeachingDevice device, int fromPeriod, int toPeriod) {
        if (fromPeriod < MIN_PERIOD_NUMBER || fromPeriod > MAX_PERIOD_NUMBER) {
            throw new IllegalArgumentException(
                    "Tiết bắt đầu phải từ " + MIN_PERIOD_NUMBER + " đến " + MAX_PERIOD_NUMBER + ".");
        }
        if (toPeriod < fromPeriod || toPeriod > MAX_PERIOD_NUMBER) {
            throw new IllegalArgumentException(
                    "Tiết kết thúc phải từ tiết bắt đầu (" + fromPeriod + ") đến " + MAX_PERIOD_NUMBER + ".");
        }
        int periodCount = toPeriod - fromPeriod + 1;
        if (periodCount > device.getMaxCredits()) {
            throw new IllegalArgumentException("Thiết bị '" + device.getDeviceId() + "' chỉ được mượn tối đa "
                    + device.getMaxCredits() + " tiết (yêu cầu: " + periodCount + " tiết).");
        }
    }

    @Override
    public String toString() {
        String returned = isReturned() ? "đã trả " + returnedDate.format(FMT) : "chưa trả";
        return device.getDeviceId() + " - " + device.getDeviceName() + " | tiết " + fromPeriod + "-" + toPeriod
                + " | mượn " + borrowedDate.format(FMT) + ", hạn " + dueDate.format(FMT) + " | " + returned;
    }
}
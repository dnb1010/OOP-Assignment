/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import src.borrowing.borrower.IBorrower;
import src.borrowing.exception.BorrowLimitExceededException;
import src.borrowing.exception.BorrowingException;
import src.borrowing.exception.DeviceNotInSlipException;
import src.borrowing.exception.DeviceUnavailableException;
import src.borrowing.exception.DuplicateDeviceException;
import src.borrowing.exception.InvalidBorrowPeriodException;

/**
 * Phiếu mượn (đề mục 7). Hợp thành các BorrowItem:
 * xóa phiếu thì các dòng mượn mất theo, nhưng thiết bị và người mượn vẫn tồn tại.
 */
public class BorrowingSlip {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final List<BorrowItem> items = new ArrayList<>();

    private final String slipId;
    private final IBorrower borrower;
    private final String teachingRoom;
    private final LocalDate borrowedDate;

    public BorrowingSlip(String slipId, IBorrower borrower, String teachingRoom) {
        this(slipId, borrower, teachingRoom, null);
    }

    /** borrowedDate = null thì lấy ngày hiện tại. */
    public BorrowingSlip(String slipId, IBorrower borrower, String teachingRoom, LocalDate borrowedDate) {
        if (slipId == null || slipId.isBlank()) {
            throw new IllegalArgumentException("Mã phiếu không được rỗng.");
        }
        Objects.requireNonNull(borrower, "Người mượn không được null.");
        if (teachingRoom == null || teachingRoom.isBlank()) {
            throw new IllegalArgumentException("Phòng dạy không được rỗng.");
        }
        this.slipId = slipId.trim();
        this.borrower = borrower;
        this.teachingRoom = teachingRoom.trim();
        this.borrowedDate = borrowedDate != null ? borrowedDate : LocalDate.now();
    }

    public String getSlipId() {
        return slipId;
    }

    public IBorrower getBorrower() {
        return borrower;
    }

    public String getTeachingRoom() {
        return teachingRoom;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    /** Danh sách chỉ đọc (tương ứng IReadOnlyList). */
    public List<BorrowItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Thêm thiết bị vào phiếu, mượn trọn số tiết tối đa của thiết bị.
     * Thứ tự: kiểm tra hết -> tạo BorrowItem -> đổi trạng thái -> thêm vào danh sách.
     */
    public void addDevice(TeachingDevice device, int borrowDays) throws BorrowingException {
        Objects.requireNonNull(device, "Thiết bị không được null.");
        addDevice(device, borrowDays, BorrowItem.MIN_PERIOD_NUMBER, device.getMaxCredits());
    }

    /** Thêm thiết bị với tiết mượn cụ thể (từ tiết - đến tiết). */
    public void addDevice(TeachingDevice device, int borrowDays, int fromPeriod, int toPeriod)
            throws BorrowingException {
        // Bước 1: chỉ kiểm tra, chưa thay đổi bất kỳ trạng thái nào.
        validateBorrowing(device, borrowDays);

        // Bước 2: tạo item (constructor có thể ném lỗi nhưng chưa đổi gì).
        BorrowItem item = new BorrowItem(device, borrowedDate, borrowedDate.plusDays(borrowDays),
                fromPeriod, toPeriod);

        // Bước 3: mọi thứ hợp lệ mới thay đổi trạng thái.
        device.markAsBorrowed();
        items.add(item);
    }

    /**
     * Trả thiết bị theo mã. Trả về phí trả muộn của thiết bị đó.
     * Mọi kiểm tra được thực hiện trước khi thay đổi trạng thái.
     */
    public BigDecimal returnDevice(String deviceId, LocalDate returnedDate) throws BorrowingException {
        // 1. Kiểm tra deviceId.
        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalArgumentException("Mã thiết bị không được rỗng.");
        }
        String id = deviceId.trim();

        // 2-3. Tìm item, không thấy thì báo lỗi.
        BorrowItem item = findItem(id);
        if (item == null) {
            throw new DeviceNotInSlipException(id, slipId);
        }

        // 4-5. Đã trả chưa, ngày trả hợp lệ không (chưa thay đổi gì).
        item.validateReturn(returnedDate);

        if (item.getDevice().getDeviceStatus() != DeviceStatus.Borrowed) {
            throw new IllegalStateException("Thiết bị '" + id + "' không ở trạng thái BORROWED (hiện tại: "
                    + item.getDevice().getDeviceStatus() + ").");
        }

        // 6-7. Ghi nhận ngày trả, chuyển thiết bị về AVAILABLE.
        item.returnItem(returnedDate);
        item.getDevice().maskAsAvaiblable();

        return item.calculateLateFee();
    }

    /** Tổng phí trả muộn của các thiết bị đã trả trong phiếu. */
    public BigDecimal calculateTotalLateFee() {
        BigDecimal total = BigDecimal.ZERO;
        for (BorrowItem item : items) {
            total = total.add(item.calculateLateFee());
        }
        return total;
    }

    /** Các thiết bị chưa trả. */
    public List<BorrowItem> getUnreturnedItems() {
        List<BorrowItem> result = new ArrayList<>();
        for (BorrowItem item : items) {
            if (!item.isReturned()) {
                result.add(item);
            }
        }
        return result;
    }

    /** Các thiết bị đã trả muộn. */
    public List<BorrowItem> getLateReturnedItems() {
        List<BorrowItem> result = new ArrayList<>();
        for (BorrowItem item : items) {
            if (item.isLate()) {
                result.add(item);
            }
        }
        return result;
    }

    private BorrowItem findItem(String deviceId) {
        for (BorrowItem item : items) {
            if (item.getDevice().getDeviceId().equals(deviceId)) {
                return item;
            }
        }
        return null;
    }

    private void validateBorrowing(TeachingDevice device, int borrowDays) throws BorrowingException {
        // 1. device không được null.
        Objects.requireNonNull(device, "Thiết bị không được null.");

        // 2. borrowDays phải lớn hơn 0 (và không vượt giới hạn của thiết bị).
        if (borrowDays <= 0 || borrowDays > device.getMaxBorrowDays()) {
            throw new InvalidBorrowPeriodException(device.getDeviceId(), borrowDays, device.getMaxBorrowDays());
        }

        // 3. Thiết bị chưa tồn tại trong phiếu.
        if (findItem(device.getDeviceId()) != null) {
            throw new DuplicateDeviceException(device.getDeviceId(), "phiếu " + slipId);
        }

        // 4. Người mượn chưa đạt giới hạn thiết bị.
        int maxDevices = borrower.getMaximumDevices();
        if (items.size() >= maxDevices) {
            throw new BorrowLimitExceededException(borrower.getBorrowerId(), maxDevices);
        }

        // 5. Thiết bị đang AVAILABLE.
        if (device.getDeviceStatus() != DeviceStatus.Available) {
            throw new DeviceUnavailableException(device.getDeviceId(), device.getDeviceStatus());
        }
    }

    @Override
    public String toString() {
        return "Phiếu " + slipId + " | " + borrower + " | Phòng " + teachingRoom
                + " | Ngày mượn " + borrowedDate.format(FMT) + " | " + items.size() + " thiết bị";
    }
}

/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing.exception;

import src.borrowing.model.DeviceStatus;

public class DeviceUnavailableException extends BorrowingException {
    private static final long serialVersionUID = 1L;

    private final String DeviceId;
    private final DeviceStatus currentStatus;

    public DeviceUnavailableException(String DeviceId, DeviceStatus currentStatus) {
        super("Thiết bị '" + DeviceId + "' không sẵn sàng để mượn (trạng thái hiện tại: " + currentStatus + ").");
        this.DeviceId = DeviceId;
        this.currentStatus = currentStatus;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public DeviceStatus getCurrentStatus() {
        return currentStatus;
    }

}

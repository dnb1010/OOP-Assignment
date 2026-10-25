/*Đồng Ngọc Bảo 202418849 */
package lab07.exception;

import lab07.model.DeviceStatus;

public class DeviceNotReadyException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String deviceId;
    private final DeviceStatus currentStatus;

    public DeviceNotReadyException(String deviceId, DeviceStatus currentStatus) {
        super("Thiết bị " + deviceId + " chưa sẵn sàng (trạng thái: " + currentStatus + ").");
        this.deviceId = deviceId;
        this.currentStatus = currentStatus;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public DeviceStatus getCurrentStatus() {
        return currentStatus;
    }
}

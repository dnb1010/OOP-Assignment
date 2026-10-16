/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing.exception;

public class DuplicateDeviceException extends BorrowingException{

    private static final long serialVersionUID = 1L;
    private final String deviceId;

    public DuplicateDeviceException(String deviceId, String container) {
        super("Thiết bị '" + deviceId + "' đã tồn tại trong " + container + ".");
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}

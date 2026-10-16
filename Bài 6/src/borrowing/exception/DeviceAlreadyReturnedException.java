/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing.exception;

public class DeviceAlreadyReturnedException extends BorrowingException{
    private static final long serialVersionUID = 1L;
    private final String deviceId;
    public DeviceAlreadyReturnedException(String deviceId){
        super("Thiết bị '" + deviceId + "' đã được trả trước đó.");
        this.deviceId = deviceId;
    }
    public String getDeviceID(){
        return deviceId;
    }
}

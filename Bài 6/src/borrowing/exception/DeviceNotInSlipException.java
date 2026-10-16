/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing.exception;

public class DeviceNotInSlipException extends BorrowingException{
    private static final long serialVersionUID = 1L;
    private final String deviceId;
    private final String slipId;
    public DeviceNotInSlipException(String deviceId, String slipId){
        super("Thiết bị '" + deviceId + "' không thuộc phiếu mượn '" + slipId + "'.");
        this.deviceId = deviceId;
        this.slipId = slipId;
    }
    public String getDeviceId(){
        return deviceId;
    }
    public String getSlipId(){
        return slipId;
    }
}   

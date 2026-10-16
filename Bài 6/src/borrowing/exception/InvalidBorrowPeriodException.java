/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing.exception;

public class InvalidBorrowPeriodException extends BorrowingException{

    private static final long serialVersionUID = 1L;

    private final String deviceId;
    private final int requestedDays;
    private final int maximumDays;

    public InvalidBorrowPeriodException(String deviceId, int requestedDays, int maximumDays) {
        super(buildMessage(deviceId, requestedDays, maximumDays));
        this.deviceId = deviceId;
        this.requestedDays = requestedDays;
        this.maximumDays = maximumDays;
    }

    private static String buildMessage(String deviceId, int requestedDays, int maximumDays) {
        if (requestedDays <= 0) {
            return "Số ngày mượn thiết bị '" + deviceId + "' phải lớn hơn 0 (yêu cầu: " + requestedDays + ").";
        }
        return "Thiết bị '" + deviceId + "' chỉ được mượn tối đa " + maximumDays
                + " ngày (yêu cầu: " + requestedDays + " ngày).";
    }

    public String getDeviceId() {
        return deviceId;
    }

    public int getRequestedDays() {
        return requestedDays;
    }

    public int getMaximumDays() {
        return maximumDays;
    }
}
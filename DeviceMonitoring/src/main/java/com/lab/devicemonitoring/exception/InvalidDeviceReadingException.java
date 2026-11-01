/*Đồng Ngọc Bảo 202418849 */
package com.lab.devicemonitoring.exception;

/** Phat sinh khi gia tri pin hoac tin hieu nam ngoai khoang 0-100. */
public class InvalidDeviceReadingException extends RuntimeException {
    private final String deviceId;
    private final int invalidValue;

    public InvalidDeviceReadingException(String deviceId, int invalidValue, String message) {
        super(message);
        this.deviceId = deviceId;
        this.invalidValue = invalidValue;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public int getInvalidValue() {
        return invalidValue;
    }
}

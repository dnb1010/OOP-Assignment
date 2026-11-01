/*Đồng Ngọc Bảo 202418849 */
package com.lab.devicemonitoring.device;

import java.time.LocalDateTime;

import com.lab.devicemonitoring.event.EventPublisher;
import com.lab.devicemonitoring.event.EventSource;
import com.lab.devicemonitoring.exception.InvalidDeviceReadingException;
import com.lab.devicemonitoring.model.AlertSeverity;
import com.lab.devicemonitoring.model.DeviceAlertEventArgs;
import com.lab.devicemonitoring.model.DeviceStatus;
import com.lab.devicemonitoring.model.DeviceStatusChangedEventArgs;

/** Publisher. Khong tham chieu toi logger, dich vu bao tri hay giao dien. */
public abstract class TeachingDevice {
    private final String id;
    private final String name;
    private DeviceStatus status = DeviceStatus.AVAILABLE;

    private final EventSource<DeviceStatusChangedEventArgs> statusChanged = new EventSource<>();
    private final EventSource<DeviceAlertEventArgs> alertRaised = new EventSource<>();

    protected TeachingDevice(String id, String name) {
        this.id = requireText(id, "id");
        this.name = requireText(name, "name");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    /** Ben ngoai chi nhan view EventPublisher => khong the raise. */
    public EventPublisher<DeviceStatusChangedEventArgs> statusChanged() {
        return statusChanged;
    }

    public EventPublisher<DeviceAlertEventArgs> alertRaised() {
        return alertRaised;
    }

    public void changeStatus(DeviceStatus newStatus) {
        if (newStatus == status) {
            return; // khong doi => khong phat sinh event
        }

        DeviceStatus oldStatus = status;
        status = newStatus; // cap nhat TRUOC, phat event SAU

        onStatusChanged(new DeviceStatusChangedEventArgs(id, oldStatus, newStatus, LocalDateTime.now()));
    }

    protected void raiseAlert(String message, AlertSeverity severity) {
        onAlertRaised(new DeviceAlertEventArgs(id, message, severity, LocalDateTime.now()));
    }

    protected void onStatusChanged(DeviceStatusChangedEventArgs e) {
        statusChanged.raise(this, e, id);
    }

    protected void onAlertRaised(DeviceAlertEventArgs e) {
        alertRaised.raise(this, e, id);
    }

    /** Kiem tra gia tri do 0..100 truoc khi thay doi trang thai. */
    protected void validateReading(int value, String readingName) {
        if (value < 0 || value > 100) {
            throw new InvalidDeviceReadingException(id, value,
                    name + " (" + id + "): " + readingName + " must be in range 0-100, got " + value + ".");
        }
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be null or blank");
        }
        return value;
    }
}

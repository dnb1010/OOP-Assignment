/*Đồng Ngọc Bảo 202418849 */
package com.lab.devicemonitoring.monitoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.lab.devicemonitoring.device.TeachingDevice;
import com.lab.devicemonitoring.event.EventHandler;
import com.lab.devicemonitoring.model.DeviceAlertEventArgs;
import com.lab.devicemonitoring.model.DeviceStatusChangedEventArgs;
import com.lab.devicemonitoring.subscriber.ConsoleNotificationService;
import com.lab.devicemonitoring.subscriber.DeviceAuditLogger;
import com.lab.devicemonitoring.subscriber.MaintenanceService;

/** Noi duy nhat chua subscribe / unsubscribe. close() tuong duong Dispose(). */
public class DeviceMonitoringCoordinator implements AutoCloseable {
    private final List<TeachingDevice> devices = new ArrayList<>();

    // QUAN TRONG: obj::method tao doi tuong MOI moi lan viet va khong equals nhau.
    // Tao handler MOT LAN, luu vao field, roi dung lai de unsubscribe.
    private final EventHandler<DeviceStatusChangedEventArgs> statusHandler;
    private final EventHandler<DeviceAlertEventArgs> notificationHandler;
    private final EventHandler<DeviceAlertEventArgs> maintenanceHandler;

    private boolean subscribed;

    public DeviceMonitoringCoordinator(
            Iterable<? extends TeachingDevice> devices,
            ConsoleNotificationService notificationService,
            DeviceAuditLogger auditLogger,
            MaintenanceService maintenanceService) {
        Objects.requireNonNull(devices).forEach(this.devices::add); // chup lai danh sach

        this.statusHandler = Objects.requireNonNull(auditLogger)::handleStatusChanged;
        this.notificationHandler = Objects.requireNonNull(notificationService)::handleAlert;
        this.maintenanceHandler = Objects.requireNonNull(maintenanceService)::handleCriticalAlert;
    }

    public void subscribe() {
        if (subscribed) {
            return; // khong dang ky trung
        }

        for (TeachingDevice device : devices) {
            device.statusChanged().subscribe(statusHandler);
            device.alertRaised().subscribe(notificationHandler);
            device.alertRaised().subscribe(maintenanceHandler);
        }
        subscribed = true;
    }

    public void unsubscribe() {
        if (!subscribed) {
            return;
        }

        for (TeachingDevice device : devices) {
            device.statusChanged().unsubscribe(statusHandler);
            device.alertRaised().unsubscribe(notificationHandler);
            device.alertRaised().unsubscribe(maintenanceHandler);
        }
        subscribed = false;
    }

    @Override
    public void close() {
        unsubscribe();
    }
}

/*Đồng Ngọc Bảo 202418849 */
package com.lab.devicemonitoring.app;

import java.util.List;

import com.lab.devicemonitoring.device.AirConditionerRemote;
import com.lab.devicemonitoring.device.ProjectorRemote;
import com.lab.devicemonitoring.device.TeachingDevice;
import com.lab.devicemonitoring.device.WirelessMicrophone;
import com.lab.devicemonitoring.event.HandlerFailureException;
import com.lab.devicemonitoring.exception.InvalidDeviceReadingException;
import com.lab.devicemonitoring.model.AlertSeverity;
import com.lab.devicemonitoring.model.DeviceStatus;
import com.lab.devicemonitoring.monitoring.DeviceMonitoringCoordinator;
import com.lab.devicemonitoring.subscriber.ConsoleNotificationService;
import com.lab.devicemonitoring.subscriber.DeviceAuditLogger;
import com.lab.devicemonitoring.subscriber.FilteredAlertSubscriber;
import com.lab.devicemonitoring.subscriber.MaintenanceService;

public class Main {
    public static void main(String[] args) {
        ProjectorRemote projector = new ProjectorRemote("PRJ-01", "Projector remote A1", 50);
        AirConditionerRemote airCon = new AirConditionerRemote("AC-01", "AC remote A1", "A1-203");
        WirelessMicrophone mic = new WirelessMicrophone("MIC-01", "Wireless mic A1", 80, 90);
        List<TeachingDevice> devices = List.of(projector, airCon, mic);

        ConsoleNotificationService notification = new ConsoleNotificationService();
        DeviceAuditLogger audit = new DeviceAuditLogger();
        MaintenanceService maintenance = new MaintenanceService();

        try (DeviceMonitoringCoordinator coordinator =
                     new DeviceMonitoringCoordinator(devices, notification, audit, maintenance)) {
            coordinator.subscribe();
            coordinator.subscribe(); // goi lan 2: khong dang ky trung

            // Subscriber bo sung ma KHONG phai sua TeachingDevice
            FilteredAlertSubscriber criticalOnly = new FilteredAlertSubscriber(
                    alert -> alert.severity() == AlertSeverity.CRITICAL,
                    alert -> System.out.println(
                            "  >> CRITICAL ONLY: " + alert.deviceId() + " - " + alert.message()));
            for (TeachingDevice d : devices) {
                d.alertRaised().subscribe(criticalOnly::handleAlert);
            }

            System.out.println("--- Projector battery ---");
            projector.updateBatteryLevel(35);  // van vung an toan: im lang
            projector.updateBatteryLevel(15);  // Warning
            projector.updateBatteryLevel(10);  // cung vung Warning: im lang
            projector.updateBatteryLevel(0);   // Critical
            projector.updateBatteryLevel(0);   // lap lai: im lang

            System.out.println("--- Air conditioner ---");
            airCon.updateResponseState(false); // Critical
            airCon.updateResponseState(false); // lap lai: im lang

            System.out.println("--- Microphone ---");
            mic.updateBatteryLevel(25);        // Warning
            mic.updateSignalQuality(30);       // Warning
            mic.updateSignalQuality(0);        // Critical

            System.out.println("--- Status changes ---");
            projector.changeStatus(DeviceStatus.BORROWED);
            projector.changeStatus(DeviceStatus.BORROWED);           // khong doi: khong event
            projector.changeStatus(DeviceStatus.UNDER_MAINTENANCE);

            System.out.println("--- Invalid reading ---");
            try {
                projector.updateBatteryLevel(150);
            } catch (InvalidDeviceReadingException ex) {
                System.out.println("Rejected " + ex.getDeviceId() + ": value=" + ex.getInvalidValue()
                        + " | " + ex.getMessage());
            }
            System.out.println("Battery van la " + projector.getBatteryLevel() + "% (trang thai khong bi hong)");

            System.out.println("--- Subscriber loi khong anh huong subscriber khac ---");
            AirConditionerRemote airCon2 = new AirConditionerRemote("AC-02", "AC remote B2", "B2-101");
            airCon2.alertRaised().subscribe((s, e) -> {
                throw new IllegalStateException("Faulty subscriber");
            });
            airCon2.alertRaised().subscribe(notification::handleAlert);
            try {
                airCon2.updateResponseState(false);
            } catch (HandlerFailureException ex) {
                System.out.println("Caught: " + ex.getMessage() + " (" + ex.getErrors().size() + " loi)");
            }

            System.out.println("--- Audit log ---");
            audit.getEntries().forEach(System.out::println);

            System.out.println("--- Maintenance requests ---");
            maintenance.getRequests().forEach(System.out::println);

            coordinator.unsubscribe();
            System.out.println("--- Sau unsubscribe ---");
            mic.updateBatteryLevel(0); // chi criticalOnly (dang ky tay) con in
        }
    }
}

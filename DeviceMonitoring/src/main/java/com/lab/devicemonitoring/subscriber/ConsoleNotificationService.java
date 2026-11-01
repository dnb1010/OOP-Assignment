/*Đồng Ngọc Bảo 202418849 */
package com.lab.devicemonitoring.subscriber;

import com.lab.devicemonitoring.model.DeviceAlertEventArgs;

public class ConsoleNotificationService {
    public void handleAlert(Object sender, DeviceAlertEventArgs e) {
        System.out.println("[" + e.severity() + "] " + e.deviceId() + ": " + e.message());
    }
}

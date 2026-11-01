/*Đồng Ngọc Bảo 202418849 */
package com.lab.devicemonitoring.model;

import java.time.LocalDateTime;

public record DeviceAlertEventArgs(
        String deviceId,
        String message,
        AlertSeverity severity,
        LocalDateTime occurredAt) {
}

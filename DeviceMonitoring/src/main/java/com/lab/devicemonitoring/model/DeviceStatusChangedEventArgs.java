/*Đồng Ngọc Bảo 202418849 */
package com.lab.devicemonitoring.model;

import java.time.LocalDateTime;

public record DeviceStatusChangedEventArgs(
        String deviceId,
        DeviceStatus oldStatus,
        DeviceStatus newStatus,
        LocalDateTime changedAt) {
}

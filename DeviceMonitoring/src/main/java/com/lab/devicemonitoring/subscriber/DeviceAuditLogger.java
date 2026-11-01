/*Đồng Ngọc Bảo 202418849 */
package com.lab.devicemonitoring.subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lab.devicemonitoring.model.DeviceStatusChangedEventArgs;

public class DeviceAuditLogger {
    private final List<String> entries = new ArrayList<>();

    public List<String> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public void handleStatusChanged(Object sender, DeviceStatusChangedEventArgs e) {
        entries.add(e.changedAt() + ": " + e.deviceId() + ", " + e.oldStatus() + " -> " + e.newStatus());
    }
}

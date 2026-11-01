/*Đồng Ngọc Bảo 202418849 */
package com.lab.devicemonitoring.subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lab.devicemonitoring.model.AlertSeverity;
import com.lab.devicemonitoring.model.DeviceAlertEventArgs;

public class MaintenanceService {
    private final List<String> requests = new ArrayList<>();

    public List<String> getRequests() {
        return Collections.unmodifiableList(requests);
    }

    public void handleCriticalAlert(Object sender, DeviceAlertEventArgs e) {
        if (e.severity() != AlertSeverity.CRITICAL) {
            return;
        }
        requests.add("Maintenance request for " + e.deviceId() + ": " + e.message());
    }
}

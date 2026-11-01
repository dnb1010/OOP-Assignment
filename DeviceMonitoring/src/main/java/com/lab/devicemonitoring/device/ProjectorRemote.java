/*Đồng Ngọc Bảo 202418849 */
package com.lab.devicemonitoring.device;

import com.lab.devicemonitoring.model.AlertSeverity;

public class ProjectorRemote extends TeachingDevice {
    private int batteryLevel;

    public ProjectorRemote(String id, String name) {
        this(id, name, 100);
    }

    public ProjectorRemote(String id, String name, int initialBatteryLevel) {
        super(id, name);
        validateReading(initialBatteryLevel, "Battery level");
        this.batteryLevel = initialBatteryLevel;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void updateBatteryLevel(int newLevel) {
        validateReading(newLevel, "Battery level");

        AlertSeverity oldSeverity = severityOf(batteryLevel);
        batteryLevel = newLevel;
        AlertSeverity newSeverity = severityOf(newLevel);

        // Chi canh bao khi roi sang vung xau hon; cung vung / hoi phuc => im lang.
        if (newSeverity.compareTo(oldSeverity) > 0) {
            String message = newSeverity == AlertSeverity.CRITICAL
                    ? "Projector remote battery is empty (0%)."
                    : "Projector remote battery is low (" + newLevel + "%).";
            raiseAlert(message, newSeverity);
        }
    }

    private static AlertSeverity severityOf(int level) {
        return level == 0 ? AlertSeverity.CRITICAL
                : level < 20 ? AlertSeverity.WARNING
                : AlertSeverity.INFORMATION;
    }
}

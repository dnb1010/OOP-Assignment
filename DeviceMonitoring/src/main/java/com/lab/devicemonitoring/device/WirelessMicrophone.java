/*Đồng Ngọc Bảo 202418849 */
package com.lab.devicemonitoring.device;

import com.lab.devicemonitoring.model.AlertSeverity;

public class WirelessMicrophone extends TeachingDevice {
    private static final int BATTERY_WARNING_BELOW = 30;
    private static final int SIGNAL_WARNING_BELOW = 40;

    private int batteryLevel;
    private int signalQuality;

    public WirelessMicrophone(String id, String name) {
        this(id, name, 100, 100);
    }

    public WirelessMicrophone(String id, String name, int initialBatteryLevel, int initialSignalQuality) {
        super(id, name);
        validateReading(initialBatteryLevel, "Battery level");
        validateReading(initialSignalQuality, "Signal quality");
        this.batteryLevel = initialBatteryLevel;
        this.signalQuality = initialSignalQuality;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public int getSignalQuality() {
        return signalQuality;
    }

    public void updateBatteryLevel(int newLevel) {
        validateReading(newLevel, "Battery level");

        AlertSeverity oldSeverity = severityOf(batteryLevel, BATTERY_WARNING_BELOW);
        batteryLevel = newLevel;
        AlertSeverity newSeverity = severityOf(newLevel, BATTERY_WARNING_BELOW);

        if (newSeverity.compareTo(oldSeverity) > 0) {
            String message = newSeverity == AlertSeverity.CRITICAL
                    ? "Microphone battery is empty (0%)."
                    : "Microphone battery is low (" + newLevel + "%).";
            raiseAlert(message, newSeverity);
        }
    }

    public void updateSignalQuality(int newQuality) {
        validateReading(newQuality, "Signal quality");

        AlertSeverity oldSeverity = severityOf(signalQuality, SIGNAL_WARNING_BELOW);
        signalQuality = newQuality;
        AlertSeverity newSeverity = severityOf(newQuality, SIGNAL_WARNING_BELOW);

        if (newSeverity.compareTo(oldSeverity) > 0) {
            String message = newSeverity == AlertSeverity.CRITICAL
                    ? "Microphone signal is lost (0%)."
                    : "Microphone signal quality is low (" + newQuality + "%).";
            raiseAlert(message, newSeverity);
        }
    }

    private static AlertSeverity severityOf(int value, int warningBelow) {
        return value == 0 ? AlertSeverity.CRITICAL
                : value < warningBelow ? AlertSeverity.WARNING
                : AlertSeverity.INFORMATION;
    }
}

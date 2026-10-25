/*Đồng Ngọc Bảo 202418849 */
package lab07.model;

public class ProjectorRemote extends TeachingDevice {
    public static final int MIN_BATTERY = 20;

    private final int batteryLevel;

    public ProjectorRemote(String id, String name, String storageLocation, int batteryLevel) {
        super(id, name, storageLocation);
        this.batteryLevel = requirePercent(batteryLevel, "BatteryLevel");
    }

    public int getBatteryLevel() { return batteryLevel; }

    @Override
    public boolean isReadyForUse() {
        return getStatus() == DeviceStatus.AVAILABLE && batteryLevel >= MIN_BATTERY;
    }

    @Override
    public double getDefaultPriorityScore() {
        return batteryLevel;
    }

    @Override
    public String toString() {
        return super.toString() + "{pin=" + batteryLevel + "%}";
    }
}

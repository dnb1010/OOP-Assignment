package lab07.model;

public class WirelessMicrophone extends TeachingDevice {
    public static final int MIN_BATTERY = 30;
    public static final int MIN_SIGNAL = 40;

    private final int batteryLevel;
    private final int signalQuality;

    public WirelessMicrophone(String id, String name, String storageLocation,
                              int batteryLevel, int signalQuality) {
        super(id, name, storageLocation);
        this.batteryLevel = requirePercent(batteryLevel, "BatteryLevel");
        this.signalQuality = requirePercent(signalQuality, "SignalQuality");
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public int getSignalQuality() {
        return signalQuality;
    }

    @Override
    public boolean isReadyForUse() {
        return getStatus() == DeviceStatus.AVAILABLE
                && batteryLevel >= MIN_BATTERY
                && signalQuality >= MIN_SIGNAL;
    }

    @Override
    public double getDefaultPriorityScore() {
        return (batteryLevel + signalQuality) / 2.0;
    }

    @Override
    public String toString() {
        return super.toString() + "{pin=" + batteryLevel + "%, tín hiệu=" + signalQuality + "%}";
    }
}

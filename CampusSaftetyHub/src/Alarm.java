public final class Alarm extends EmergencyDevice {
    private boolean on;

    public boolean isOn() {
        return on;
    }

    public void activate() {
        on = true;
        System.out.println("[ALARM] Siren ON");
    }

    /** Thiết bị chủ động báo ngược cho mediator. */
    public void silence() {
        on = false;
        System.out.println("[ALARM] Siren silenced manually");
        if (mediator != null) {
            mediator.notify(this, EmergencyEvents.ALARM_SILENCED);
        }
    }
}

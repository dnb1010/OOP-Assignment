import java.util.Objects;

/** Mediator: phối hợp alarm, door, ventilation theo sự kiện. */
public final class EmergencyMediator implements IEmergencyMediator {
    private final Alarm alarm;
    private final DoorController door;
    private final VentilationController ventilation;
    private boolean fireActive;

    public EmergencyMediator(Alarm alarm, DoorController door, VentilationController ventilation) {
        this.alarm = Objects.requireNonNull(alarm, "alarm");
        this.door = Objects.requireNonNull(door, "door");
        this.ventilation = Objects.requireNonNull(ventilation, "ventilation");

        alarm.setMediator(this);
        door.setMediator(this);
        ventilation.setMediator(this);
    }

    @Override
    public void notify(Object sender, String eventName) {
        switch (eventName) {
            case EmergencyEvents.FIRE_DETECTED -> {
                if (fireActive) {
                    return; // tránh kích hoạt lặp
                }
                fireActive = true;
                System.out.println("[MEDIATOR] FIRE_DETECTED -> coordinating response");
                alarm.activate();
                door.unlockAll();
                ventilation.startExhaust();
            }
            case EmergencyEvents.ALARM_SILENCED ->
                System.out.println("[MEDIATOR] Alarm silenced; doors and ventilation stay active");
            default -> System.out.println("[MEDIATOR] Unknown event: " + eventName);
        }
    }
}

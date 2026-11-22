import java.util.Objects;

public final class EmergencyBridgeObserver implements IAlertObserver {
    private final IEmergencyMediator mediator;

    public EmergencyBridgeObserver(IEmergencyMediator mediator){
        this.mediator = Objects.requireNonNull(mediator, "mediator");
    }

    @Override 
    public void Update(AlertEvent e){
        if("CRITICAL".equals(e.severity())){
            mediator.notify(this, EmergencyEvents.FIRE_DETECTED);
        }
    }
}

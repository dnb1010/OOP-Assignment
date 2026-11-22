
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class SensorMonitor {
    private static final double CRITICAL_THRESHOLD = 70.0;
    private static final double WARNING_THRESHOLD = 50.5;

    private final List<IAlertObserver> observers = new ArrayList<>();

    public void attach(IAlertObserver observer){
        Objects.requireNonNull(observer, "observer");
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void detach(IAlertObserver observer){
        observers.remove(observer);
    }
    
    public void checkSensor(String sensorName, ISensor sensor){
        double value = sensor.ReadValue();

        String severity;
        if (value >= CRITICAL_THRESHOLD){
            severity = "CRITICAL";
        } else if(value >= WARNING_THRESHOLD){
            severity = "WARNING";
        }
        else{
            return;
        }
        AlertEvent alertEvent = new AlertEvent(sensorName, value, sensor.GetUnit(), severity, LocalDateTime.now());
        notifyObservers(alertEvent);
    }
    
    private void notifyObservers(AlertEvent alertEvent){
        for (IAlertObserver observer: new ArrayList<>(observers)){
            try {
                observer.Update(alertEvent);
            } catch (Exception ex) {
                System.out.println("[MONITOR][ERROR] " + observer.getClass().getSimpleName() + " failed: " + ex.getMessage());
            }
        }
    }
}

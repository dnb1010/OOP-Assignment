import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AuditLogObserver implements IAlertObserver {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final List<String> log = new ArrayList<>();

    public List<String> getEntries(){
        return Collections.unmodifiableList(log);
    }


    @Override
    public void Update(AlertEvent e) {
        String line = String.format("%s | %s | %.1f%s | %s",
                e.occurredAt().format(FMT), e.sensorName(), e.value(), e.unit(), e.severity());
        log.add(line);
        System.out.println("[AUDIT] " + line);
    }
}

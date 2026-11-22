public class Main {
    public static void main(String[] args) {
        LegacySmokeDetector legacy = new LegacySmokeDetector(300);
        ISensor sensor = new SmokeSensorAdapter(legacy);

        Alarm alarm = new Alarm();
        DoorController door = new DoorController();
        VentilationController vent = new VentilationController();
        EmergencyMediator mediator = new EmergencyMediator(alarm, door, vent);

        SensorMonitor monitor = new SensorMonitor();
        DashBoardObserver dashboard = new DashBoardObserver();
        EmailAlertObserver email = new EmailAlertObserver("admin@school.edu");
        AuditLogObserver audit = new AuditLogObserver();

        monitor.attach(dashboard);
        monitor.attach(dashboard); // đăng ký trùng: bị bỏ qua
        monitor.attach(email);
        monitor.attach(audit);
        monitor.attach(new FaultyObserver());
        monitor.attach(new EmergencyBridgeObserver(mediator));

        System.out.println("--- 0. Adapter test (735) ---");
        LegacySmokeDetector t = new LegacySmokeDetector(735);
        ISensor ts = new SmokeSensorAdapter(t);
        System.out.println(ts.ReadValue() + " " + ts.GetUnit());

        System.out.println("\n--- 1. Normal (30%) ---");
        monitor.checkSensor("Smoke-01", sensor);

        System.out.println("\n--- 2. WARNING (60%) ---");
        legacy.setSmokeLevel(600);
        monitor.checkSensor("Smoke-01", sensor);

        System.out.println("\n--- 3. CRITICAL (73.5%) ---");
        legacy.setSmokeLevel(735);
        monitor.checkSensor("Smoke-01", sensor);

        System.out.println("\n--- 4. Detach email, CRITICAL again ---");
        monitor.detach(email);
        monitor.checkSensor("Smoke-01", sensor);

        System.out.println("\n--- 5. Alarm silenced via mediator ---");
        alarm.silence();
    }
}

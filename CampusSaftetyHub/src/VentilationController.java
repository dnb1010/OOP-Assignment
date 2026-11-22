public final class VentilationController extends EmergencyDevice {
    private boolean running;

    public boolean isRunning() {
        return running;
    }

    public void startExhaust() {
        running = true;
        System.out.println("[VENT] Smoke exhaust fans RUNNING");
    }
}

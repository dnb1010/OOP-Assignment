public final class DoorController extends EmergencyDevice {
    private boolean unlocked;

    public boolean isUnlocked() {
        return unlocked;
    }

    public void unlockAll() {
        unlocked = true;
        System.out.println("[DOOR] All exit doors UNLOCKED");
    }
}

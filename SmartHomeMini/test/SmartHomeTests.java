import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/** Bo test chay doc lap, khong can thu vien ngoai: java -cp out smarthome.SmartHomeTests */
public class SmartHomeTests {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        run("T1 Factory tra dung kieu", SmartHomeTests::t1);
        run("T2 Loai khong ho tro -> IllegalArgumentException", SmartHomeTests::t2);
        run("T3 Singleton cung tham chieu", SmartHomeTests::t3);
        run("T4 GoToWork", SmartHomeTests::t4);
        run("T5 GoToSleep", SmartHomeTests::t5);
        System.out.println("\nKet qua: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }

    private static void t1() {
        check(DeviceFactory.create("light", "L") instanceof SmartLight, "light -> SmartLight");
        check(DeviceFactory.create(" FAN ", "F") instanceof SmartFan, "' FAN ' -> SmartFan");
        check(DeviceFactory.create("Door", "D") instanceof SmartDoor, "Door -> SmartDoor");
    }

    private static void t2() {
        boolean thrown = false;
        try {
            DeviceFactory.create("camera", "Cam");
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check(thrown, "camera phai nem IllegalArgumentException");
    }

    private static void t3() {
        check(HomeConfiguration.getInstance() == HomeConfiguration.getInstance(), "hai lan cung doi tuong");
    }

    private static void t4() {
        SmartLight light = new SmartLight("L");
        SmartFan fan = new SmartFan("F");
        SmartDoor door = new SmartDoor("D");
        quiet(() -> { light.TurnOn(); fan.TurnOn(); door.Unlock(); });

        quiet(() -> new SmartHomeFacade(light, fan, door).goToWork());

        check(!light.isOn(), "den tat");
        check(!fan.isOn(), "quat tat");
        check(door.isLock(), "cua khoa");
    }

    private static void t5() {
        HomeConfiguration.getInstance().setNightLightLevel(30); // reset de test doc lap
        SmartLight light = new SmartLight("L");
        SmartFan fan = new SmartFan("F");
        SmartDoor door = new SmartDoor("D");
        quiet(() -> { fan.TurnOn(); door.Unlock(); });

        String out = capture(() -> new SmartHomeFacade(light, fan, door).goToSleep());

        check(!fan.isOn(), "quat tat");
        check(door.isLock(), "cua khoa");
        check(light.isOn(), "den bat");
        check(out.contains("Night light level: 30%"), "hien muc sang ban dem");
    }

    // ---- tien ich test ----
    private static void run(String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("[PASS] " + name);
        } catch (AssertionError e) {
            failed++;
            System.out.println("[FAIL] " + name + " -> " + e.getMessage());
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static String capture(Runnable action) {
        PrintStream old = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
        try {
            action.run();
        } finally {
            System.setOut(old);
        }
        return buffer.toString();
    }

    private static void quiet(Runnable action) {
        capture(action);
    }
}

public class App {
    public static void main(String[] args) {
        IDevice light = DeviceFactory.create("light", "Living room light");
        IDevice fan = DeviceFactory.create("fan", "Living room fan");
        ILockable door = (ILockable) DeviceFactory.create("door", "Main door");

        HomeConfiguration config1 = HomeConfiguration.getInstance();
        HomeConfiguration config2 = HomeConfiguration.getInstance();
        System.out.println(config1 == config2); // true

        if (config1 != config2) {
            throw new IllegalStateException("Singleton implementation is incorrect");
        }

        System.out.println("--- Trang thai ban dau ---");
        light.TurnOn();
        fan.TurnOff();
        door.Lock();

        SmartHomeFacade facade = new SmartHomeFacade(light, fan, door);

        System.out.println("--- GoToWork ---");
        facade.goToWork();

        System.out.println("--- GoToSleep ---");
        facade.goToSleep();
    }
}
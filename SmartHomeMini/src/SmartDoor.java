public final class SmartDoor implements IDevice, ILockable{
    private final  String name;
    private boolean on;
    private boolean lock;
    public SmartDoor(String name){
        if (name == null || name.isEmpty()){
            throw new IllegalArgumentException("Device's name is required");
        }
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public boolean isOn(){
        return on;
    }

    public boolean isLock(){
        return lock;
    }

    @Override 
    public void TurnOn(){
        on = true;
        System.out.println(name + ": ON");
    }

    @Override 
    public void TurnOff(){
        on = false;
        System.out.println(name + ": OFF");
    }

    @Override 
    public void Lock(){
        lock = true;
        System.out.println(name + ": LOCK");
    }

    @Override 
    public void Unlock(){
        lock = false;
        System.out.println(name + ": UNLOCK");
    }

}   

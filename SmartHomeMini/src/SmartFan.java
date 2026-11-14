public final class SmartFan implements IDevice{
    private final String name;
    private boolean on;

    public SmartFan(String name){
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
}

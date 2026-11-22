import java.util.Objects;

public final class SmokeSensorAdapter  implements ISensor{
    private final LegacySmokeDetector legacy;

    public SmokeSensorAdapter(LegacySmokeDetector legacy){
        this.legacy = Objects.requireNonNull(legacy, "legacy");
    }

    @Override 
    public double ReadValue(){
        return legacy.GetSmokeLevel() / 10.0;
    }

    @Override 
    public String GetUnit(){
        return "% smoke";
    }
}

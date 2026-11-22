public abstract class EmergencyDevice {
    protected IEmergencyMediator mediator;
    
    public void setMediator(IEmergencyMediator mediator){
        this.mediator = mediator;
    }
}

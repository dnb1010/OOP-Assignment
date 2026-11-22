public final class EmailAlertObserver implements IAlertObserver{
    private final String recipient;
    public EmailAlertObserver(String recipient){
        this.recipient = recipient;
    }
    @Override 
    public void Update(AlertEvent e){
        System.out.printf("[EMAIL] To: %s | %s at %s (%.1f%s)%n",
            recipient, e.severity(), e.sensorName(), e.value(), e.unit()
        );
    }
}

public final class DashBoardObserver implements IAlertObserver{
    @Override 
    public void Update(AlertEvent e){
        System.out.printf("[DASHBOARD] %s: %.1f%s [%s]%n",
            e.sensorName(), e.value(), e.unit(), e.severity()
        );
    }
}

/** Observer cố tình lỗi, để test cô lập lỗi. */
public final class FaultyObserver implements IAlertObserver {
    @Override
    public void Update(AlertEvent e) {
        throw new IllegalStateException("boom");
    }
}

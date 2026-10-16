/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing.exception;

public class BorrowLimitExceededException extends BorrowingException{

    private static final long serialVersionUID = 1L;

    private final String borrowerId;
    private final int maximumDevices;

    public BorrowLimitExceededException(String borrowerId, int maximumDevices) {
        super("Người mượn '" + borrowerId + "' đã đạt giới hạn " + maximumDevices + " thiết bị trên một phiếu.");
        this.borrowerId = borrowerId;
        this.maximumDevices = maximumDevices;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public int getMaximumDevices() {
        return maximumDevices;
    }
}

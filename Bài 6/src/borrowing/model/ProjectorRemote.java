/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing.model;

import java.math.BigDecimal;

/** Điều khiển máy chiếu. */
public class ProjectorRemote extends TeachingDevice {

    public ProjectorRemote(String deviceId, String name, int yearInUse, BigDecimal purchasePrice) {
        super(deviceId, name, yearInUse, purchasePrice);
    }

    @Override
    public int getMaxBorrowDays() {
        return 3;
    }

    @Override
    public int getMaxCredits() {
        return 5;
    }

    @Override
    public BigDecimal getLateFeePerDay() {
        return new BigDecimal("15000");
    }

    /** Bảo trì điều khiển máy chiếu: 6% giá mua mỗi năm. */
    @Override
    public BigDecimal calculateAnnualMaintenanceCost() {
        return getPurchasePrice().multiply(new BigDecimal("0.06"));
    }
}

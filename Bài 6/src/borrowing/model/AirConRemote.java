/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing.model;

import java.math.BigDecimal;

public class AirConRemote extends TeachingDevice{
    public AirConRemote(String deviceId, String nameDevice, int yearInUse, BigDecimal purchasePrice){
        super(deviceId, nameDevice, yearInUse, purchasePrice);
    }

    @Override
    public int getMaxBorrowDays() {
        return 2;
    }

    @Override
    public int getMaxCredits() {
        return 5;
    }

    @Override
    public BigDecimal getLateFeePerDay() {
        return new BigDecimal("10000");
    }

    /** Bảo trì điều khiển điều hòa: 5% giá mua mỗi năm. */
    @Override
    public BigDecimal calculateAnnualMaintenanceCost() {
        return getPurchasePrice().multiply(new BigDecimal("0.05"));
    }
}

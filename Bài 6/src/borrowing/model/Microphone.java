/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing.model;
import java.math.BigDecimal;

public class Microphone extends TeachingDevice{
    public Microphone(String deviceId, String DeviceName, int YearOfImplementation, BigDecimal PurchasePrice){
        super(deviceId, DeviceName, YearOfImplementation, PurchasePrice);
    }
    @Override 
    public int getMaxBorrowDays(){
        return 1;
    }
    @Override 
    public int getMaxCredits(){
        return 3;
    }
    @Override 
    public BigDecimal getLateFeePerDay(){
        return new BigDecimal("20000");
    }
    @Override 
    public BigDecimal calculateAnnualMaintenanceCost(){
        return getPurchasePrice().multiply(new BigDecimal("0.08")).add(new BigDecimal("100000"));
    }
}

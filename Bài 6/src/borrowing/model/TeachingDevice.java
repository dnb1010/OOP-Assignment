/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing.model;
import java.math.BigDecimal;
import src.borrowing.exception.DeviceUnavailableException;

public abstract class TeachingDevice extends Device{
    protected TeachingDevice(String DeviceID, String DeviceName, int YearOfImplementation, BigDecimal PurchasePrice){
        super(DeviceID, DeviceName, YearOfImplementation, PurchasePrice);
    }

    /*So ngay duoc muon toi da cho mot lan muon */
    public abstract int getMaxBorrowDays();

    /*So tiet duoc phep muon toi da trong mot lan muon */
    public abstract int getMaxCredits();

    /*Phi tra muon cho moi ngay qua han */
    public abstract BigDecimal getLateFeePerDay();

    /*Chuyen sang borrowed. Chi hop le khi dang o available */
    public void markAsBorrowed() throws DeviceUnavailableException{
        if (Status != DeviceStatus.Available){
            throw new DeviceUnavailableException(getDeviceId(), Status);
        }
        Status = DeviceStatus.Borrowed;
    }

    /*Chuyen ve available. Chi hop le khi dang o borrowed */
    public void maskAsAvaiblable(){
        if (Status != DeviceStatus.Borrowed){
            throw new IllegalArgumentException("Thiết bị '" + getDeviceId() + "' không ở trạng thái BORROWED (hiện tại: " + Status + ").");
        
        }
        Status = DeviceStatus.Available;
    }

}

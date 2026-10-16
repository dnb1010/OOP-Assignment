/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing.model;
import java.math.BigDecimal;
import java.time.Year;
import java.util.Locale;
public abstract class Device {
    public static final int MAINTENANCE_AGE_THRESHOLD_YEARS = 5;

    private final String DeviceId;
    private String DeviceName;
    private final int YearOfImplementation;
    private final BigDecimal PurchasePrice;

    protected  DeviceStatus Status = DeviceStatus.Available;

    protected Device(String DeviceId, String DeviceName, int YearOfImplementation, BigDecimal PurchasePrice) {
        if (DeviceId == null || DeviceId.trim().isEmpty())
            throw new IllegalArgumentException("Ma thiet bi khong duoc rong");
        if (DeviceName == null || DeviceName.trim().isEmpty())
            throw new IllegalArgumentException("Ten thiet bi khong duoc rong");
        if (YearOfImplementation > Year.now().getValue())
            throw new IllegalArgumentException("Nam dua vao su dung khong duoc lon hon nam hien tai");
        this.DeviceId = DeviceId;
        this.DeviceName = requireValidName(DeviceName);
        this.YearOfImplementation = YearOfImplementation;
        this.PurchasePrice = PurchasePrice;
    }

    public String getDeviceId(){
        return DeviceId;
    }

    public String getDeviceName(){
        return DeviceName;
    }

    public int getYearOfImplementation(){
        return YearOfImplementation;
    }

    public BigDecimal getPurchasePrice(){
        return PurchasePrice;
    } 
    
    public DeviceStatus getDeviceStatus(){
        return Status;
    }

    public int getAge(){
        return Year.now().getValue() - YearOfImplementation;
    }

    public boolean requiresMaintenance(){
        return Status == DeviceStatus.Undermaintenance || getAge() > MAINTENANCE_AGE_THRESHOLD_YEARS;
    }

    public void setName(String DeviceName){
        this.DeviceName = requireValidName(DeviceName);
    }

    private static String requireValidName(String name){
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Ten thiet bi khong duoc rong");
        }
        return name.trim();
    }

    public abstract BigDecimal calculateAnnualMaintenanceCost();

    /*Dua thiet bi can bao tri. Khong cho phep khi thiet bi dang duoc muon */
    public void sendToMaintenance(){
        if (Status == DeviceStatus.Borrowed)
            throw new IllegalStateException("Khong the bao tri thiet bi khong dang duoc muon");
        Status = DeviceStatus.Undermaintenance;
    }

    /*Ket thuc bao tri, thiet bi tro lai available */
    public void completeMaintenance(){
        if (Status != DeviceStatus.Undermaintenance){
            throw new IllegalStateException("Thiết bị '" + DeviceId + "' không ở trạng thái bảo trì.");
        }
        Status = DeviceStatus.Available;
    }

    @Override 
    public String toString() {
        return String.format(Locale.US, "[%s] %s - %s | Năm %d | Giá %,.0f đ | %s",
                getClass().getSimpleName(), DeviceId, DeviceName, YearOfImplementation, PurchasePrice, Status);
    }
}


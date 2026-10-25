/*Đồng Ngọc Bảo 202418849 */
package lab07.model;

import lab07.exception.DeviceNotReadyException;

public abstract class TeachingDevice implements IIdentifiable {
    private final String id;
    private final String name;
    private final String storageLocation;
    private DeviceStatus status = DeviceStatus.AVAILABLE;

    protected TeachingDevice(String id, String name, String storageLocation){
        this.id = requireText(id, "Id");
        this.name = requireText(name, "Name");
        this.storageLocation = requireText(storageLocation, "StorageLocation");
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {return name;}

    public String getStorageLocation() {return storageLocation;}

    public DeviceStatus getStatus() {return status;}

    /* Protected set */
    protected void setStatus(DeviceStatus status){
        if (status == null) {
            throw new IllegalArgumentException("Trạng thái không được null.");
        }
        this.status = status;
    }

    public abstract boolean isReadyForUse();

    public abstract double getDefaultPriorityScore();

    public void borrow(){
        if (!isReadyForUse()){
            throw new DeviceNotReadyException(id, status);
        }
        setStatus(DeviceStatus.BORROWED);
    }

    public void returnDevice(){
        if (status != DeviceStatus.BORROWED){
            throw new IllegalStateException(
                "Khong the tra thiet bi" + id + "vi trang thai hien tai la" + status
            );
        }
        setStatus(DeviceStatus.AVAILABLE);
    }

    public void sendToMaintenance(){
        if (status != DeviceStatus.AVAILABLE){
            throw new IllegalStateException("Chỉ thiết bị AVAILABLE mới đưa đi bảo trì được (hiện: " + status + ").");
        }
        setStatus(DeviceStatus.UNDER_MAINTENANCE);
    }


    public void finishMaintenance() {
        if (status != DeviceStatus.UNDER_MAINTENANCE) {
            throw new IllegalStateException(
                    "Thiết bị " + id + " không ở trạng thái bảo trì (hiện: " + status + ").");
        }
        setStatus(DeviceStatus.AVAILABLE);
    }


    protected static int requirePercent(int value, String field) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException(field + " phải nằm trong khoảng 0..100 (nhận: " + value + ").");
        }
        return value;
    }

    protected static String requireText(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " không được để trống.");
        }
        return value.trim();
    }

    @Override 
    public String toString(){
        return getClass().getSimpleName() + "[" + id + ", " + name + ", " + status + "]";
    }
}

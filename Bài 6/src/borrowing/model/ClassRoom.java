/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import src.borrowing.exception.DuplicateDeviceException;
public class ClassRoom {
    private final String RoomID;
    private final String RoomName;
    private final int Capacity;
    private final List<Device> devices = new ArrayList<>();

    public ClassRoom(String RoomID, String RoomName, int Capacity){
        if (RoomID == null || RoomID.isBlank())
            throw new IllegalArgumentException("Ma phong khong duoc rong");
        if (RoomName == null || RoomName.isBlank())
            throw new IllegalArgumentException("Ten phong khong duoc de trong");
        if (Capacity <= 0)
            throw new IllegalArgumentException("Suc chua phai lon hon khong");
        this.RoomID = RoomID;
        this.RoomName = RoomName;
        this.Capacity = Capacity;
    }
    
    public String getRoomID(){
        return RoomID;
    }

    public String getRoomName(){
        return RoomName;
    }

    public int getCapacity(){
        return Capacity;
    }

    public List<Device> getDevices(){
        return Collections.unmodifiableList(devices);
    }

    public void addDevice(Device device) throws DuplicateDeviceException{
        if (device == null){
            throw new IllegalArgumentException("Thiet bi khong duoc null");
        }
        for (Device d: devices){
            if (d.getDeviceId().equals(device.getDeviceId())){
                throw new DuplicateDeviceException(device.getDeviceId(), "Phong " + RoomID);
            }
        }
        devices.add(device);
    }

    public boolean removeDevice(String DeviceID){
        Device device = findDevice(DeviceID);
        return device != null && devices.remove(device);
    }

    public Device findDevice(String DeviceId){
        if (DeviceId == null || DeviceId.isBlank()){
            return null;
        }
        String id = DeviceId.trim();
        for (Device d: devices){
            if (d.getDeviceId().equals(id)){
                return d;
            }
        }
        return null;
    }

        /** Đa hình: mỗi thiết bị tự tính chi phí, không dùng if/switch theo kiểu lớp. */
    public BigDecimal calculateAnnualMaintenanceCost() {
        return devices.stream()
                .map(Device::calculateAnnualMaintenanceCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
 
    public List<Device> getDevicesRequiringMaintenance() {
        List<Device> results = new ArrayList<>();
        for (Device d: devices){
            if (d.requiresMaintenance()){
                results.add(d);
            }
        }
        return results;
    }

    @Override 
    public String toString(){
        return "Phòng " + RoomID + " - " + RoomName + " (sức chứa " + Capacity + ", " + devices.size() + " thiết bị)";
    }
}

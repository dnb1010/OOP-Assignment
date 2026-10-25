/*Đồng Ngọc Bảo - 202418849 */
package lab07.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/** Điều khiển điều hòa. Sẵn sàng khi Available; chọn cho một phòng thì phải tương thích phòng đó. */
public class AirConditionerRemote extends TeachingDevice {
    private final Set<String> compatibleRooms;

    public AirConditionerRemote(String id, String name, String storageLocation,
                                Collection<String> compatibleRooms) {
        super(id, name, storageLocation);
        if (compatibleRooms == null) {
            throw new IllegalArgumentException("CompatibleRooms không được null.");
        }
        Set<String> rooms = new LinkedHashSet<>();
        for (String room : compatibleRooms) {
            rooms.add(requireText(room, "Mã phòng"));
        }
        this.compatibleRooms = Collections.unmodifiableSet(rooms);
    }

    /** Tập phòng tương thích (chỉ đọc). */
    public Set<String> getCompatibleRooms() { return compatibleRooms; }

    public boolean isCompatibleWith(String roomId) {
        return roomId != null && compatibleRooms.contains(roomId.trim());
    }

    /** Điểm ưu tiên khi chọn cho một phòng cụ thể: 100 nếu tương thích, ngược lại 0. */
    public double getPriorityScoreFor(String roomId) {
        return isCompatibleWith(roomId) ? 100 : 0;
    }

    @Override
    public boolean isReadyForUse() {
        return getStatus() == DeviceStatus.AVAILABLE;
    }

    /** Khi chưa gắn với phòng nào: 100 nếu điều khiển tương thích ít nhất một phòng, ngược lại 0. */
    @Override
    public double getDefaultPriorityScore() {
        return compatibleRooms.isEmpty() ? 0 : 100;
    }

    @Override
    public String toString() {
        return super.toString() + "{phòng=" + compatibleRooms + "}";
    }
}

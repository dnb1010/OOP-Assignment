/*Đồng Ngọc Bảo 202418849 */
package com.lab.devicemonitoring.device;

import com.lab.devicemonitoring.model.AlertSeverity;

public class AirConditionerRemote extends TeachingDevice {
    private final String assignedRoom;
    private boolean responsive = true;

    public AirConditionerRemote(String id, String name, String assignedRoom) {
        super(id, name);
        if (assignedRoom == null || assignedRoom.isBlank()) {
            throw new IllegalArgumentException("assignedRoom must not be null or blank");
        }
        this.assignedRoom = assignedRoom;
    }

    public String getAssignedRoom() {
        return assignedRoom;
    }

    public boolean isResponsive() {
        return responsive;
    }

    public void updateResponseState(boolean isResponsive) {
        if (isResponsive == responsive) {
            return; // khong doi => khong lap lai canh bao
        }

        boolean wasResponsive = responsive;
        responsive = isResponsive;

        if (wasResponsive && !isResponsive) {
            raiseAlert("Air conditioner in room " + assignedRoom + " is not responding.",
                    AlertSeverity.CRITICAL);
        }
    }
}

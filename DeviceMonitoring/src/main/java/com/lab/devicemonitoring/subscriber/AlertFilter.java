/*Đồng Ngọc Bảo 202418849 */
package com.lab.devicemonitoring.subscriber;

import com.lab.devicemonitoring.model.DeviceAlertEventArgs;

/** Delegate tu dinh nghia: public delegate bool AlertFilter(DeviceAlertEventArgs alert). */
@FunctionalInterface
public interface AlertFilter {
    boolean test(DeviceAlertEventArgs alert);
}

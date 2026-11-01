/*Đồng Ngọc Bảo 202418849 */
package com.lab.devicemonitoring.subscriber;

import java.util.Objects;
import java.util.function.Consumer;

import com.lab.devicemonitoring.model.DeviceAlertEventArgs;

public class FilteredAlertSubscriber {
    private final AlertFilter filter;
    private final Consumer<DeviceAlertEventArgs> action;

    public FilteredAlertSubscriber(AlertFilter filter, Consumer<DeviceAlertEventArgs> action) {
        this.filter = Objects.requireNonNull(filter);
        this.action = Objects.requireNonNull(action);
    }

    public void handleAlert(Object sender, DeviceAlertEventArgs e) {
        if (filter.test(e)) {
            action.accept(e);
        }
    }
}
